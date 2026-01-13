//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetaiForm: a ViewModel used to process and store initialized instances of 'AccountDetailLine'. Creates a form that
// displays journal entries in blocks, including by year and by month.
//     EntryGroup: a subclass which holds a journal entry and its filtered journal entry lines that match the account.
//

package com.itsretro.beancounter.viewmodel;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import com.itsretro.beancounter.model.Account;

public class AccountDetailView 
{
    //block structure: account details should be structured by year, then by month.
    //private Set<AccountDetailBlockYear> accountDetailBlocks;
    private Map<Account, Map<Year, AccountDetailBlockYear>> accountDetailBlocks;
    
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private BigDecimal grandTotal;
    
    //
    // Initializer(s)
    //

    public AccountDetailView()
    {
        this.accountDetailBlocks = new HashMap<>();

        this.totalDebits = BigDecimal.ZERO;
        this.totalCredits = BigDecimal.ZERO;
        this.grandTotal = BigDecimal.ZERO;
    }

    //
    // Getters & Setters
    //

    public Map<Account, Map<Year, AccountDetailBlockYear>> getAccountDetailBlocks()
    {
        return this.accountDetailBlocks;
    }

    public void setAccountDetailBlocks(Map<Account, Map<Year, AccountDetailBlockYear>> accountDetailBlocks)
    {
        this.accountDetailBlocks = accountDetailBlocks;
    }

    public BigDecimal getTotalDebits()
    {
        return this.totalDebits;
    }

    public void setTotalDebits(BigDecimal totalDebits)
    {
        this.totalDebits = totalDebits;
    }

    public BigDecimal getTotalCredits()
    {
        return this.totalCredits;
    }

    public void setTotalCredits(BigDecimal totalCredits)
    {
        this.totalCredits = totalCredits;
    }

    public BigDecimal getGrandTotal()
    {
        return this.grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal)
    {
        this.grandTotal = grandTotal;
    }
}
