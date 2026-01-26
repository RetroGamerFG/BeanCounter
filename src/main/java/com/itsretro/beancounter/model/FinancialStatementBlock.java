package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FinancialStatementBlock 
{
    private Map<String, FinancialStatementLine> statementLines; //string = Account.accountName
    
    private BigDecimal totalAmountMTD;
    private BigDecimal totalAmountQTD;
    private BigDecimal totalAmountYTD;

    //
    // Initializer(s)
    //

    public FinancialStatementBlock()
    {
        this.statementLines = new HashMap<>();
        
        this.totalAmountMTD = BigDecimal.ZERO;
        this.totalAmountQTD = BigDecimal.ZERO;
        this.totalAmountYTD = BigDecimal.ZERO;
    }

    //
    // Getters & Setters
    //

    public Map<String, FinancialStatementLine> getStatementLines()
    {
        return statementLines;
    }

    public void setStatementLines(Map<String, FinancialStatementLine> statementLines)
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
