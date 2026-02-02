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
    
    //getAccountsForAccountDetail() - given a starting and ending Integer representing Account identifiers, fetches a range of Account
        //data stored in the Account Repository.
    //inputs -
        //startID: an Integer representing an Account identifier, and the start of the range.
        //endID: an Integer representing an Account identifier, and the end of the range (inclusive).
    //output - a list of Account instances based on the given search range.
    public List<Account> getAccountsForAccountDetail(Integer startID, Integer endID)
    {
        Account startingAccount = accountRepository.findById(startID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FATAL ERROR: Failed to lookup starting account"));
        Account endingAccount = accountRepository.findById(endID)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FATAL ERROR: Failed to lookup ending account"));

        return accountRepository.findByAccountCodeRange(startingAccount.getAccountCode(), endingAccount.getAccountCode());
    }

    //getAllAccountsForDropdown() - fetches all Account data saved in the Account repository. This is primarily used for HTML inputs.
    //inputs - none.
    //output - a list containing all accounts (should be at least one for program to function...).
    public List<Account> getAllAccounts()
    {
        return accountRepository.findAll();
    }

    //getAllAccountsForAPI() - fetches all Account data saved in the Account repository, then turns result into a Map with the identifier as a key.
        //This is primarily used as output for the API.
    //inputs - none.
    //output - a map with the passed values, the identifier being the key.
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

    //saveNewAccount() - calls the Account repository to save an instance.
    //inputs - account: the Account instance to save into the repository.
    //output - the Account instance, regardless of result.
    public Account saveNewAccount(Account account)
    {
        accountRepository.save(account);
        return account;
    }
}
