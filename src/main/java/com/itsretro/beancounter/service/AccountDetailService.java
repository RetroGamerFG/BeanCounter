package com.itsretro.beancounter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsretro.beancounter.logic.AccountDetailLogic;
import com.itsretro.beancounter.model.Account;
import com.itsretro.beancounter.model.AccountDetail;
import com.itsretro.beancounter.repositories.AccountDetailRepository;
import com.itsretro.beancounter.viewmodel.AccountDetailView;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AccountDetailService 
{
    @Autowired
    private AccountDetailRepository accountDetailRepository;

    @Autowired
    private AccountDetailLogic accountDetailLogic;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JournalEntryService journalEntryService;

    //
    //
    //
    public AccountDetail getAccountDetailByID(Long id)
    {
        return accountDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    //
    //
    //
    public List<AccountDetail> getAllAccountDetail()
    {
        return accountDetailRepository.findAll();
    }

    //
    //
    //
    public AccountDetail saveNewAccountDetail(AccountDetail accountDetail)
    {
        accountDetailRepository.save(accountDetail);
        return accountDetail;
    }

    //
    //
    //
    public AccountDetailView getAccountDetailView(AccountDetail accountDetail)
    {
        List<Account> matchedAccounts = accountService.getAccountsForAccountDetail(accountDetail.getStartingAccountID(), accountDetail.getEndingAccountID());

        AccountDetailView adv = accountDetailLogic.buildEmptyAccountDetailView(accountDetail, matchedAccounts);
        
        for(Account account : matchedAccounts)
        {
            List<Object[]> queryResults = journalEntryService.getJournalEntriesForAccountDetail(
                accountDetail.getStartingDate(),
                accountDetail.getEndingDate(),
                accountDetail.getGeneratedDate(),
                account.getAccountCode()
            );

            if(queryResults.isEmpty() == false)
            {
                accountDetailLogic.addJournalEntriesToAccountDetailView(adv, account, queryResults);
            }
        }

        //calculate the totals for all stored entries in the account detail view
        accountDetailLogic.calculateTotalsForAccountDetailView(adv);

        return adv;
    }
}
