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

    @Column(name = "DebitAmount", precision = 19, scale = 4, nullable = true)
    private BigDecimal debitAmount;

    @Column(name = "CreditAmount", precision = 19, scale = 4, nullable = true)
    private BigDecimal creditAmount;

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

    public BigDecimal getDebitAmount()
    {
        return this.debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount)
    {
        this.debitAmount = debitAmount;

        if(debitAmount != null)
        {
            this.setTransactionType("D");
        }
    }

    public BigDecimal getcreditAmount()
    {
        return this.creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount)
    {
        this.creditAmount = creditAmount;

        if(creditAmount != null)
        {
            this.setTransactionType("C");
        }
    }

    //
    // Additional Methods
    //

    public boolean validJournalEntryLines()
    {
        return false;
    }
}