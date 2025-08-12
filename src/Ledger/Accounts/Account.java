//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// Account - the base class for accounts. Used to create an instance of a unique account used in journal entries and statements. Fields
// include the account code, account name, type (asset/liability/SE/etc.), a description, and if considered "current" for statement purposes.


package Ledger.Accounts;

import java.io.Serializable;

public class Account implements Serializable
{
    private String accountCode;
    private String accountName;
    private String accountType;
    private String accountDescription;

    private boolean allowCurrentStatus; //if enabled, forms can calculate amounts deemed current (within a year's timeframe)
    private String contraAccountCode;

    public Account(String accountCode, String accountName, String accountType, String accountDescription, boolean allowCurrentStatus, String contraAccountCode)
    {
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.accountType = accountName;
        this.accountDescription = accountDescription;
        this.allowCurrentStatus = allowCurrentStatus;
        this.contraAccountCode = contraAccountCode;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public String getAccountType()
    {
        return accountType;
    }

    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }

    public boolean getAllowCurrentStatus()
    {
        return allowCurrentStatus;
    }

    public void setCurrentStatus(boolean allowCurrentStatus)
    {
        this.allowCurrentStatus = allowCurrentStatus;
    }

    public String getAccountDescription()
    {
        return accountDescription;
    }

    public void setContraAccountCode(String contraAccountCode)
    {
        this.contraAccountCode = contraAccountCode;
    }

    public String getContraAccountCode()
    {
        return this.contraAccountCode;
    }

//
// Additional Functions
//

    //hasContraAccount() - returns a boolean based on whether the contraAccountCode member is/isn't null.
    public boolean hasContraAccount()
    {
        if(contraAccountCode != null)
        {
            return true;
        }
        
        return false;
    }
}
