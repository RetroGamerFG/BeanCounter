//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// ProcessedAccount - created when processing several transactions, holds the total(s) based on the account.


package Ledger.Accounts;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProcessedAccount
{
    private String accountRef;
    private String contraRef;
    private BigDecimal balance;
    private boolean valuesReverse;
    private boolean hasContra;

    public ProcessedAccount(String accountRef, boolean valuesReverse)
    {
        this.accountRef = accountRef;
        this.contraRef = null;
        this.valuesReverse = valuesReverse;
        this.balance = new BigDecimal(0);
        this.hasContra = false;
    }

    public String getAccountRef()
    {
        return accountRef;
    }

    public String getContraRef()
    {
        return contraRef;
    }

    public void setContraRef(String contraRef)
    {
        this.contraRef = contraRef;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public boolean getValuesReverse()
    {
        return valuesReverse;
    }

    public boolean getHasContra()
    {
        return hasContra;
    }

    public void setHasContra(boolean hasContra)
    {
        this.hasContra = hasContra;
    }

//
// Additional Functions
//

    //add() - increments balance by the passed BigDecimal value.
    public void add(BigDecimal addValue)
    {
        this.balance = balance.add(addValue);
    }

    //subtract() - decrements balance by the passed BigDecimal value.
    public void subtract(BigDecimal lessValue)
    {
        this.balance = balance.subtract(lessValue);
    }

//
// Static Functions
//

    //getProcessedContraAccounts() - creates an ArrayList of all ProcessedAccount objects that match a passed code identifying the contra-account's "parent".
    //I.E. the code 6001.001 would return an ArrayList containing 6008.001 and 6009.001.
    public static ArrayList<ProcessedAccount> getProcessedContraAccounts(ArrayList<ProcessedAccount> inProcessedAccounts, String contraCode)
    {
        ArrayList<ProcessedAccount> output = new ArrayList<>();

        for(ProcessedAccount currentAccount : inProcessedAccounts)
        {
            if(currentAccount.getContraRef() != null)
            {
                if(currentAccount.getContraRef().compareTo(contraCode) == 0)
                {
                    output.add(currentAccount);
                }
            }
        }

        return output;
    }
}
