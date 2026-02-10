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

    public IncomeStatementView getIncomeStatementView(FinancialStatement fs)
    {
        IncomeStatementView isv = new IncomeStatementView();

        LocalDate endDate = null;
        
        //determine the end date to query journal entries to. Should match the statement's range type (MTD, QTD, YTD).
        if("QTD".compareToIgnoreCase(fs.getRangeType()) == 0)
        {
            endDate = businessInfoLogic.determineEndDate(fs.getStartingDate(), true, false);
        }
        else if("YTD".compareToIgnoreCase(fs.getRangeType()) == 0)
        {
            endDate = businessInfoLogic.determineEndDate(fs.getStartingDate(), false, true);
        }
        else
        {
            endDate = businessInfoLogic.determineEndDate(fs.getStartingDate(), false, false);
        }

        //create the formatted date header based on the statement and range type.
        isv.setDateRangeString(financialStatementLogic.createDateRangeString(
            fs.getRangeType(),
            endDate, 
            false
        ));

        //
        // Column Creation Operation
        //  Depending on user selection, there can be multiple columns for multiple months/quarters/years.
        //  These next steps determine what queries to call, and what columns to create.
        //  Split into private methods to keep this method condensed.

        if("YTD".compareToIgnoreCase(fs.getRangeType()) == 0) //YTD, 12 months and/or 4 quarters
        {
            if(fs.getIncludeAllMonths() && fs.getIncludeAllQuarters())
            {
                createColumnsForMonthRange(isv, fs, 1);
                createColumnForQuarter(isv, fs, 1);

                createColumnsForMonthRange(isv, fs, 2);
                createColumnForQuarter(isv, fs, 2);

                createColumnsForMonthRange(isv, fs, 3);
                createColumnForQuarter(isv, fs, 3);

                createColumnsForMonthRange(isv, fs, 4);
                createColumnForQuarter(isv, fs, 4);
            }
            else if (fs.getIncludeAllMonths())
            {
                createColumnsForMonthRange(isv, fs, 1);
                createColumnsForMonthRange(isv, fs, 2);
                createColumnsForMonthRange(isv, fs, 3);
                createColumnsForMonthRange(isv, fs, 4);
            }
            else if (fs.getIncludeAllQuarters())
            {
                createColumnForQuarter(isv, fs, 1);
                createColumnForQuarter(isv, fs, 2);
                createColumnForQuarter(isv, fs, 3);
                createColumnForQuarter(isv, fs, 4);
                
            }

            createColumn(isv, fs, String.valueOf(endDate.getYear()), fs.getStartingDate(), endDate);
        }
        else if ("QTD".compareToIgnoreCase(fs.getRangeType()) == 0) //QTD, 3 months
        {
            if (fs.getIncludeAllMonths())
            {
                createColumnsForMonthRange(isv, fs, businessInfoLogic.getQuarterByMonth(endDate.getMonth()));
            }

            createColumn(isv, fs, "Q" + businessInfoLogic.getQuarterByMonth(endDate.getMonth()), fs.getStartingDate(), endDate);
        }
        else //MTD, one month only
        {
            createColumn(isv, fs, fs.getStartingDate().getMonth().toString(), fs.getStartingDate(), endDate);
        }

        //calculate totals
        incomeStatementLogic.calculateTotals(isv);

        //extract all found accounts in each column (necessary for multiple columns where values may not be present)
        incomeStatementLogic.extractMatchedAccountNames(isv);

        System.out.println(); //for breakpoint debugging

        return isv;
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

    private void createColumn(IncomeStatementView isv, FinancialStatement fs, String columnLabel, LocalDate startDate, LocalDate endDate)
    {
        int colIndex = isv.getColumnCount();

        incomeStatementLogic.createColumn(isv, columnLabel, colIndex);

        //call the journalEntry repository to get list of 'Revenue' account journal entries
        List<FinancialStatementLine> queriedRev = fetchJournalEntries(
            startDate,
            endDate,
            fs.getGeneratedDate(), 
            "R"
        );

        //populate the fetched journal entries into the income statement view
        incomeStatementLogic.addJournalEntriesToColumn(isv, queriedRev, "R", colIndex);

        //call the journalEntry repository to get list of "Expense" account journal entries
        List<FinancialStatementLine> queriedExp = fetchJournalEntries(
            startDate,
            endDate,
            fs.getGeneratedDate(), 
            "X"
        );

        //populate the fetched journal entries into the income statement view
        incomeStatementLogic.addJournalEntriesToColumn(isv, queriedExp, "X", colIndex);
    }

    //needs revision in regard to year
    private void createColumnsForMonthRange(IncomeStatementView isv, FinancialStatement fs, int quarter)
    {
        int startMonth = businessInfoLogic.getQuarterStartMonth(quarter).getValue();
        int year = fs.getStartingDate().getYear();

        if(startMonth >= fs.getStartingDate().getMonthValue())
        {
            year--;
        }

        for(int m = 0; m < 3; m++)
        {
            int currentMonth = (startMonth + m);

            if(currentMonth > 12)
            {
                currentMonth -= 12;
                startMonth -= 12;
                year++;
            }

            LocalDate currentStart = LocalDate.of(year, currentMonth, 1);
            LocalDate currentEnd = LocalDate.of(year, currentMonth, 1).with(TemporalAdjusters.lastDayOfMonth());

            createColumn(isv, fs, currentStart.getMonth().toString(), currentStart, currentEnd);
        }
    }

    private void createColumnForQuarter(IncomeStatementView isv, FinancialStatement fs, int quarter)
    {
        int startMonth = businessInfoLogic.getQuarterStartMonth(quarter).getValue();
        int startYear = fs.getStartingDate().getYear();

        if(startMonth >= fs.getStartingDate().getMonthValue())
        {
            startYear--;
        }

        int endMonth = businessInfoLogic.getQuarterEndMonth(quarter).getValue();
        int endYear = startYear;

        if(startMonth < fs.getStartingDate().getMonthValue())
        {
            endYear++;
        }

        LocalDate quarterStartDate = LocalDate.of(startYear, startMonth, 1);
        LocalDate quarterEndDate = LocalDate.of(endYear, endMonth, 1).with(TemporalAdjusters.lastDayOfMonth());

        createColumn(isv, fs, "Q" + quarter, quarterStartDate, quarterEndDate);
    }
}
