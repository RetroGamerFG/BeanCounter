package com.itsretro.beancounter.controller.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.itsretro.beancounter.model.FinancialStatement;
import com.itsretro.beancounter.service.FinancialStatementService;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

import jakarta.validation.Valid;

@Controller
public class FinancialStatementController 
{
    @Autowired
    FinancialStatementService financialStatementService;

    @GetMapping("/statements")
    public String Statements(Model model)
    {
        model.addAttribute("financialStatement", new FinancialStatement());
        model.addAttribute("allFinancialStatements", financialStatementService.getAllFinancialStatements());

        return "statements";
    }

    @PostMapping("/create_financial_statement")
    public String createAccountDetailEntry(@Valid @ModelAttribute("financialStatement") FinancialStatement financialStatement, BindingResult bindingResult, Model model)
    {
        if(financialStatement.getGeneratedDate() == null)
        {
            financialStatement.setGeneratedDate(LocalDate.now());
        }

        if(bindingResult.hasErrors())
        {
            return reloadView(model, financialStatement);
        }

        financialStatementService.saveNewFinancialStatement(financialStatement);

        return "redirect:/statements";
    }

    @GetMapping("/statements/view/income_statement/{id}")
    public String incomeStatementView(@PathVariable("id") Long id, Model model)
    {
        FinancialStatement financialStatement = financialStatementService.getFinancialStatementByID(id);

        IncomeStatementView incomeStatementView = financialStatementService.getIncomeStatementView(financialStatement);

        model.addAttribute("incomeStatementView", incomeStatementView);
        
        return "income_statement_view";
    }

    private String reloadView(Model model, FinancialStatement financialStatement) 
    {
        model.addAttribute("financialStatement", financialStatement);

        return "statements";
    }
}
