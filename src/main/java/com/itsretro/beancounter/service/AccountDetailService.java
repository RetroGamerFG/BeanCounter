//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetailService: a service class used to link the AccountDetail repository and the AccountDetail logic.
//  Includes methods for retrieving and storing AccountDetail, and building AccountDetailView. 
//

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

    //getAccountDetailByID() - fetches an AccountDetail from the repository given an identifier.
    //inputs - id: a long representing the id of the account detail to search for.
    //output - an AccountDetail matching the id, or an exception is thrown on error.
    public AccountDetail getAccountDetailByID(Long id)
    {
        return accountDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    //getAllAccountDetail() - fetches all AccountDetail from the repository.
    //inputs - none.
    //output - a list containing all instances of AccountDetail.
    public List<AccountDetail> getAllAccountDetail()
    {
        return accountDetailRepository.findAll();
    }

    //saveNewAccountDetail() - calls the AccountDetail repository to save an instance.
    //inputs - accountDetail: an AccountDetail instance to save into the repository.
    //output - the AccountDetail instance, regardless of result.
    public AccountDetail saveNewAccountDetail(AccountDetail accountDetail)
    {
        accountDetailRepository.save(accountDetail);
        return accountDetail;
    }

    //getAccountDetailView() - creates an AccountDetailView instance through several operations.
    //inputs - accountDetail: an AccountDetail instance to convert into a view.
    //output - an AccountDetailView with the passed values.
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

        //call the logic class to create the date line
        adv.setDateString(accountDetailLogic.createDateString(accountDetail));

        return adv;
    }
}
