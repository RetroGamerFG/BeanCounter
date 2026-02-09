package com.itsretro.beancounter.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsretro.beancounter.logic.BusinessInfoLogic;
import com.itsretro.beancounter.logic.FinancialStatementLogic;
import com.itsretro.beancounter.logic.IncomeStatementLogic;
import com.itsretro.beancounter.model.BusinessInfo;
import com.itsretro.beancounter.model.FinancialStatement;
import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.repositories.FinancialStatementRepository;
import com.itsretro.beancounter.repositories.JournalEntryLineRepository;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FinancialStatementService 
{
    @Autowired
    private FinancialStatementRepository financialStatementRepository;

    @Autowired
    private JournalEntryLineRepository journalEntryLineRepository;

    @Autowired
    private FinancialStatementLogic financialStatementLogic;

    @Autowired
    private IncomeStatementLogic incomeStatementLogic;

    @Autowired
    private BusinessInfoLogic businessInfoLogic;

    private final BusinessInfo businessInfo;

    //
    // Initializer
    //

    public FinancialStatementService(BusinessInfo businessInfo)
    {
        this.businessInfo = businessInfo;
    }

    public BusinessInfo getBusinessInfo()
    {
        return this.businessInfo;
    }

    //
    // Repository Calls
    //

    public FinancialStatement getFinancialStatementByID(Long id)
    {
        return financialStatementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Financial statement not found"));
    }

    public List<FinancialStatement> getAllFinancialStatements()
    {
        return financialStatementRepository.findAll();
    }

    public FinancialStatement saveNewFinancialStatement(FinancialStatement financialStatement)
    {
        financialStatementRepository.save(financialStatement);
        return financialStatement;
    }

    //
    // Service Methods
    //

    public IncomeStatementView getIncomeStatementView(FinancialStatement financialStatement)
    {
        IncomeStatementView isv = new IncomeStatementView();
        
        //determine the end date to query journal entries to. Should match the statement's range type (MTD, QTD, YTD).
        LocalDate endDate = businessInfoLogic.determineEndDate(businessInfo, financialStatement);

        //create the formatted date header based on the statement and range type.
        isv.setDateRangeString(financialStatementLogic.createDateRangeString(
            financialStatement.getRangeType(),
            endDate, 
            false
        ));

        //
        // Column Creation Operation
        //  Depending on user selection, there can be multiple columns for multiple months/quarters/years.
        //  These next steps determine what queries to call, and what columns to create.
        //  Split into private methods to keep this method condensed.

        //
        // Include all months and quarters
        if(financialStatement.getIncludeAllMonths() && financialStatement.getIncludeAllQuarters())
        {
            //createColumnsForAllMonthsAndQuarters(isv, financialStatement, endDate);
        }

        //
        // Include all months only
        else if(financialStatement.getIncludeAllMonths())
        {
            createColumnsForAllMonths(isv, financialStatement, endDate);
        }

        //
        // Include all quarters only
        else if(financialStatement.getIncludeAllQuarters())
        {
            //createColumnsForAllQuarters(isv, financialStatement, endDate);
        }

        //
        // Only include the financial statement's specified range.
        else
        {
            //ASSUMES THIS IS A MTD
            incomeStatementLogic.createColumn(isv, financialStatement.getStartingDate().getMonth().toString(), 0);

            //call the journalEntry repository to get list of 'Revenue' account journal entries
            List<FinancialStatementLine> queriedRev = fetchJournalEntries(
                financialStatement.getStartingDate(), 
                endDate,
                financialStatement.getGeneratedDate(), 
                "R"
            );

            //populate the fetched journal entries into the income statement view
            incomeStatementLogic.addJournalEntriesToColumn(isv, queriedRev, "R", 0);

            //call the journalEntry repository to get list of "Expense" account journal entries
            List<FinancialStatementLine> queriedExp = fetchJournalEntries(
                financialStatement.getStartingDate(), 
                endDate,
                financialStatement.getGeneratedDate(), 
                "X"
            );

            //populate the fetched journal entries into the income statement view
            incomeStatementLogic.addJournalEntriesToColumn(isv, queriedExp, "X", 0);
        }

        //calculate totals
        incomeStatementLogic.calculateTotals(isv);

        //extract all found accounts in each column (necessary for multiple columns where values may not be present)
        incomeStatementLogic.extractMatchedAccountNames(isv);

        System.out.println(); //for breakpoint debugging

        return isv;
    }

    private void createColumnsForAllMonthsAndQuarters(IncomeStatementView isv, FinancialStatement fs, LocalDate endDate)
    {
        
    }

    private void createColumnsForAllMonths(IncomeStatementView isv, FinancialStatement fs, LocalDate endDate)
    {
        int month = businessInfo.getIncorporationDate().getMonthValue();
        int year = endDate.getYear();

        //if the incorporation month is greater than the ending month (i.e. April > January), roll back one year
        if(month > endDate.getMonthValue())
        {
            year--;
        }
        
        for(int m = 0; m < 12; m++)
        {
            int currentMonth = (month + m);

            if(currentMonth > 12)
            {
                currentMonth -= 12;
                month -= 12;
                year++;
            }

            LocalDate currentStart = LocalDate.of(year, currentMonth, 1);
            LocalDate currentEnd = LocalDate.of(year, currentMonth, 1).with(TemporalAdjusters.lastDayOfMonth());

            incomeStatementLogic.createColumn(isv, currentStart.getMonth().toString(), m);

            List<FinancialStatementLine> revenueQuery = fetchJournalEntries(currentStart, currentEnd, fs.getGeneratedDate(), "R");
            incomeStatementLogic.addJournalEntriesToColumn(isv, revenueQuery, "R", m);

            List<FinancialStatementLine> expenseQuery = fetchJournalEntries(currentStart, currentEnd, fs.getGeneratedDate(), "R");
            incomeStatementLogic.addJournalEntriesToColumn(isv, expenseQuery, "X", m);
        }
    }

    private List<FinancialStatementLine> fetchJournalEntries(LocalDate startDate, LocalDate endDate, LocalDate generatedDate, String entryType)
    {
        return journalEntryLineRepository.getAccountsForFinancialStatement(
            startDate,
            endDate,
            generatedDate,
            entryType
        );
    }
}
