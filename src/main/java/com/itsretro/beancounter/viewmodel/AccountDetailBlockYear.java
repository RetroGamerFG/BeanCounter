package com.itsretro.beancounter.viewmodel;

import java.math.BigDecimal;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class AccountDetailBlockYear //n for the amount of passed AccountDetailLine
{
    private Map<Month, AccountDetailBlockMonth> monthBlocks;

    private BigDecimal totalDebits;
    private BigDecimal totalCredits;
    private BigDecimal grandTotal;

    public AccountDetailBlockYear()
    {
        this.monthBlocks = new HashMap<>();

        this.totalDebits = new BigDecimal(0);
        this.totalCredits = new BigDecimal(0);
        this.grandTotal = new BigDecimal(0);
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

    //calculateTotals() - calls each AccountDetailBlockMonth's calculate method, then adds the fetched totals to this instance's totals.
    //inputs - none.
    //output - none; updates the instance's values.
    public void calculateTotals()
    {
        //iterate through each AccountDetailLine to increment total amounts
        for(AccountDetailBlockMonth adbm : this.monthBlocks.values())
        {
            adbm.calculateTotals(); //call calculateTotals() to update values

            this.totalDebits = this.totalDebits.add(adbm.getTotalDebits());
            this.totalCredits = this.totalCredits.add(adbm.getTotalCredits());
            this.grandTotal = this.grandTotal.add(adbm.getGrandTotal());
        }
    }
}
