package com.itsretro.beancounter.Controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itsretro.beancounter.Model.Account;
import com.itsretro.beancounter.Model.JournalEntry;
import com.itsretro.beancounter.Repositories.AccountRepository;
import com.itsretro.beancounter.Repositories.JournalEntryRepository;

@RestController
public class MCPIntegration
{
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JournalEntryRepository journalEntryRepository;

    @PostMapping("/api/create_journal_entry")
    public ResponseEntity<?> createJournalEntry(@RequestBody JournalEntry journalEntry)
    {
        journalEntry.firstSubmitDefaults(); //assigns creationDate, isEditable, and status

        if(journalEntry.validateLines() ==  false)
        {
            return ResponseEntity.badRequest().body("Validation failed: entry lines do not balance.");
        }

        journalEntry.setStatus("Review");
        journalEntryRepository.save(journalEntry);

        return ResponseEntity.ok("Journal Entry was created successfully, and is ready for review.");
    }

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
}
