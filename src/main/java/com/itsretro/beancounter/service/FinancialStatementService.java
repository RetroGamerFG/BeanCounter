package com.itsretro.beancounter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsretro.beancounter.config.BusinessConfig;
import com.itsretro.beancounter.logic.IncomeStatementLogic;
import com.itsretro.beancounter.model.FinancialStatement;
import com.itsretro.beancounter.repositories.FinancialStatementRepository;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FinancialStatementService 
{
    @Autowired
    private FinancialStatementRepository financialStatementRepository;

    @Autowired
    private IncomeStatementLogic incomeStatementLogic;

    @Autowired
    private BusinessConfig businessConfig;

    public FinancialStatement getFinancialStatementByID(Long id)
    {
        return financialStatementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Financial statement not found"));
    }

    public IncomeStatementView getIncomeStatementView(FinancialStatement financialStatement)
    {
        IncomeStatementView isv = new IncomeStatementView();
        return isv;
    }

}
