package com.itsretro.beancounter.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import com.itsretro.beancounter.Model.Account;
import com.itsretro.beancounter.Model.AccountDetail;
import com.itsretro.beancounter.Model.JournalEntry;
import com.itsretro.beancounter.Model.JournalEntryLine;
import com.itsretro.beancounter.Repositories.AccountDetailRepository;
import com.itsretro.beancounter.Repositories.AccountRepository;
import com.itsretro.beancounter.Repositories.JournalEntryRepository;
import com.itsretro.beancounter.ViewModel.AccountDetailLine;

import jakarta.validation.Valid;

@Controller
public class AccountDetailController 
{
    @Autowired
    AccountDetailRepository accountDetailRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @GetMapping("/account_detail")
    public String accountDetail(Model model)
    {
        model.addAttribute("accountDetail", new AccountDetail());
        model.addAttribute("allAccountDetails", accountDetailRepository.findAll());
        model.addAttribute("allAccounts", accountRepository.findAll());

        return "account_detail";
    }

    @PostMapping("/create_account_detail")
    public String createAccountDetailEntry(@Valid @ModelAttribute("accountDetail") AccountDetail accountDetail, BindingResult bindingResult, Model model)
    {
        if(accountDetail.getGeneratedDate() == null)
        {
            accountDetail.setGeneratedDate(LocalDate.now());
        }

        if(bindingResult.hasErrors())
        {
            model.addAttribute("accountDetail", accountDetail);
            model.addAttribute("allAccounts", accountRepository.findAll());

            return "account_detail";
        }

        accountDetailRepository.save(accountDetail);

        return "redirect:/account_detail";
    }

    @GetMapping("/account_detail/view/{id}")
    public String viewAccountDetail(@PathVariable("id") Long id, Model model)
    {
        //fetch the accountDetail instance
        AccountDetail accountDetail = accountDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Detail not found"));

        //fetch the starting and ending accounts (used to get account code range)
        Account startingAccount = accountRepository.findById(accountDetail.getStartingAccountID()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FATAL ERROR: Failed to lookup starting account"));
        Account endingAccount = accountRepository.findById(accountDetail.getEndingAccountID()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FATAL ERROR: Failed to lookup ending account"));

        //fetch all accounts from the specified range
        List<Account> matchedAccounts = accountRepository.findByAccountCodeRange(
            startingAccount.getAccountCode(),
            endingAccount.getAccountCode()
        );

        //fetch all journal entries that were:
            //1 - created before the detail's generated date
            //2 - have a post date found within the range
            //3 - have a journal entry that matches the account range
        List<JournalEntry> matchedEntries = journalEntryRepository.findForAccountDetail(
            accountDetail.getStartingDate(),
            accountDetail.getEndingDate(),
            accountDetail.getGeneratedDate(),
            startingAccount.getAccountCode(),
            endingAccount.getAccountCode()
        );

        //create instances of 'AccountDetailLine' for every account, matching for JournalEntry instances and valid lines
        List<AccountDetailLine> fullAccountDetail = new ArrayList<>();

        for(Account account : matchedAccounts)
        {
            AccountDetailLine adl = new AccountDetailLine(account);

            for(JournalEntry entry : matchedEntries)
            {
                List<JournalEntryLine> lines = entry.getLinesByAccountID(account.getAccountID()); //likely needs revision
                adl.createEntryGroup(entry, lines);
            }

            adl.calculateGrandTotal(); //force call to calculate grand total(s) following all entries added
            fullAccountDetail.add(adl);
        }

        model.addAttribute("accountDetailLines", fullAccountDetail);

        return "account_detail_view";
    }
}
