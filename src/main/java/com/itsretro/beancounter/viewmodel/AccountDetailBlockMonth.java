package com.itsretro.beancounter.viewmodel;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class AccountDetailBlockMonth //1 < n < 12 for a given year
{
    private Set<AccountDetailLine> accountDetailLines;

    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private BigDecimal grandTotal;

    public AccountDetailBlockMonth()
    {
        this.accountDetailLines = new HashSet<>();

        this.totalDebits = new BigDecimal(0);
        this.totalCredits = new BigDecimal(0);
        this.grandTotal = new BigDecimal(0);
    }

    //
    // Getters & Setters
    //

    public Set<AccountDetailLine> getAccountDetailLines()
    {
        return this.accountDetailLines;
    }

    public void setAccountDetailLines(Set<AccountDetailLine> accountDetailLines)
    {
        this.accountDetailLines = accountDetailLines;
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

    //
    // Additional Methods
    //

    public void insert(AccountDetailLine adl)
    {
        this.accountDetailLines.add(adl);
    }

    //calculateTotals() - calls each AccountDetailLine's calculate method, then adds the fetched totals to this instance's totals.
    //inputs - none.
    //output - none; updates the instance's values.
    public void calculateTotals()
    {
        //iterate through each AccountDetailLine to increment total amounts
        for(AccountDetailLine adl : this.accountDetailLines)
        {
            //add the values to debits and/or credits
            this.totalDebits = this.totalDebits.add(adl.getDebitAmount());
            this.totalCredits = this.totalCredits.add(adl.getCreditAmount());
        }

        //perform calculation for grand total
    }
}
