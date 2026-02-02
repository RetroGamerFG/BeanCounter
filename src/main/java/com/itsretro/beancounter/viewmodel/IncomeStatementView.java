package com.itsretro.beancounter.viewmodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itsretro.beancounter.model.FinancialStatementColumn;

public class IncomeStatementView 
{
    private Set<String> revenueAccounts;
    private Set<String> expenseAccounts;

    private List<FinancialStatementColumn> revenueItems;
    private List<FinancialStatementColumn> expenseItems;

    private int columnCount;
    private List<BigDecimal> netIncomeByColumn;

    private String dateRangeString;

    //
    // Initializer(s)
    //

    public IncomeStatementView()
    {
        this.revenueAccounts = new HashSet<>();
        this.expenseAccounts = new HashSet<>();

        this.revenueItems = new ArrayList<>();
        this.expenseItems = new ArrayList<>();

        this.netIncomeByColumn = new ArrayList<>();

        this.dateRangeString = null;
    }

    //
    // Getters & Setters
    //

    public Set<String> getRevenueAccounts()
    {
        return this.revenueAccounts;
    }

    public void setRevenueAccounts(Set<String> revenueAccounts)
    {
        this.revenueAccounts = revenueAccounts;
    }

    public Set<String> getExpenseAccounts()
    {
        return this.expenseAccounts;
    }

    public void setExpenseAccounts(Set<String> expenseAccounts)
    {
        this.expenseAccounts = expenseAccounts;
    }

    public List<FinancialStatementColumn> getRevenueItems()
    {
        return this.revenueItems;
    }

    public void setRevenueItems(List<FinancialStatementColumn> revenueItems)
    {
        this.revenueItems = revenueItems;
    }

    public List<FinancialStatementColumn> getExpenseItems()
    {
        return this.expenseItems;
    }

    public void setExpenseItems(List<FinancialStatementColumn> expenseItems)
    {
        this.expenseItems = expenseItems;
    }

    public int getColumnCount()
    {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
    }

    public List<BigDecimal> getNetIncomeByColumn()
    {
        return this.netIncomeByColumn;
    }

    public void setNetIncomeByBlock(List<BigDecimal> netIncomeByColumn)
    {
        this.netIncomeByColumn = netIncomeByColumn;
    }

    public String getDateRangeString()
    {
        return this.dateRangeString;
    }

    public void setDateRangeString(String dateRangeString)
    {
        this.dateRangeString = dateRangeString;
    }

    //
    // Additional Methods
    //

    public void incrementColumnCount()
    {
        this.columnCount++;
    }
}
