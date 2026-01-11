//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// MCPIntegration: an optional Spring Boot @RestController that exposes the API to allow AI agents and/or MCP server
// instances to fetch and store valid data into the program. See the 'BeanCounter-Agent' project for prepared tools to download.
//
// The developer(s) of this program make no guarantees regarding the accuracy, reliability, or regulatory compliance of 
// AI-generated outputs and disclaim all liability arising from their use without appropriate human oversight.
//

package com.itsretro.beancounter.Controller.api;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itsretro.beancounter.Model.Account;
import com.itsretro.beancounter.Model.JournalEntry;
import com.itsretro.beancounter.Model.JournalEntryLine;
import com.itsretro.beancounter.Repositories.AccountRepository;
import com.itsretro.beancounter.Repositories.JournalEntryRepository;

@RestController
public class MCPIntegration
{
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JournalEntryRepository journalEntryRepository;

    //createJournalEntry() - takes an API call and creates a journal entry.
    //inputs - journalEntry: a @RequestBody representation, parsed into a journal entry model.
    //output - a ResponseEntity based on the result of the operation.
    @PostMapping("/api/create_journal_entry")
    public ResponseEntity<?> createJournalEntry(@RequestBody JournalEntry journalEntry)
    {
        //force removal of journalEntryID and journalEntryLineID's for database compatability
        journalEntry.setJournalEntryID(null);

        for(JournalEntryLine jel : journalEntry.getLines())
        {
            jel.setJournalEntryLineID(null);
        }

        //assigns creationDate, isEditable, and status
        journalEntry.firstSubmitDefaults();

        if(journalEntry.getPostDate() == null)
        {
            journalEntry.setPostDate(LocalDate.now());
        }

        if(journalEntry.validateLines() ==  false)
        {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "VALIDATION_FAILED",
                "message", "Journal entry line amounts are invalid. Ensure total debits equal total credits, and only a single debit or credit amount is entered on each line.",
                "data", journalEntry
            ));
        }

        journalEntry.setStatus("Review");
        journalEntryRepository.save(journalEntry);

        return ResponseEntity.ok(Map.of("message", "Journal Entry was created successfully, and is ready for review."));
    }

    //createAccount() - takes an API call and creates a new account.
    //inputs - account: a @RequestBody representation, parsed into an account model.
    //output - the account instance with embedded result code.
    @PostMapping("/api/create_account")
    public Account createAccount(@RequestBody Account account)
    {
        //force removal of accountID for database compatability
        account.setAccountID(null);

        accountRepository.save(account);
        return account;
    }

    //getAccounts() - takes an API call and returns all the available accounts as JSON.
    //inputs - none.
    //output - a JSON representation of all accounts in Map format, the accountID being the key
    @GetMapping("/api/get_accounts")
    public Map<Integer, Account> getAccounts()
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

    //getJournalEntries() - takes an API call and returns valid accounts identified by ID.
    //inputs - a @RequestParam representation of a list of integers representing one or more journalEntryID.
    //output - a JSON representation of all found journal entries in Map format, the journalEntryID being the key.
    @GetMapping("/api/get_journal_entries")
    public Map<Integer, JournalEntry> getJournalEntries(@RequestParam("id_list") List<Integer> idList)
    {
        List<JournalEntry> matchedEntries = journalEntryRepository.findAllById(idList);

        //transform result into a map for JSON proper
        Map<Integer, JournalEntry> result = new HashMap<>();

        for(JournalEntry journalEntry : matchedEntries)
        {
            result.put(journalEntry.getJournalEntryID(), journalEntry);
        }

        return result;
    }
}
