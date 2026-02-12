//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// FinancialStatement: a JPA model used to store information for financial statements.
//

package com.itsretro.beancounter.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FinancialStatement")
public class FinancialStatement 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FinancialStatementID")
    private Long financialStatementID;

    @Column(name = "StatementType", length = 2) //(IS), (BS), (RE), (FA)
    private String statementType;

    @Column(name = "GeneratedDate")
    private LocalDate generatedDate;

    @Column(name = "StartingDate")
    private LocalDate startingDate;

    @Column(name = "RangeType", length = 3)
    private String rangeType;

    @Column(name = "IncludeAllMonths", nullable = true) //for a multi-month statement
    private boolean includeAllMonths;

    @Column(name = "IncludeAllQuarters", nullable = true) //for a multi-quarter statement
    private boolean includeAllQuarters;

    @Column(name = "IncludePreviousMonth", nullable = true) //to include the month before the startingDate
    private boolean includePreviousMonth;

    @Column(name = "IncludePreviousQuarter", nullable = true) //to include the quarter before the startingDate
    private boolean includePreviousQuarter;

    @Column(name = "IncludePreviousYear", nullable = true) //to include the year before the startingDate
    private boolean includePreviousYear;

    //
    // Getters & Setters
    //

    public Long getFinancialStatementID()
    {
        return financialStatementID;
    }

    public void setFinancialStatementID(Long financialStatementID)
    {
        this.financialStatementID = financialStatementID;
    }

    public String getStatementType()
    {
        return statementType;
    }

    public void setStatementType(String statementType)
    {
        this.statementType = statementType;
    }

    public LocalDate getGeneratedDate()
    {
        return this.generatedDate;
    }

    public void setGeneratedDate(LocalDate generatedDate)
    {
        this.generatedDate = generatedDate;
    }

    public LocalDate getStartingDate()
    {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate)
    {
        this.startingDate = startingDate;
    }

    public String getRangeType()
    {
        return rangeType;
    }

    public void setRangeType(String rangeType)
    {
        this.rangeType = rangeType;
    }

    public boolean getIncludeAllMonths()
    {
        return this.includeAllMonths;
    }

    public void setIncludeAllMonths(boolean includeAllMonths)
    {
        this.includeAllMonths = includeAllMonths;
    }

    public boolean getIncludeAllQuarters()
    {
        return this.includeAllQuarters;
    }

    public void setIncludeAllQuarters(boolean includeAllQuarters)
    {
        this.includeAllQuarters = includeAllQuarters;
    }

    public boolean getIncludePreviousMonth()
    {
        return this.includePreviousMonth;
    }

    public void setIncludePreviousMonth(boolean includePreviousMonth)
    {
        this.includePreviousMonth = includePreviousMonth;
    }

    public boolean getIncludePreviousQuarter()
    {
        return this.includePreviousQuarter;
    }

    public void setIncludePreviousQuarter(boolean includePreviousQuarter)
    {
        this.includePreviousQuarter = includePreviousQuarter;
    }

    public boolean getIncludePreviousYear()
    {
        return this.includePreviousYear;
    }

    public void setIncludePreviousYear(boolean includePreviousYear)
    {
        this.includePreviousYear = includePreviousYear;
    }
}
