package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class IncomeStatementColumn 
{
    private String columnLabel;
    private int columnIndex;
    
    private LinkedHashMap<String, FinancialStatementLine> revenueLines;
    private LinkedHashMap<String, FinancialStatementLine> expenseLines;

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

        this.revenueLines = new LinkedHashMap<>();
        this.expenseLines = new LinkedHashMap<>();

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

    public LinkedHashMap<String, FinancialStatementLine> getRevenueLines()
    {
        return this.revenueLines;
    }

    public void setRevenueLines(LinkedHashMap<String, FinancialStatementLine> revenueLines)
    {
        this.revenueLines = revenueLines;
    }

    public LinkedHashMap<String, FinancialStatementLine> getExpenseLines()
    {
        return this.expenseLines;
    }

    public void setExpenseLines(LinkedHashMap<String, FinancialStatementLine> expenseLines)
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
