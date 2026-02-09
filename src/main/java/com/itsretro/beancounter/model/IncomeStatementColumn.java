package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class IncomeStatementColumn 
{
    private String columnLabel;
    private int columnIndex;
    
    private Map<String, FinancialStatementLine> revenueLines;
    private Map<String, FinancialStatementLine> expenseLines;

    private BigDecimal totalRevenue;
    private BigDecimal totalExpense;
    private BigDecimal netIncome;

    //
    // Initializer(s)
    //

    public IncomeStatementColumn()
    {
        this.columnLabel = null;
        this.columnIndex = -1;

        this.revenueLines = new HashMap<>();
        this.expenseLines = new HashMap<>();

        this.totalRevenue = BigDecimal.ZERO;
        this.totalExpense = BigDecimal.ZERO;
        this.netIncome = BigDecimal.ZERO;
    }

    //
    // Getters & Setters
    //

    public String getColumnLabel()
    {
        return this.columnLabel;
    }

    public void setColumnLabel(String columnLabel)
    {
        this.columnLabel = columnLabel;
    }

    public int getColumnIndex()
    {
        return this.columnIndex;
    }

    public void setColumnIndex(int columnIndex)
    {
        this.columnIndex = columnIndex;
    }

    public Map<String, FinancialStatementLine> getRevenueLines()
    {
        return this.revenueLines;
    }

    public void setRevenueLines(Map<String, FinancialStatementLine> revenueLines)
    {
        this.revenueLines = revenueLines;
    }

    public Map<String, FinancialStatementLine> getExpenseLines()
    {
        return this.expenseLines;
    }

    public void setExpenseLines(Map<String, FinancialStatementLine> expenseLines)
    {
        this.expenseLines = expenseLines;
    }

    public BigDecimal getTotalRevenue()
    {
        return this.totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue)
    {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalExpense()
    {
        return this.totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense)
    {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetIncome()
    {
        return this.netIncome;
    }

    public void setNetIncome(BigDecimal netIncome)
    {
        this.netIncome = netIncome;
    }
}
