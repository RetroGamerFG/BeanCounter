package com.itsretro.beancounter.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.itsretro.beancounter.model.FinancialStatement;
import com.itsretro.beancounter.service.FinancialStatementService;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

@Controller
public class FinancialStatementController 
{
    @Autowired
    FinancialStatementService financialStatementService;

    @GetMapping("/statements")
    public String Statements(Model model)
    {
        return "statements";
    }

    @GetMapping("/statements/view/income_statement/{id}")
    public String incomeStatementView(@PathVariable("id") Long id, Model model)
    {
        FinancialStatement financialStatement = financialStatementService.getFinancialStatementByID(id);
        
        IncomeStatementView incomeStatementView = financialStatementService.getIncomeStatementView(financialStatement);

        model.addAttribute("incomeStatementView", incomeStatementView);
        
        return "income_statement_view";
    }
    
}
