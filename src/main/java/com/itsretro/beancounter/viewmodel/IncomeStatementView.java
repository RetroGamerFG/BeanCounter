package com.itsretro.beancounter.viewmodel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.itsretro.beancounter.model.IncomeStatementColumn;

public class IncomeStatementView 
{
    private LinkedHashSet<String> revenueAccounts;
    private LinkedHashSet<String> expenseAccounts;

    private List<IncomeStatementColumn> columns;
    private int columnCount;

    private String dateRangeString;

    //
    // Initializer(s)
    //

    public IncomeStatementView()
    {
        this.revenueAccounts = new LinkedHashSet<>();
        this.expenseAccounts = new LinkedHashSet<>();

        this.columns = new ArrayList<>();
        this.columnCount = 0;

        this.dateRangeString = null;
    }

    //
    // Getters & Setters
    //

    public Set<String> getRevenueAccounts()
    {
        return this.revenueAccounts;
    }

    public void setRevenueAccounts(LinkedHashSet<String> revenueAccounts)
    {
        this.revenueAccounts = revenueAccounts;
    }

    public LinkedHashSet<String> getExpenseAccounts()
    {
        return this.expenseAccounts;
    }

    public void setExpenseAccounts(LinkedHashSet<String> expenseAccounts)
    {
        this.expenseAccounts = expenseAccounts;
    }

    public List<IncomeStatementColumn> getColumns()
    {
        return this.columns;
    }

    public void setColumns(List<IncomeStatementColumn> columns)
    {
        this.columns = columns;
    }

    public int getColumnCount()
    {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount)
    {
        this.columnCount = columnCount;
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
