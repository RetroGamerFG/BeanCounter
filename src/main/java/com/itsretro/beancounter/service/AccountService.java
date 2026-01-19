//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountService: a service class used to link the Account repository.
// Includes methods for retrieving and storing Account. 
//

package com.itsretro.beancounter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.itsretro.beancounter.model.Account;
import com.itsretro.beancounter.repositories.AccountRepository;

@Service
public class AccountService 
{
    @Autowired
    private AccountRepository accountRepository;
    
    //
    //
    //
    public List<Account> getAccountsForAccountDetail(Integer startID, Integer endID)
    {
        Account startingAccount = accountRepository.findById(startID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FATAL ERROR: Failed to lookup starting account"));
        Account endingAccount = accountRepository.findById(endID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FATAL ERROR: Failed to lookup ending account"));

        return accountRepository.findByAccountCodeRange(startingAccount.getAccountCode(), endingAccount.getAccountCode());
    }

    //
    //
    //
    public List<Account> getAllAccountsForDropdown()
    {
        return accountRepository.findAll();
    }

    //
    //
    //
    public Map<Integer, Account> getAllAccountsForAPI()
    {
        List<Account> allAccounts = accountRepository.findAll();

        //transform result into a map for JSON proper
        Map<Integer, Account> result = new HashMap<>();

        for(Account account : allAccounts)
        {
            result.put(account.getAccountID(), account);
        }

        return result;
    }

    //
    //
    //
    public Account saveNewAccount(Account account)
    {
        accountRepository.save(account);
        return account;
    }
}
