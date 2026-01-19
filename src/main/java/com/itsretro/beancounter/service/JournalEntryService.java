//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalEntryService: a service class used to link the JournalEntry repository and the Journal logic.
// Includes methods for retrieving and storing JournalEntry. 
//

package com.itsretro.beancounter.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.itsretro.beancounter.exception.BusinessValidationException;
import com.itsretro.beancounter.logic.JournalLogic;
import com.itsretro.beancounter.model.JournalEntry;
import com.itsretro.beancounter.repositories.JournalEntryRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class JournalEntryService 
{
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private JournalLogic journalLogic;

    //
    //
    //
    public JournalEntry getEntryByID(Integer id)
    {
        JournalEntry entry = journalEntryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Journal Entry not found"));

        if (entry.getIsEditable() == false)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This entry is locked and cannot be changed");
        }

        return entry;
    }

    //
    //
    //
    public void removeEntryByID(Integer id)
    {
        JournalEntry entry = journalEntryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Journal Entry not found"));

        if(entry.getIsEditable() == false || "Posted".equals(entry.getStatus()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This entry is locked and cannot be removed");
        }

        journalEntryRepository.deleteById(id);
    }

    //
    //
    //
    public List<JournalEntry> getAllUnpostedEntries()
    {
        return journalEntryRepository.findByStatusNot("Posted");
    }

    //
    //
    //
    public Map<Integer, JournalEntry> getEntriesByIDList(List<Integer> idList)
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

    //
    //
    //
    public List<Object[]> getJournalEntriesForAccountDetail(LocalDate startDate, LocalDate endDate, LocalDate generatedDate, BigDecimal accountCode)
    {
        return journalEntryRepository.findForAccountDetail(startDate, endDate, generatedDate, accountCode);
    }

    //
    //
    //
    @Transactional
    public JournalEntry saveAndSubmit(JournalEntry entry)
    {
        //validate the lines, throw exception if fails to balance
        if(!journalLogic.isBalanced(entry.getLines()))
        {
            throw new IllegalArgumentException("Journal Entry does not balance!");
        }

        //update the journal entry for "Review", then save to DB
        entry.setStatus("Review");
        journalEntryRepository.save(entry);

        return entry;
    }

    //
    //
    //
    @Transactional
    public JournalEntry postJournalEntry(JournalEntry entry)
    {
        //validate the lines, throw exception if fails to balance
        if(!journalLogic.isBalanced(entry.getLines()))
        {
            throw new BusinessValidationException("lineError", "The journal lines do not balance.");
        }

        //validate the entry's status is set to "Review", throw exception otherwise
        if("Review".equals(entry.getStatus()) == false)
        {
            throw new BusinessValidationException("statusError", "The journal entry was not review-ready.");
        }

        //validate a post signature was entered, throw exception otherwise
        if (entry.getPostSignature() == null || entry.getPostSignature().isBlank())
        {
            throw new BusinessValidationException("signatureError", "Posting requires a valid signature.");
        }

        //update the journal entry to "Posted", set isEditable to false, then save to DB
        entry.setStatus("Posted");
        entry.setIsEditable(false);
        journalEntryRepository.save(entry);

        return entry;
    }
}
