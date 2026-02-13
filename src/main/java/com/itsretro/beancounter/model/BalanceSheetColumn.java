//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// BalanceSheetColumn: a model used for columns used by the BalanceSheetView. Includes separated maps for assets, liabilities, and equity accounts, and
//  additional separation by current and long-term status.
//

package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class BalanceSheetColumn 
{
    private String columnLabel;
    private int columnIndex;
    
    private LinkedHashMap<String, FinancialStatementLine> currentAssetLines;
    private LinkedHashMap<String, FinancialStatementLine> longTermAssetLines;

    private LinkedHashMap<String, FinancialStatementLine> currentLiabilityLines;
    private LinkedHashMap<String, FinancialStatementLine> longTermLiabilityLines;
    private LinkedHashMap<String, FinancialStatementLine> equityLines;

    private BigDecimal totalCurrentAssets;
    private BigDecimal totalLongTermAssets;

    private BigDecimal totalCurrentLiability;
    private BigDecimal totalLongTermLiability;
    private BigDecimal totalEquity;

    private BigDecimal totalAssets;
    private BigDecimal totalLiabilityAndEquity;

    //
    // Initializer(s)
    //

    public BalanceSheetColumn()
    {
        this.columnLabel = null;
        this.columnIndex = -1;

        this.currentAssetLines = new LinkedHashMap<>();
        this.longTermAssetLines = new LinkedHashMap<>();
        this.currentLiabilityLines = new LinkedHashMap<>();
        this.longTermLiabilityLines = new LinkedHashMap<>();
        this.equityLines = new LinkedHashMap<>();

        this.totalCurrentAssets = BigDecimal.ZERO;
        this.totalLongTermAssets = BigDecimal.ZERO;
        this.totalCurrentLiability = BigDecimal.ZERO;
        this.totalLongTermLiability = BigDecimal.ZERO;
        this.totalEquity = BigDecimal.ZERO;
        this.totalAssets = BigDecimal.ZERO;
        this.totalLiabilityAndEquity = BigDecimal.ZERO;
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

    public LinkedHashMap<String, FinancialStatementLine> getCurrentAssetLines()
    {
        return this.currentAssetLines;
    }

    public void setCurrentAssetLines(LinkedHashMap<String, FinancialStatementLine> currentAssetLines)
    {
        this.currentAssetLines = currentAssetLines;
    }

    public LinkedHashMap<String, FinancialStatementLine> getLongTermAssetLines()
    {
        return this.longTermAssetLines;
    }

    public void setLongTermAssetLines(LinkedHashMap<String, FinancialStatementLine> longTermAssetLines)
    {
        this.longTermAssetLines = longTermAssetLines;
    }

    public LinkedHashMap<String, FinancialStatementLine> getCurrentLiabilityLines()
    {
        return this.currentLiabilityLines;
    }

    public void setCurrentLiabilityLines(LinkedHashMap<String, FinancialStatementLine> currentLiabilityLines)
    {
        this.currentLiabilityLines = currentLiabilityLines;
    }

    public LinkedHashMap<String, FinancialStatementLine> getLongTermLiabilityLines()
    {
        return this.longTermLiabilityLines;
    }

    public void setLongTermLiabilityLines(LinkedHashMap<String, FinancialStatementLine> longTermLiabilityLines)
    {
        this.longTermLiabilityLines = longTermLiabilityLines;
    }

    public LinkedHashMap<String, FinancialStatementLine> getEquityLines()
    {
        return this.equityLines;
    }

    public void setEquityLines(LinkedHashMap<String, FinancialStatementLine> equityLines)
    {
        this.equityLines = equityLines;
    }

    public BigDecimal getTotalCurrentAssets()
    {
        return this.totalCurrentAssets;
    }

    public void setTotalCurrentAssets(BigDecimal totalCurrentAssets)
    {
        this.totalCurrentAssets = totalCurrentAssets;
    }

    public BigDecimal getTotalLongTermAssets()
    {
        return this.totalLongTermAssets;
    }

    public void setTotalLongTermAssets(BigDecimal totalLongTermAssets)
    {
        this.totalLongTermAssets = totalLongTermAssets;
    }

    public BigDecimal getTotalCurrentLiability()
    {
        return this.totalCurrentLiability;
    }

    public void setTotalCurrentLiability(BigDecimal totalCurrentLiability)
    {
        this.totalCurrentLiability = totalCurrentLiability;
    }

    public BigDecimal getTotalLongTermLiability()
    {
        return this.totalLongTermLiability;
    }

    public void setTotalLongTermLiability(BigDecimal totalLongTermLiability)
    {
        this.totalLongTermLiability = totalLongTermLiability;
    }

    public BigDecimal getTotalEquity()
    {
        return this.totalEquity;
    }

    public void setTotalEquity(BigDecimal totalEquity)
    {
        this.totalEquity = totalEquity;
    }

    public BigDecimal getTotalAssets()
    {
        return this.totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets)
    {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getTotalLiabilityAndEquity()
    {
        return this.totalLiabilityAndEquity;
    }

    public void setTotalLiabilityAndEquity(BigDecimal totalLiabilityAndEquity)
    {
        this.totalLiabilityAndEquity = totalLiabilityAndEquity;
    }
}
