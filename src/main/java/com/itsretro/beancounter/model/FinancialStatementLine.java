package com.itsretro.beancounter.model;

import java.math.BigDecimal;

public class FinancialStatementLine 
{
    private boolean isCredit;

    private BigDecimal totalAmountMTD;
    private BigDecimal totalAmountQTD;
    private BigDecimal totalAmountYTD;

    //
    // Initializer(s)
    //

    public FinancialStatementLine()
    {
        this.totalAmountMTD = BigDecimal.ZERO;
        this.totalAmountQTD = BigDecimal.ZERO;
        this.totalAmountYTD = BigDecimal.ZERO;
    }

    //
    // Getters & Setters
    //

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
