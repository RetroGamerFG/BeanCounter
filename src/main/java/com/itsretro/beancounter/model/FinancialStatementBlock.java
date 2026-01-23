package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.util.Map;

public class FinancialStatementBlock 
{
    public enum accountType 
    {
        REVENUE,
        EXPENSE;
    }

    private Map<accountType, FinancialStatementLine> statementLines;
    
    private BigDecimal totalAmountMTD;
    private BigDecimal totalAmountQTD;
    private BigDecimal totalAmountYTD;

    //
    // Getters & Setters
    //

    public Map<accountType, FinancialStatementLine> getStatementLines()
    {
        return statementLines;
    }

    public void setStatementLines(Map<accountType, FinancialStatementLine> statementLines)
    {
        this.statementLines = statementLines;
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
