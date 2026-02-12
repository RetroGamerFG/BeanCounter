//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetailLine: a model used to display journal entries that match filter criteria for an account detail.
//  Used by AccountDetailYear and AccountDetailBlockMonth to create AccountDetailView.
//

package com.itsretro.beancounter.model;

import java.math.BigDecimal;

public class AccountDetailLine
{
    private String journalEntryID;
    private String description;
    private String postSignature;
    private String postDate;
        
    private BigDecimal amount;
    private String type;

    //
    // Getters & Setters
    //

    public String getJournalEntryID()
    {
        return this.journalEntryID;
    }

    public void setJournalEntryID(String journalEntryID)
    {
        this.journalEntryID = journalEntryID;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPostSignature()
    {
        return this.postSignature;
    }

    public void setPostSignature(String postSignature)
    {
        this.postSignature = postSignature;
    }

    public String getPostDate()
    {
        return this.postDate;
    }

    public void setPostDate(String postDate)
    {
        this.postDate = postDate;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    //
    // Additional Methods
    //

    public BigDecimal getDebitAmount()
    {
        if("D".equalsIgnoreCase(this.type))
        {
            return this.amount;
        }
        else
        {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getCreditAmount()
    {
        if("C".equalsIgnoreCase(this.type))
        {
            return this.amount;
        }
        else
        {
            return BigDecimal.ZERO;
        }
    }
}
