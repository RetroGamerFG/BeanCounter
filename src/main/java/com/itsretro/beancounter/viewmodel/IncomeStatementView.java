package com.itsretro.beancounter.viewmodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.itsretro.beancounter.model.FinancialStatementBlock;

public class IncomeStatementView 
{
    private List<FinancialStatementBlock> revenueItems; //blocks hold MTD, QTD, and YTD totals for accountType
    private List<FinancialStatementBlock> expenseItems;

    private BigDecimal grandTotalMTD;
    private BigDecimal grandTotalQTD;
    private BigDecimal grandTotalYTD;

    private boolean includeMTD;
    private boolean includeQTD;
    private boolean includeYTD;

    //
    // Initializer(s)
    //

    public IncomeStatementView()
    {
        this.revenueItems = new ArrayList<>();
        this.expenseItems = new ArrayList<>();

        this.grandTotalMTD = BigDecimal.ZERO;
        this.grandTotalQTD = BigDecimal.ZERO;
        this.grandTotalYTD = BigDecimal.ZERO;

        this.includeMTD = false;
        this.includeQTD = false;
        this.includeYTD = false;
    }

    //
    // Getters & Setters
    //

    public List<FinancialStatementBlock> getRevenueItems()
    {
        return revenueItems;
    }

    public void setRevenueItems(List<FinancialStatementBlock> revenueItems)
    {
        this.revenueItems = revenueItems;
    }

    public List<FinancialStatementBlock> getExpenseItems()
    {
        return expenseItems;
    }

    public void setExpenseItems(List<FinancialStatementBlock> expenseItems)
    {
        this.expenseItems = expenseItems;
    }

    public BigDecimal getGrandTotalMTD()
    {
        return grandTotalMTD;
    }

    public void setGrandTotalMTD(BigDecimal grandTotalMTD)
    {
        this.grandTotalMTD = grandTotalMTD;
    }

    public BigDecimal getGrandTotalQTD()
    {
        return grandTotalQTD;
    }

    public void setGrandTotalQTD(BigDecimal grandTotalQTD)
    {
        this.grandTotalQTD = grandTotalQTD;
    }

    public BigDecimal getGrandTotalYTD()
    {
        return grandTotalYTD;
    }

    public void setGrandTotalYTD(BigDecimal grandTotalYTD)
    {
        this.grandTotalYTD = grandTotalYTD;
    }

    public boolean getIncludeMTD()
    {
        return includeMTD;
    }

    public void setIncludeMTD(boolean includeMTD)
    {
        this.includeMTD = includeMTD;
    }

    public boolean getIncludeQTD()
    {
        return includeQTD;
    }

    public void setIncludeQTD(boolean includeQTD)
    {
        this.includeQTD = includeQTD;
    }

    public boolean getIncludeYTD()
    {
        return includeYTD;
    }

    public void setIncludeYTD(boolean includeYTD)
    {
        this.includeYTD = includeYTD;
    }
}
