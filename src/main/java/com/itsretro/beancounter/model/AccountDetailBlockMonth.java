//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetailBlockMonth: a model used to store AccountDetailLine instances for a given month.
//  Used by AccountDetailBlockYear to create AccountDetailView.
//

package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class AccountDetailBlockMonth implements FinancialBlock
{
    private SortedSet<AccountDetailLine> accountDetailLines;

    private BigDecimal totalDebits;
    private BigDecimal totalCredits;

    private BigDecimal grandTotal;
    private String grandTotalType;

    public AccountDetailBlockMonth()
    {
        this.accountDetailLines = new TreeSet<>(
            Comparator.comparing(AccountDetailLine::getPostDate)
        );

        this.totalDebits = new BigDecimal(0);
        this.totalCredits = new BigDecimal(0);
        this.grandTotal = new BigDecimal(0);

        this.grandTotalType = null;
    }

    //
    // Getters & Setters
    //

    public SortedSet<AccountDetailLine> getAccountDetailLines()
    {
        return this.accountDetailLines;
    }

    public void setAccountDetailLines(SortedSet<AccountDetailLine> accountDetailLines)
    {
        this.accountDetailLines = accountDetailLines;
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

    //
    // Additional Methods
    //

    public void insert(AccountDetailLine adl)
    {
        this.accountDetailLines.add(adl);
    }
}
