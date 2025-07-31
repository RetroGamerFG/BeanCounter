//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// TransactionEntry - the base class for transactions, holds the effected account along with the debit or credit amount being made. A transaction
// will always hold 2 or more instances of a TransactionEntry.


package Ledger.Transactions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class TransactionEntry implements Serializable
{
    private String accountCode;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;

    public TransactionEntry()
    {
        this.accountCode = null;
        this.debitAmount = null;
        this.creditAmount = null;
    }

    public TransactionEntry(String accountCode, BigDecimal debitAmount, BigDecimal creditAmount)
    {
        this.accountCode = accountCode;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public BigDecimal getDebitAmount()
    {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount)
    {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount()
    {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount)
    {
        this.creditAmount = creditAmount;
    }

    //printDebitAmount() - returns the debit value as a formatted String if not null, else returns zero formatted
    public String printDebitAmount()
    {
        if(debitAmount != null)
        {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            return currencyFormatter.format(getDebitAmount());
        }
        
        return "$0.00";
    }

    //printCreditAmount() - returns the credit value as a formatted String if not null, else returns zero formatted
    public String printCreditAmount()
    {
        if(creditAmount != null)
        {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            return currencyFormatter.format(getCreditAmount());
        }
        
        return "$0.00";
    }
}
