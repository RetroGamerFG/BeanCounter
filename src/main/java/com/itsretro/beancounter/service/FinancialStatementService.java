//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// FinancialStatementService: a service class used to link the FinancialStatement repository and FinancialStatement logic.
//  Includes methods for creating views and building columns to store within the views.
//

package com.itsretro.beancounter.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsretro.beancounter.logic.FinancialStatementLogic;
import com.itsretro.beancounter.model.FinancialStatement;
import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.repositories.FinancialStatementRepository;
import com.itsretro.beancounter.repositories.JournalEntryLineRepository;

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

    public List<FinancialStatementLine> fetchJournalEntries(LocalDate startDate, LocalDate endDate, LocalDate generatedDate, String entryType)
    {
        return journalEntryLineRepository.getAccountsForFinancialStatement(
            startDate,
            endDate,
            generatedDate,
            entryType
        );
    }

    //
    // Service Methods
    //

    public String createDateRangeString(String rangeType, LocalDate endDate, boolean dateOnly)
    {
        return financialStatementLogic.createDateRangeString(
            rangeType,
            endDate, 
            dateOnly
        );
    }
}
