//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetailView: a ViewModel used to process and store initialized instances of 'AccountDetailLine'. Creates a form that
//  displays journal entries in blocks, including by year and by month.
//  
//  Includes the following models to create: AccountDetailBlockYear, AccountDetailBlockMonth, AccountDetailLine
//

package com.itsretro.beancounter.viewmodel;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import com.itsretro.beancounter.model.Account;
import com.itsretro.beancounter.model.AccountDetailBlock;
import com.itsretro.beancounter.model.FinancialBlock;

public class AccountDetailView implements FinancialBlock
{
    private SortedMap<Account, AccountDetailBlock> accountDetailBlocks;
    
    private BigDecimal totalReportDebits;
    private BigDecimal totalReportCredits;
    private BigDecimal totalReportAmount;

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

        this.totalReportDebits = BigDecimal.ZERO;
        this.totalReportCredits = BigDecimal.ZERO;
        this.totalReportAmount = BigDecimal.ZERO;

        this.grandTotalType = null;

        this.dateString = null;
    }

    //
    // Getters & Setters
    //

    public SortedMap<Account, AccountDetailBlock> getAccountDetailBlocks()
    {
        return this.accountDetailBlocks;
    }

    public void setAccountDetailBlocks(SortedMap<Account, AccountDetailBlock> accountDetailBlocks)
    {
        this.accountDetailBlocks = accountDetailBlocks;
    }

    @Override
    public BigDecimal getTotalDebits()
    {
        return this.totalReportDebits;
    }

    public void setTotalDebits(BigDecimal totalDebits)
    {
        this.totalReportDebits = totalDebits;
    }

    @Override
    public BigDecimal getTotalCredits()
    {
        return this.totalReportCredits;
    }

    public void setTotalCredits(BigDecimal totalCredits)
    {
        this.totalReportCredits = totalCredits;
    }

    @Override
    public BigDecimal getGrandTotal()
    {
        return this.totalReportAmount;
    }

    @Override
    public void setGrandTotal(BigDecimal grandTotal)
    {
        this.totalReportAmount = grandTotal;
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
