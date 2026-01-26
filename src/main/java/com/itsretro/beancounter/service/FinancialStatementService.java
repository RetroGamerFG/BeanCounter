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
    // Service Methods
    //

    public FinancialStatement getFinancialStatementByID(Long id)
    {
        return financialStatementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Financial statement not found"));
    }

    public IncomeStatementView getIncomeStatementView(FinancialStatement financialStatement)
    {
        IncomeStatementView isv = new IncomeStatementView();
        
        //determine the end date to query journal entries to. Should match the statement's range type (MTD, QTD, YTD).
        LocalDate endDate = businessInfoLogic.determineEndDate(businessInfo,financialStatement);

        //call the journalEntry repository to get list of 'Revenue' account journal entries
        List<Object[]> queried = journalEntryRepository.findForFinancialStatement(
            financialStatement.getStartingDate(), 
            endDate,
            financialStatement.getCreationDate(), 
            "R"
        );

        //populate the fetched journal entries into the income statement view

        return isv;
    }

}
