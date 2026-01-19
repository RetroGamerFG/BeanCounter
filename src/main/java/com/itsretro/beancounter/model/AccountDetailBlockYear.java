//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetailBlockYear: a model used to store AccountDetailLine instances for a given year.
//   Used to create AccountDetailView.
//

package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.time.Month;
import java.util.EnumMap;
import java.util.Map;

public class AccountDetailBlockYear implements FinancialBlock //n for the amount of passed AccountDetailLine
{
    private Map<Month, AccountDetailBlockMonth> monthBlocks;

    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private BigDecimal grandTotal;

    private String grandTotalType;

    public AccountDetailBlockYear()
    {
        this.monthBlocks = new EnumMap<>(Month.class);

        this.totalDebits = new BigDecimal(0);
        this.totalCredits = new BigDecimal(0);
        this.grandTotal = new BigDecimal(0);

        this.grandTotalType = null;
    }

    //
    // Getters & Setters
    //

    public Map<Month, AccountDetailBlockMonth> getMonthBlocks()
    {
        return this.monthBlocks;
    }

    public void setMonthBlocks(Map<Month, AccountDetailBlockMonth> monthBlocks)
    {
        this.monthBlocks = monthBlocks;
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
}
