//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// BalanceSheetView: a ViewModel used to process and store initialized instances of FinancialStatement.
//  Creates a form that displays summarized account totals in the form of a balance sheet.
//  
//  Includes the following models to create: BalanceSheetColumn, FinancialStatementLine
//

package com.itsretro.beancounter.viewmodel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.itsretro.beancounter.model.BalanceSheetColumn;

public class BalanceSheetView 
{
    private LinkedHashSet<String> currentAssetAccounts;
    private LinkedHashSet<String> longTermAssetAccounts;
    private LinkedHashSet<String> currentLiabilityAccounts;
    private LinkedHashSet<String> longTermLiabilityAccounts;
    private LinkedHashSet<String> equityAccounts;

    private List<BalanceSheetColumn> columns;
    private int columnCount;

    private String dateRangeString;

    //
    // Initializer(s)
    //

    public BalanceSheetView()
    {
        this.currentAssetAccounts = new LinkedHashSet<>();
        this.longTermAssetAccounts = new LinkedHashSet<>();
        this.currentLiabilityAccounts = new LinkedHashSet<>();
        this.longTermLiabilityAccounts = new LinkedHashSet<>();
        this.equityAccounts = new LinkedHashSet<>();

        this.columns = new ArrayList<>();
        this.columnCount = 0;

        this.dateRangeString = null;
    }

    //
    // Getters & Setters
    //

    public LinkedHashSet<String> getCurrentAssetAccounts()
    {
        return this.currentAssetAccounts;
    }

    public void setCurrentAssetAccounts(LinkedHashSet<String> currentAssetAccounts)
    {
        this.currentAssetAccounts = currentAssetAccounts;
    }

    public LinkedHashSet<String> getLongTermAssetAccounts()
    {
        return this.longTermAssetAccounts;
    }

    public void setLongTermAssetAccounts(LinkedHashSet<String> longTermAssetAccounts)
    {
        this.longTermAssetAccounts = longTermAssetAccounts;
    }

    public LinkedHashSet<String> getCurrentLiabilityAccounts()
    {
        return this.currentLiabilityAccounts;
    }

    public void setCurrentLiabilityAccounts(LinkedHashSet<String> currentLiabilityAccounts)
    {
        this.currentLiabilityAccounts = currentLiabilityAccounts;
    }

    public LinkedHashSet<String> getLongTermLiabilityAccounts()
    {
        return this.longTermLiabilityAccounts;
    }

    public void setLongTermLiabilityAccounts(LinkedHashSet<String> longTermLiabilityAccounts)
    {
        this.longTermLiabilityAccounts = longTermLiabilityAccounts;
    }

    public LinkedHashSet<String> getEquityAccounts()
    {
        return this.equityAccounts;
    }

    public void setEquityAccounts(LinkedHashSet<String> equityAccounts)
    {
        this.equityAccounts = equityAccounts;
    }

    public List<BalanceSheetColumn> getColumns()
    {
        return this.columns;
    }

    public void setColumns(List<BalanceSheetColumn> columns)
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
