package com.itsretro.beancounter.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.itsretro.beancounter.Model.JournalEntry;
import com.itsretro.beancounter.Model.JournalEntryLine;
import com.itsretro.beancounter.Repositories.AccountRepository;
import com.itsretro.beancounter.Repositories.JournalEntryLineRepository;
import com.itsretro.beancounter.Repositories.JournalEntryRepository;

@Controller
public class JournalEntryController
{
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private JournalEntryLineRepository journalEntryLineRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/general_ledger")
    public String generalLedger(Model model)
    {
        model.addAttribute("journalEntry", journalEntryRepository.findAll());
        return "general_ledger";
    }

    @GetMapping("/general_ledger/journal_entry_view")
    public String journalEntryView(Model model)
    {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.getLines().add(new JournalEntryLine());
        journalEntry.getLines().add(new JournalEntryLine());

        model.addAttribute("journalEntry", journalEntry);
        model.addAttribute("accounts", accountRepository.findAll());
        
        return "journal_entry_view";
    }

    @PostMapping("/save_journal_entry")
    public String submitForm(@ModelAttribute("journalEntry") JournalEntry journalEntry)
    {
        journalEntryRepository.save(journalEntry);
        return "redirect:/journal_entry_list";
    }
}
