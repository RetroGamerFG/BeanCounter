//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// FinancialStatementBlock: a model used to display account totals that match filter criteria for a financial statement.
//   Used by various views such as IncomeStatementView. A block can represent a month, quarter, or year based on passed
//   instances of FinancialStatementLine.
//

package com.itsretro.beancounter.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FinancialStatementColumn 
{
    private String columnLabel; //should be a month, quarter, or the year
    private String columnRange; //mtd, qtd, or ytd

    private List<FinancialStatementLine> lines;

    private BigDecimal totalAmount;

    //
    // Initializer(s)
    //

    public FinancialStatementColumn()
    {   
        this.columnLabel = null;
        this.columnRange = null;

        this.lines = new ArrayList<>();

        this.totalAmount = BigDecimal.ZERO;
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

    public String getColumnRange()
    {
        return this.columnRange;
    }

    public void setColumnRange(String columnRange)
    {
        this.columnRange = columnRange;
    }

    public List<FinancialStatementLine> getLines()
    {
        return this.lines;
    }

    public void setLines(List<FinancialStatementLine> lines)
    {
        this.lines = lines;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }
}
