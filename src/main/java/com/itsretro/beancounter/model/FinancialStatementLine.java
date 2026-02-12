//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// FinancialStatementLine: a model used for extracted repository data regarding amounts of accounts. Utilized by
//  FinancialStatement and the different statement views.
//

package com.itsretro.beancounter.model;

import java.math.BigDecimal;

public class FinancialStatementLine 
{
    private String accountName;
    private boolean isCredit;
    private BigDecimal totalAmount;

    //
    // Initializer(s)
    //

    public FinancialStatementLine()
    {
        this.accountName = null;
        this.isCredit = false;
        this.totalAmount = BigDecimal.ZERO;
    }

    //for use with the journalEntryLine repository
    public FinancialStatementLine(String accountName, BigDecimal debitAmount, BigDecimal creditAmount)
    {
        this.accountName = accountName;

        if(debitAmount.compareTo(creditAmount) > 0)
        {
            this.totalAmount = debitAmount;
            this.isCredit = false;
        }
        else if (debitAmount.compareTo(creditAmount) < 0)
        {
            this.totalAmount = creditAmount;
            this.isCredit = true;
        }
        else
        {
            this.totalAmount = BigDecimal.ZERO;
            this.isCredit = false;
        }
    }

    //
    // Getters & Setters
    //

    public String getAccountName()
    {
        return this.accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public boolean getIsCredit()
    {
        return this.isCredit;
    }

    public void setIsCredit(boolean isCredit)
    {
        this.isCredit = isCredit;
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
