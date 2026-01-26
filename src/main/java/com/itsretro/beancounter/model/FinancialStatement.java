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

    @Column(name = "CreationDate")
    private LocalDate creationDate;

    @Column(name = "StartingDate")
    private LocalDate startingDate;

    @Column(name = "RangeType", length = 3)
    private String rangeType;

    @Column(name = "IncludeMTD", nullable = true)
    private boolean includeMTD;

    @Column(name = "IncludeQTD", nullable = true)
    private boolean includeQTD;

    @Column(name = "IncludeYTD", nullable = true)
    private boolean includeYTD;

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

    public LocalDate getCreationDate()
    {
        return this.creationDate;
    }

    public void setCreationDate(LocalDate creationDate)
    {
        this.creationDate = creationDate;
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

    public boolean isIncludeMTD()
    {
        return includeMTD;
    }

    public void setIncludeMTD(boolean includeMTD)
    {
        this.includeMTD = includeMTD;
    }

    public boolean isIncludeQTD() {
        return includeQTD;
    }

    public void setIncludeQTD(boolean includeQTD)
    {
        this.includeQTD = includeQTD;
    }

    public boolean isIncludeYTD()
    {
        return includeYTD;
    }

    public void setIncludeYTD(boolean includeYTD)
    {
        this.includeYTD = includeYTD;
    }
}
