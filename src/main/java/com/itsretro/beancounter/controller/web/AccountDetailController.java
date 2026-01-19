//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetailController: a Spring Boot @Controller used to create and view account detail forms.
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

import com.itsretro.beancounter.model.AccountDetail;
import com.itsretro.beancounter.service.AccountDetailService;
import com.itsretro.beancounter.service.AccountService;
import com.itsretro.beancounter.viewmodel.AccountDetailView;

import jakarta.validation.Valid;

@Controller
public class AccountDetailController 
{
    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private AccountService accountService;

    //accountDetail() - the main view for the account detail.
    //inputs - model: the page model to load values into.
    //output - the account detail page loaded into view.
    @GetMapping("/account_detail")
    public String accountDetail(Model model)
    {
        model.addAttribute("accountDetail", new AccountDetail());
        model.addAttribute("allAccountDetails", accountDetailService.getAllAccountDetail());
        model.addAttribute("allAccounts", accountService.getAllAccountsForDropdown());

        return "account_detail";
    }

    //createAccountDetailEntry() - takes the user's inputs from accountDetail() and attempts to store into the database.
    //inputs -
        //accountDetail: a @ModelAttribute instance of an account detail created from the front-end. Uses the @Valid attribute.
        //bindingResult: a BindingResult used to validate the instance for saving into the database.
        //model: the page model to load values into if saving is unsuccessful.
    //output - reloads the page with error handling if unsuccessful, or reloads the account detail page otherwise.
    @PostMapping("/create_account_detail")
    public String createAccountDetailEntry(@Valid @ModelAttribute("accountDetail") AccountDetail accountDetail, BindingResult bindingResult, Model model)
    {
        if(accountDetail.getGeneratedDate() == null)
        {
            accountDetail.setGeneratedDate(LocalDate.now());
        }

        if(bindingResult.hasErrors())
        {
            return reloadView(model, accountDetail);
        }

        accountDetailService.saveNewAccountDetail(accountDetail);

        return "redirect:/account_detail";
    }

    //accountDetailView() - takes the user's selection and presents an account detail form.
    //inputs -
        //id: a @PathVariable instance of a long representing the AccountDetail to view.
        //model: the page model to load values into.
    //output - the account detail page loaded into view
    @GetMapping("/account_detail/view/{id}")
    public String accountDetailView(@PathVariable("id") Long id, Model model)
    {
        AccountDetail accountDetail = accountDetailService.getAccountDetailByID(id);
        
        AccountDetailView accountDetailView = accountDetailService.getAccountDetailView(accountDetail);

        model.addAttribute("accountDetailView", accountDetailView);
        
        return "account_detail_view";
    }

    //reloadView() - a helper function used if a AccountDetail fails to generate.
    //inputs -
        //model: the page model to load values into.
        //accountDetail: the AccountDetail instance to reload into model.
    //output - reloads the page with previous selections from accountDetail.
    private String reloadView(Model model, AccountDetail accountDetail) 
    {
        model.addAttribute("accountDetail", accountDetail);
        model.addAttribute("allAccounts", accountService.getAllAccountsForDropdown());

        return "account_detail";
    }
}