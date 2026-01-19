//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetailView: a ViewModel used to process and store initialized instances of 'AccountDetailLine'. Creates a form that
// displays journal entries in blocks, including by year and by month.
//   Includes the following models to create: AccountDetailBlockYear, AccountDetailBlockMonth, AccountDetailLine
//

package com.itsretro.beancounter.viewmodel;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import com.itsretro.beancounter.model.Account;
import com.itsretro.beancounter.model.AccountDetailBlockYear;
import com.itsretro.beancounter.model.FinancialBlock;

public class AccountDetailView implements FinancialBlock
{
    private SortedMap<Account, SortedMap<Year, AccountDetailBlockYear>> accountDetailBlocks;
    
    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private BigDecimal grandTotal;

    private String grandTotalType;

    private String dateString;
    
    //
    // Initializer(s)
    //

    public AccountDetailView()
    {
        this.accountDetailBlocks = new TreeMap<>(
            Comparator.comparing(Account::getAccountCode)
        );

        this.totalDebits = BigDecimal.ZERO;
        this.totalCredits = BigDecimal.ZERO;
        this.grandTotal = BigDecimal.ZERO;

        this.grandTotalType = null;

        this.dateString = null;
    }

    //
    // Getters & Setters
    //

    public SortedMap<Account, SortedMap<Year, AccountDetailBlockYear>> getAccountDetailBlocks()
    {
        return this.accountDetailBlocks;
    }

    public void setAccountDetailBlocks(SortedMap<Account, SortedMap<Year, AccountDetailBlockYear>> accountDetailBlocks)
    {
        this.accountDetailBlocks = accountDetailBlocks;
    }

    @Override
    public BigDecimal getTotalDebits()
    {
        return this.totalDebits;
    }

    public void setTotalDebits(BigDecimal totalDebits)
    {
        this.totalDebits = totalDebits;
    }

    @Override
    public BigDecimal getTotalCredits()
    {
        return this.totalCredits;
    }

    public void setTotalCredits(BigDecimal totalCredits)
    {
        this.totalCredits = totalCredits;
    }

    @Override
    public BigDecimal getGrandTotal()
    {
        return this.grandTotal;
    }

    @Override
    public void setGrandTotal(BigDecimal grandTotal)
    {
        this.grandTotal = grandTotal;
    }

    public String getGrandTotalType()
    {
        return this.grandTotalType;
    }

    @Override
    public void setGrandTotalType(String grandTotalType)
    {
        this.grandTotalType = grandTotalType;
    }

    public String getDateString()
    {
        return this.dateString;
    }

    public void setDateString(String dateString)
    {
        this.dateString = dateString;
    }
}
