package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.time.Year;
import java.util.SortedMap;
import java.util.TreeMap;

public class AccountDetailBlock implements FinancialBlock
{
    private SortedMap<Year, AccountDetailBlockYear> yearBlocks;

    private BigDecimal totalAccountDebits;
    private BigDecimal totalAccountCredits;
    private BigDecimal totalAccountAmount;

    private String totalAccountType;

    //
    // Initializer(s)
    //

    public AccountDetailBlock()
    {
        this.yearBlocks = new TreeMap<>();

        this.totalAccountDebits = BigDecimal.ZERO;
        this.totalAccountCredits = BigDecimal.ZERO;
        this.totalAccountAmount = BigDecimal.ZERO;

        this.totalAccountType = null;
    }

    //
    // Getters & Setters
    //

    public SortedMap<Year, AccountDetailBlockYear> getYearBlocks()
    {
        return this.yearBlocks;
    }

    public void setYearBlocks(SortedMap<Year, AccountDetailBlockYear> yearBlocks)
    {
        this.yearBlocks = yearBlocks;
    }

    @Override
    public BigDecimal getTotalDebits()
    {
        return this.totalAccountDebits;
    }

    public void setTotalDebits(BigDecimal totalDebits)
    {
        this.totalAccountDebits = totalDebits;
    }

    @Override
    public BigDecimal getTotalCredits()
    {
        return this.totalAccountCredits;
    }

    public void setTotalCredits(BigDecimal totalCredits)
    {
        this.totalAccountCredits = totalCredits;
    }

    @Override
    public BigDecimal getGrandTotal()
    {
        return this.totalAccountAmount;
    }

    @Override
    public void setGrandTotal(BigDecimal grandTotal)
    {
        this.totalAccountAmount = grandTotal;
    }

    public String getGrandTotalType()
    {
        return this.totalAccountType;
    }

    @Override
    public void setGrandTotalType(String grandTotalType)
    {
        this.totalAccountType = grandTotalType;
    }
}
