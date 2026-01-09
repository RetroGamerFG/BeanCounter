package com.itsretro.beancounter.ViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.itsretro.beancounter.Model.Account;
import com.itsretro.beancounter.Model.JournalEntry;
import com.itsretro.beancounter.Model.JournalEntryLine;

public class AccountDetailLine 
{
    public class EntryGroup
    {
        private JournalEntry journalEntry; //holds the matched entry for metadata
        private List<JournalEntryLine> journalEntryLines; //holds extracted lines that match account

        //
        // Sub-Class Initializer
        //

        public EntryGroup()
        {
            this.journalEntry = null;
            this.journalEntryLines = null;
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

        public BigDecimal getDebitAmounts()
        {
            BigDecimal result = new BigDecimal(0);

            for(JournalEntryLine jel : journalEntryLines)
            {
                if(jel.getTransactionType().compareTo("D") == 0)
                {
                    result = result.add(jel.getDebitAmount());
                }
            }

            return result;
        }

        public BigDecimal getCreditAmounts()
        {
            BigDecimal result = new BigDecimal(0);

            for(JournalEntryLine jel : journalEntryLines)
            {
                if(jel.getTransactionType().compareTo("C") == 0)
                {
                    result = result.add(jel.getCreditAmount());
                }
            }

            return result;
        }
    }

    //
    // Class Members
    //

    private Account account;
    private List<EntryGroup> associatedEntries;

    private BigDecimal debitTotal;
    private BigDecimal creditTotal;

    private BigDecimal grandTotal;
    private String grandTotalType;

    //
    // Initializer
    //

    public AccountDetailLine(Account account)
    {
        this.account = account;
        this.associatedEntries = new ArrayList<>();
        this.debitTotal = null;
        this.creditTotal = null;
        this.grandTotal = null;
        this.grandTotalType = null;
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

    public boolean getIsEmpty()
    {
        return this.associatedEntries.isEmpty();
    }

    public BigDecimal getDebitTotal()
    {
        return this.debitTotal;
    }

    public void setDebitTotal(BigDecimal debitTotal)
    {
        this.debitTotal = debitTotal;
    }

    public BigDecimal getCreditTotal()
    {
        return this.creditTotal;
    }

    public void setCreditTotal(BigDecimal creditTotal)
    {
        this.creditTotal = creditTotal;
    }

    public BigDecimal getGrandTotal()
    {

        return this.grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal)
    {
        this.grandTotal = grandTotal;
    }

    public String getGrandTotalType()
    {
        return this.grandTotalType;
    }

    public void setGrandTotalType(String grandTotalType)
    {
        this.grandTotalType = grandTotalType;
    }

    //
    // Additional Functions
    //

    public void createEntryGroup(JournalEntry journalEntry, List<JournalEntryLine> matchedLines)
    {
        EntryGroup entryGroup = new EntryGroup();

        entryGroup.setJournalEntry(journalEntry);
        entryGroup.setJournalEntryLines(matchedLines);

        this.associatedEntries.add(entryGroup);
    }

    public void calculateGrandTotal()
    {
        BigDecimal calcDebitTotal = new BigDecimal(0);
        BigDecimal calcCreditTotal = new BigDecimal(0);

        for(EntryGroup eg : getAssociatedEntries())
        {
            calcDebitTotal = calcDebitTotal.add(eg.getDebitAmounts());
            calcCreditTotal = calcCreditTotal.add(eg.getCreditAmounts());
        }

        setDebitTotal(calcDebitTotal);
        setCreditTotal(calcCreditTotal);

        if(calcDebitTotal.compareTo(calcCreditTotal) > 0)
        {
            setGrandTotal(calcDebitTotal.subtract(calcCreditTotal));
            setGrandTotalType("D");
        }
        else
        {
            setGrandTotal(calcCreditTotal.subtract(calcDebitTotal));
            setGrandTotalType("C");
        }
    }
}
