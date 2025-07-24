//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// Transaction -  an alias for journal entry. Holds information for when an entry is prepared by the user, including an identifier, creation and post date, 
// the total amount, a description, and it's post status. Also includes an array of "TransactionEntry", which includes accounts included in the entry.


package Ledger.Transactions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Transaction implements Serializable
{
    private int transactionNumber;
    private Date transactionDate;
    private LocalDate postDate;
    private String postStatus;
    private String formattedTotal;
    private String transactionDescription;
    private String postSignature;

    private ArrayList<TransactionEntry> transactionEntries;

    private BigDecimal debits;
    private BigDecimal credits;
    private BigDecimal total;
    private BigDecimal variance;

    public Transaction()
    {
        transactionNumber = -1;
        transactionDate = new Date();
        postDate = null;
        postStatus = null;
        transactionEntries = new ArrayList<>();
        formattedTotal = "$0.00";
        transactionDescription = null;
        postSignature = null;
    }

    public int getTransactionNumber()
    {
        return transactionNumber;
    }

    public void setTransactionNumber(int transactionNumber)
    {
        this.transactionNumber = transactionNumber;
    }

    public Date getTransactionDate()
    {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate)
    {
        this.transactionDate = transactionDate;
    }

    public LocalDate getPostDate()
    {
        return postDate;
    }

    public void setPostDate(LocalDate postDate)
    {
        this.postDate = postDate;
    }

    public String getPostStatus()
    {
        return postStatus;
    }

    public void setPostStatus(String postStatus)
    {
        this.postStatus = postStatus;
    }

    public String getTransactionDescription()
    {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription)
    {
        this.transactionDescription = transactionDescription;
    }

    public String getPostSignature()
    {
        return postSignature;
    }

    public void setPostSignature(String postSignature)
    {
        this.postSignature = postSignature;
    }

    public ArrayList<TransactionEntry> getTransactionEntries()
    {
        return this.transactionEntries;
    }

    //addTransactionEntry() - adds a passed "TransactionEntry" object into the ArrayList member
    public void addTransactionEntry(TransactionEntry newEntry)
    {
        transactionEntries.add(newEntry);
    }

    //removeTransactionEntry() - removes a passed "TransactionEntry" object into the ArrayList member.
    public void removeTransactionEntry(TransactionEntry removeEntry)
    {
        transactionEntries.remove(removeEntry);
    }

    //getTotalAmount() - returns total formatted as a String.
    public String getTotalAmount()
    {
        return this.total.toString();
    }

    public void setFormattedTotal()
    {
        formattedTotal = "$" + getTotalAmount().toString();
    }

    public String getFormattedTotal()
    {
        return this.formattedTotal;
    }

    //validateDifference() - checks the difference between debits and credits is zero, returns boolean based on result.
    public boolean validateDifference()
    {
        variance = new BigDecimal(0);
        total = new BigDecimal(0);

        for(TransactionEntry current : transactionEntries)
        {
            //if no amounts are detected, return false
            if(current.getDebitAmount() == null && current.getCreditAmount() == null)
            {
                return false;
            }

            debits = new BigDecimal(0);
            credits = new BigDecimal(0);

            if(current.getDebitAmount() != null)
            {
                debits = debits.add(current.getDebitAmount());
            }
            
            if(current.getCreditAmount() != null)
            {
                credits = credits.add(current.getCreditAmount());
            }

            total = total.add(debits);
            variance = variance.add(debits);
            variance = variance.subtract(credits);

            setFormattedTotal();
        }

        //if variance exists, return false, else return true
        if(variance.compareTo(new BigDecimal(0)) != 0)
        {
            return false;
        }

        return true;
    }
}
