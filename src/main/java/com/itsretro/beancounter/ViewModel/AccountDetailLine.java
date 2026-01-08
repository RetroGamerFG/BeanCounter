package com.itsretro.beancounter.ViewModel;

import java.util.ArrayList;
import java.util.List;

import com.itsretro.beancounter.Model.Account;
import com.itsretro.beancounter.Model.JournalEntry;
import com.itsretro.beancounter.Model.JournalEntryLine;

public class AccountDetailLine 
{
    public class EntryGroup
    {
        private JournalEntry journalEntry;
        private List<JournalEntryLine> journalEntryLines;

        //
        // Sub-Class Initializer
        //

        public EntryGroup()
        {
            this.journalEntry = null;
            this.journalEntryLines = new ArrayList<>();
        }

        //
        // Sub-Class Getter & Setter
        //

        public JournalEntry getJournalEntry()
        {
            return this.journalEntry;
        }

        public void setJournalEntry(JournalEntry journalEntry)
        {
            this.journalEntry = journalEntry;
        }

        public List<JournalEntryLine> getJournalEntryLines()
        {
            return this.journalEntryLines;
        }

        public void setJournalEntryLines(List<JournalEntryLine> journalEntryLines)
        {
            this.journalEntryLines = journalEntryLines;
        }

        //
        // Additional Sub-Class Functions
        //

        public void insertJournalEntryLine(JournalEntryLine journalEntryLine)
        {
            this.journalEntryLines.add(journalEntryLine);
        }

        public JournalEntryLine getSingleJournalEntryLine(int index)
        {
            try
            {
                return this.journalEntryLines.get(index);
            }
            catch (IndexOutOfBoundsException e)
            {
                return null;
            }
        }
    }

    //
    // Class Members
    //

    private Account account;
    private List<EntryGroup> associatedEntries;

    //
    // Initializer
    //

    public AccountDetailLine(Account account)
    {
        this.account = account;
        this.associatedEntries = new ArrayList<>();
    }

    //
    // Getters & Setters
    //

    public Account getAccount()
    {
        return this.account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public List<EntryGroup> getAssociatedEntries()
    {
        return this.associatedEntries;
    }

    public void setAssociatedEntries(List<EntryGroup> associatedEntries)
    {
        this.associatedEntries = associatedEntries;
    }

    //
    // Additional Functions
    //

    public void createEntryGroup(JournalEntry journalEntry, List<JournalEntryLine> journalEntryLines)
    {
        EntryGroup entryGroup = new EntryGroup();

        entryGroup.setJournalEntry(journalEntry);
        entryGroup.setJournalEntryLines(journalEntryLines);

        this.associatedEntries.add(entryGroup);
    }
}
