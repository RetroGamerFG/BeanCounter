//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalEntryController: a Spring Boot @Controller used to create and view journal entries.
//

package com.itsretro.beancounter.Controller.web;

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

import com.itsretro.beancounter.Model.JournalEntry;
import com.itsretro.beancounter.Model.JournalEntryLine;
import com.itsretro.beancounter.Repositories.AccountRepository;
import com.itsretro.beancounter.Repositories.JournalEntryRepository;

import jakarta.validation.Valid;

@Controller
public class JournalEntryController
{
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private AccountRepository accountRepository;

    //deleteJournalEntry() - with a valid passed journalEntryID, the instance will be removed from the database. This only works with editable entries.
    //inputs - id: a @PathVariable instance of an integer representing the journalEntryID to remove.
    //output - throws an exception if the entry cannot be removed, or reloads the general ledger page otherwise.
    @GetMapping("/delete_journal_entry/{id}")
    public String deleteJournalEntry(@PathVariable("id") Integer id)
    {
        JournalEntry journalEntry = journalEntryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Journal Entry not found"));

        if(journalEntry.getIsEditable() == false)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This entry cannot be removed");
        }

        journalEntryRepository.deleteById(id);

        return "redirect:/general_ledger";
    }

    //generalLedger() - the main view for the general ledger.
    //inputs - model: the page model to load values into.
    //output - the general ledger page loaded into view.
    @GetMapping("/general_ledger")
    public String generalLedger(Model model)
    {
        model.addAttribute("journalEntry", journalEntryRepository.findByStatusNot("Posted"));
        return "general_ledger";
    }

    //journalEntryView() - the view for creating new journal entries.
    //inputs - model: the page model to load values into.
    //output - the journal entry view page loaded into view.
    @GetMapping("/general_ledger/journal_entry_view")
    public String journalEntryView(Model model)
    {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.getLines().add(new JournalEntryLine());
        journalEntry.getLines().add(new JournalEntryLine());

        model.addAttribute("journalEntry", journalEntry);
        model.addAttribute("allAccounts", accountRepository.findAll());
        
        return "journal_entry_view";
    }

    //journalEntryViewExisting() - the view for existing journal entries.
    //inputs -
        //id: a @PathVariable instance of an integer representing the journalEntryID to load.
        //model: the page model to load values into.
    //output - the journal entry view page loaded into view, modified for the existing entry.
    @GetMapping("/general_ledger/journal_entry_view/{id}")
    public String journalEntryViewExisting(@PathVariable("id") Integer id, Model model)
    {
        JournalEntry journalEntry = journalEntryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Journal Entry not found"));

        if(journalEntry.getIsEditable() == false)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This entry cannot be changed");
        }

        model.addAttribute("journalEntry", journalEntry);
        model.addAttribute("allAccounts", accountRepository.findAll());
        
        return "journal_entry_view";
    }

    //submitForm() - takes the user's inputs from journalEntryView() or journalEntryViewExisting() and attempts to store into the database.
    //inputs -
        //journalEntry: a @ModelAttribute instance of a journal entry created from the front-end. Uses the @Valid attribute.
        //bindingResult: a BindingResult used to validate the instance for saving into the database.
        //model: the page model to load values into if saving is unsuccessful.
    //output - reloads the page with error handling if unsuccessful, or reloads the general ledger page otherwise.
    @PostMapping("/save_journal_entry")
    public String submitForm(@Valid @ModelAttribute("journalEntry") JournalEntry journalEntry, BindingResult bindingResult, Model model)
    {
        journalEntry.firstSubmitDefaults(); //assigns creationDate, isEditable, and status
        
        boolean validation = journalEntry.validateLines();

        if(validation == false || bindingResult.hasErrors())
        {
            if(validation == false)
            {
                bindingResult.reject("lineError", "The journal lines do not balance.");
            }

            model.addAttribute("journalEntry", journalEntry);
            model.addAttribute("allAccounts", accountRepository.findAll());

            return "journal_entry_view";
        }

        journalEntry.setStatus("Review");
        journalEntryRepository.save(journalEntry);

        return "redirect:/general_ledger";
    }

    //postJournalEntry() - takes the user's inputs from journalEntryViewExisting() and attempts to update it's values in the database.
    //inputs -
        //journalEntry: a @ModelAttribute instance of a journal entry pulled from the database.
        //bindingResult: a BindingResult used to validate the instance for saving into the database.
        //model: the page model to load values into if saving is unsuccessful.
    //output - reloads the page with error handling if unsuccessful, or reloads the general ledger page otherwise.
    @PostMapping("/post_journal_entry")
    public String postJournalEntry(@Valid @ModelAttribute("journalEntry") JournalEntry journalEntry, BindingResult bindingResult, Model model)
    {
        boolean amountValidation = journalEntry.validateLines();
        boolean statusValidation = journalEntry.getStatus().compareTo("Review") == 0;
        boolean signatureValidation = !journalEntry.getPostSignature().isBlank(); //inverted!

        if(amountValidation == false || statusValidation == false || signatureValidation == false || bindingResult.hasErrors())
        {
            if(amountValidation == false)
            {
                bindingResult.reject("lineError", "The journal lines do not balance.");
            }

            if(statusValidation == false)
            {
                bindingResult.reject("statusError", "The journal entry was not review-ready.");
            }

            if(signatureValidation == false)
            {
                bindingResult.reject("signatureError", "Posting requires a valid signature.");
            }

            model.addAttribute("journalEntry", journalEntry);
            model.addAttribute("allAccounts", accountRepository.findAll());

            return "journal_entry_view";
        }

        journalEntry.setStatus("Posted");
        journalEntry.setIsEditable(false);

        journalEntryRepository.save(journalEntry);

        return "redirect:/general_ledger";
    }
}
