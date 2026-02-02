//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalEntryController: a Spring Boot @Controller used to create and view journal entries.
//

package com.itsretro.beancounter.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itsretro.beancounter.exception.BusinessValidationException;
import com.itsretro.beancounter.model.JournalEntry;
import com.itsretro.beancounter.model.JournalEntryLine;
import com.itsretro.beancounter.service.AccountService;
import com.itsretro.beancounter.service.JournalEntryService;

import jakarta.validation.Valid;

@Controller
public class JournalEntryController
{
    @Autowired
    private JournalEntryService journalService;

    @Autowired
    private AccountService accountService;

    //deleteJournalEntry() - with a valid passed journalEntryID, the instance will be removed from the database. This only works with editable entries.
    //inputs - id: a @PathVariable instance of an integer representing the journalEntryID to remove.
    //output - throws an exception if the entry cannot be removed, or reloads the general ledger page otherwise.
    @GetMapping("/delete_journal_entry/{id}")
    public String deleteJournalEntry(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes)
    {
        try
        {
            journalService.removeEntryByID(id);
            redirectAttributes.addFlashAttribute("successMessage", "Entry deleted successfully.");
        }
        catch(Exception e)
        {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/general_ledger";
    }

    //generalLedger() - the main view for the general ledger.
    //inputs - model: the page model to load values into.
    //output - the general ledger page loaded into view.
    @GetMapping("/general_ledger")
    public String generalLedger(Model model)
    {
        model.addAttribute("journalEntry", journalService.getAllUnpostedEntries());
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
        model.addAttribute("allAccounts", accountService.getAllAccounts());
        
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
        JournalEntry journalEntry = journalService.getEntryByID(id);

        model.addAttribute("journalEntry", journalEntry);
        model.addAttribute("allAccounts", accountService.getAllAccounts());
        
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

        if(bindingResult.hasErrors())
        {
            return reloadView(model, journalEntry);
        }

        try
        {
            journalService.saveAndSubmit(journalEntry);
        }
        catch(IllegalArgumentException e)
        {
            bindingResult.reject("lineError", e.getMessage());
            return reloadView(model, journalEntry);
        }

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
        if(bindingResult.hasErrors())
        {
            return reloadView(model, journalEntry);
        }

        try
        {
            journalService.postJournalEntry(journalEntry);
        }
        catch(BusinessValidationException e)
        {
            bindingResult.rejectValue(e.getFieldName(), e.getMessage());
            return reloadView(model, journalEntry);
        }

        return "redirect:/general_ledger";
    }

    //reloadView() - a helper function used if a JournalEntry fails to submit/post.
    //inputs -
        //model: the page model to load values into.
        //journalEntry: the JournalEntry instance to reload into model.
    //output - reloads the page with the journal entry.
    private String reloadView(Model model, JournalEntry journalEntry) 
    {
        model.addAttribute("journalEntry", journalEntry);
        model.addAttribute("allAccounts", accountService.getAllAccounts());

        return "journal_entry_view";
    }
}
