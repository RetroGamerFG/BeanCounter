//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// FinancialStatementController: a Spring Boot @Controller used to create and view financial statements.
//

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
import com.itsretro.beancounter.service.BusinessInfoService;
import com.itsretro.beancounter.service.FinancialStatementService;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

import jakarta.validation.Valid;

@Controller
public class FinancialStatementController 
{
    @Autowired
    FinancialStatementService financialStatementService;

    @Autowired
    BusinessInfoService businessInfoService;

    //statements() - the main view for the statements page.
    //inputs - model: the page model to load values into.
    //output - the statements page loaded into view.
    @GetMapping("/statements")
    public String statements(Model model)
    {
        model.addAttribute("financialStatement", new FinancialStatement());
        model.addAttribute("allFinancialStatements", financialStatementService.getAllFinancialStatements());

        return "statements";
    }

    //createAccountDetailEntry() - takes the user's inputs from the statements page and attempts to store into the database.
    //inputs -
        //financialStatement: a @ModelAttribute instance of a financial statement created from the front-end. Uses the @Valid attribute.
        //bindingResult: a BindingResult used to validate the instance for saving into the database.
        //model: the page model to load values into if saving is unsuccessful.
    //output - reloads the page with error handling if unsuccessful, or reloads the statements page otherwise.
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

    //incomeStatementView() - takes the user's selection and presents an income statement form.
    //inputs -
        //id: a @PathVariable instance of a long representing the FinancialStatement to view.
        //model: the page model to load values into.
    //output - the income statement page loaded into view
    @GetMapping("/statements/view/income_statement/{id}")
    public String incomeStatementView(@PathVariable("id") Long id, Model model)
    {
        FinancialStatement financialStatement = financialStatementService.getFinancialStatementByID(id);

        IncomeStatementView incomeStatementView = financialStatementService.getIncomeStatementView(financialStatement);

        model.addAttribute("incomeStatementView", incomeStatementView);
        model.addAttribute("businessInfo", businessInfoService.getBusinessInfo());
        
        return "income_statement_view";
    }

    //reloadView() - a helper function used if a FinancialStatement fails to generate.
    //inputs -
        //model: the page model to load values into.
        //financialStatement: the FinancialStatement instance to reload into model.
    //output - reloads the page with previous selections from financialStatement.
    private String reloadView(Model model, FinancialStatement financialStatement) 
    {
        model.addAttribute("financialStatement", financialStatement);

        return "statements";
    }
}
