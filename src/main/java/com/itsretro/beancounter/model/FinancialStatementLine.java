package com.itsretro.beancounter.model;

import java.math.BigDecimal;

public class FinancialStatementLine 
{
    private String accountName;
    private boolean isCredit;

    private BigDecimal totalAmountMTD;
    private BigDecimal totalAmountQTD;
    private BigDecimal totalAmountYTD;

    //
    // Getters & Setters
    //

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public boolean getIsCredit()
    {
        return this.isCredit;
    }

    public void setIsCredit(boolean isCredit)
    {
        this.isCredit = isCredit;
    }

    public BigDecimal getTotalAmountMTD()
    {
        return totalAmountMTD;
    }

    public void setTotalAmountMTD(BigDecimal totalAmountMTD)
    {
        this.totalAmountMTD = totalAmountMTD;
    }

    public BigDecimal getTotalAmountQTD()
    {
        return totalAmountQTD;
    }

    public void setTotalAmountQTD(BigDecimal totalAmountQTD)
    {
        this.totalAmountQTD = totalAmountQTD;
    }

    public BigDecimal getTotalAmountYTD()
    {
        return totalAmountYTD;
    }

    public void setTotalAmountYTD(BigDecimal totalAmountYTD)
    {
        this.totalAmountYTD = totalAmountYTD;
    }
}
