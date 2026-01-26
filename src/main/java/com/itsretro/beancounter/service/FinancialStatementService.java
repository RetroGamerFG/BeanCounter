package com.itsretro.beancounter.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsretro.beancounter.logic.BusinessInfoLogic;
import com.itsretro.beancounter.logic.FinancialStatementLogic;
import com.itsretro.beancounter.logic.IncomeStatementLogic;
import com.itsretro.beancounter.model.BusinessInfo;
import com.itsretro.beancounter.model.FinancialStatement;
import com.itsretro.beancounter.repositories.FinancialStatementRepository;
import com.itsretro.beancounter.repositories.JournalEntryRepository;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FinancialStatementService 
{
    @Autowired
    private FinancialStatementRepository financialStatementRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

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

    public IncomeStatementView getIncomeStatementView(FinancialStatement financialStatement)
    {
        IncomeStatementView isv = new IncomeStatementView();
        
        //determine the end date to query journal entries to. Should match the statement's range type (MTD, QTD, YTD).
        LocalDate endDate = businessInfoLogic.determineEndDate(businessInfo,financialStatement);

        //call the journalEntry repository to get list of 'Revenue' account journal entries
        List<Object[]> queriedRev = journalEntryRepository.findForFinancialStatement(
            financialStatement.getStartingDate(), 
            endDate,
            financialStatement.getGeneratedDate(), 
            "R"
        );

        //populate the fetched journal entries into the income statement view
        incomeStatementLogic.addJournalEntriesToIncomeStatementView(isv, queriedRev);

        //call the journalEntry repository to get list of "Expense" account journal entries
        List<Object[]> queriedExp = journalEntryRepository.findForFinancialStatement(
            financialStatement.getStartingDate(), 
            endDate,
            financialStatement.getGeneratedDate(), 
            "E"
        );

        //populate the fetched journal entries into the income statement view
        incomeStatementLogic.addJournalEntriesToIncomeStatementView(isv, queriedExp);

        return isv;
    }
}
