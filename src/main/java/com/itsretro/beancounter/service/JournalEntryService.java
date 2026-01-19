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

    //getEntryByID() - given a valid identifier, returns a JournalEntry instance. Only returns if the entry is editable.
    //inputs - id: an Integer representing the journal entry identifier.
    //outputs - a JournalEntry instance matching the passed id, or throws an exception otherwise.
    public JournalEntry getEntryByID(Integer id)
    {
        JournalEntry entry = journalEntryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Journal Entry not found"));

        if (entry.getIsEditable() == false)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This entry is locked and cannot be changed");
        }

        return entry;
    }

    //removeEntryByID() - given a valid identifier, calls the JournalEntry repository to remove a journal entry. Only removes if the entry is editable.
    //inputs - id: an Integer representing the journal entry identifier.
    //outputs - none; updates the repository if successful, or throws an exception otherwise.
    public void removeEntryByID(Integer id)
    {
        JournalEntry entry = journalEntryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Journal Entry not found"));

        if(entry.getIsEditable() == false || "Posted".equals(entry.getStatus()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This entry is locked and cannot be removed");
        }

        journalEntryRepository.deleteById(id);
    }

    //getAllUnpostedEntries() - fetches all JournalEntry data in the JournalEntry repository that do not have "Posted" status. 
    //inputs - none.
    //output - a list of JournalEntry instances that meet the criteria.
    public List<JournalEntry> getAllUnpostedEntries()
    {
        return journalEntryRepository.findByStatusNot("Posted");
    }

    //getEntriesByIDList() - given a list containing identifiers, fetches matching JournalEntry data in the JournalEntry repository and converts into
        //a map. This is primarily used as output by the API.
    //inputs - idList: a list of Integers representing identifiers for one or more JournalEntry.
    //output - a map with the passed values, the identifier being the key.
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

    //getJournalEntriesForAccountDetail() - queries the JournalEntry repository to find journal entries that meet the account detail paramters. This
        //method is called for every account included in the account detail.
    //inputs -
        //startDate: a LocalDate representing the starting date for journal entries to include.
        //endDate: a LocalDate representing the ending date for journal entries to include.
        //generatedDate: a LocalDate representing the cutoff for when a journal entry was generated.
        //accountCode: a BigDecimal representing the account code to match for.
    //output - a list containing objects (JournalEntry and JournalEntryLine).
    public List<Object[]> getJournalEntriesForAccountDetail(LocalDate startDate, LocalDate endDate, LocalDate generatedDate, BigDecimal accountCode)
    {
        return journalEntryRepository.findForAccountDetail(startDate, endDate, generatedDate, accountCode);
    }

    //saveAndSubmit() - takes a JournalEntry instance, validates the amounts, then saves into the JournalEntry repository. Also saves associated
        //JournalEntryLine instances to the JournalEntryLine repository.
    //inputs - entry: a JournalEntry instance to save into the repository.
    //output - the JournalEntry instance, regardless of result.
    @Transactional
    public JournalEntry saveAndSubmit(JournalEntry entry)
    {
        //validate the lines, throw exception if fails to balance
        if(journalLogic.isBalanced(entry.getLines()) == false)
        {
            throw new IllegalArgumentException("Journal Entry does not balance!");
        }

        //update the journal entry for "Review", then save to DB
        entry.setStatus("Review");
        journalEntryRepository.save(entry);

        return entry;
    }

    //postJournalEntry() - takes a JournalEntry instance, validates for completeness, then updates the JournalEntry repository data.
    //inputs - entry: a JournalEntry instance to update to "Posted" status.
    //output - the JournalEntry instance, regardless of result.
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
