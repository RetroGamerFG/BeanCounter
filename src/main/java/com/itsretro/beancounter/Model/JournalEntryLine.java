package com.itsretro.beancounter.Model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "JournalEntryLine")
public class JournalEntryLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JournalEntryLineID")
    private Integer journalEntryLineID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;

    @Column(name = "TransactionType", length = 1)
    private String transactionType;

    @Column(name = "Amount", precision = 19, scale = 4)
    private BigDecimal amount;

    //
    // Getter & Setter Methods
    //

    public Integer getJournalEntryLineID()
    {
        return this.journalEntryLineID;
    }

    public void setJournalEntryLineID(Integer journalEntryLineID)
    {
        this.journalEntryLineID = journalEntryLineID;
    }

    public Account getAccount()
    {
        return this.account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public String getTransactionType()
    {
        return this.transactionType;
    }

    public void setTransactionType(String transactionType)
    {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}