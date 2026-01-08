package com.itsretro.beancounter.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.itsretro.beancounter.Model.AccountDetail;
import com.itsretro.beancounter.Repositories.AccountDetailRepository;
import com.itsretro.beancounter.Repositories.AccountRepository;

import jakarta.validation.Valid;

@Controller
public class AccountDetailController 
{
    @Autowired
    AccountDetailRepository accountDetailRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/account_detail")
    public String accountDetail(Model model)
    {
        model.addAttribute("accountDetail", new AccountDetail());
        model.addAttribute("allAccounts", accountRepository.findAll());

        return "account_detail";
    }

    @PostMapping("/create_account_detail")
    public String createAccountDetailEntry(@Valid @ModelAttribute("accountDetail") AccountDetail accountDetail, BindingResult bindingResult, Model model)
    {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("accountDetail", accountDetail);
            model.addAttribute("allAccounts", accountRepository.findAll());

            return "account_detail";
        }

        return "redirect:/account_detail";
    }
}
