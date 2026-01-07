package com.itsretro.beancounter.Controller;

import java.time.LocalDate;

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

    @GetMapping("/general_ledger")
    public String generalLedger(Model model)
    {
        model.addAttribute("journalEntry", journalEntryRepository.findAll());
        return "general_ledger";
    }

    //for new entries
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

    //for existing entries
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

    @PostMapping("/save_journal_entry")
    public String submitForm(@Valid @ModelAttribute("journalEntry") JournalEntry journalEntry, BindingResult bindingResult, Model model)
    {
        if(journalEntry.getCreationDate() == null)
        {
            journalEntry.setCreationDate(LocalDate.now()); //assign the time at time of save
        }

        journalEntry.setStatus("Open"); //set status to review, then save to database
        journalEntry.setIsEditable(true);
        
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
