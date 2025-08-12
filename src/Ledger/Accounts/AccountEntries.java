//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// AccountEntries - used to hold multiple instances of "Account". Includes ability to load predefined accounts, as well as 
// create new custom accounts.


package Ledger.Accounts;

import java.io.Serializable;
import java.util.ArrayList;

public class AccountEntries implements Serializable
{
    private ArrayList<Account> accountItems;

    public AccountEntries() 
    {
        accountItems = new ArrayList<>();
    }

    public void insertAccountItem(Account newItem) 
    {
        accountItems.add(newItem);
    }

    public void removeAccountItem(Account removeItem) 
    {
        accountItems.remove(removeItem);
    }

//
// Account Retrieval
//

    //accountExists() - searches for accounts based on a provided code. Returns a boolean based on result.
    public boolean accountExists(String searchCode)
    {
        for(Account currentItem : accountItems)
        {
            if(currentItem.getAccountCode().compareTo(searchCode) == 0)
            {
                return true;
            }
        }

        return false;
    }

    //getAllAccounts() - returns the ArrayList member, consisting of all accounts.
    public ArrayList<Account> getAllAccounts()
    {
        return accountItems;
    }

    //getAccountByCode() - searches for account by matching code, returns found "Account" object or null if unsuccessful.
    public Account getAccountByCode(String code)
    {
        for(Account current : accountItems)
        {
            if(current.getAccountCode().compareTo(code) == 0)
            {
                return current;
            }
        }

        return null;
    }

    //getAccountsByType() - returns an ArrayList of all "Account" objects that match the type criteria.
    public ArrayList<Account> getAccountsByType(String type)
    {
        ArrayList<Account> output = new ArrayList<>();

        for(Account current : accountItems)
        {
            if(current.getAccountType().compareToIgnoreCase(type) == 0)
            {
                output.add(current);
            }
        }

        return output;
    }

    //getAccountByDescription() - returns the account code if a description is passed, or null if no match is found.
    public String getAccountCodeByDescription(String description)
    {
        for(Account current : accountItems)
        {
            if(current.getAccountDescription().compareToIgnoreCase(description) == 0)
            {
                return current.getAccountCode();
            }
        }

        return null;
    }

    //createDefaults() - for first-time program usage, creates default accounts. These should meet the basic needs of most users, but extra functionality to add custom
    //accounts is also included in the front-end.
    public void createDefaults()
    {
        accountItems = new ArrayList<>();

        //Assets
        accountItems.add(new Account("1001.001", "Cash", "Asset", "Cash", true, null));
        accountItems.add(new Account("1002.001", "Accounts Receivable", "Asset", "Accounts Receivable: Short-Term", true, null));
        accountItems.add(new Account("1003.001", "Inventory", "Asset", "Inventory", true, null));
        accountItems.add(new Account("1004.001", "Marketable Securities", "Asset", "Marketable Securities", true, null));
        accountItems.add(new Account("1005.001", "Notes Receivable", "Asset", "Notes Receivable", true, null));
        accountItems.add(new Account("1006.001", "Prepaid Expenses", "Asset", "Prepaid Expenses", true, null));
        accountItems.add(new Account("1007.001", "Goodwill", "Asset", "Goodwill", false, null));
        accountItems.add(new Account("1008.001", "Patents", "Asset", "Patents", false, null));
        accountItems.add(new Account("1009.001", "Licenses", "Asset", "Licenses and Franchises", false, null));

        accountItems.add(new Account("3001.001", "Equipment", "Asset", "Equipment", false, null));
        accountItems.add(new Account("3002.001", "Building & Property", "Asset", "Building & Property", false, null));
        accountItems.add(new Account("3003.001", "Land", "Asset", "Land", false, null));

        //Liabilities
        accountItems.add(new Account("2001.001", "Accounts Payable", "Liability", "Accounts Payable", true, null));
        accountItems.add(new Account("2002.001", "Securities Issued", "Liability", "Securities Issued", true, null));
        accountItems.add(new Account("2003.001", "Notes Payable", "Liability", "Notes Payable: Short-Term", true, null));
        accountItems.add(new Account("2004.001", "Interest Payable", "Liability", "Interest Payable", true, null));
        accountItems.add(new Account("2005.001", "Wages Payable", "Liability", "Wages Payable", false, null));
        accountItems.add(new Account("2006.001", "Unearned Revenue", "Liability", "Unearned Revenue", true, null));

        accountItems.add(new Account("4001.001", "Accumulated Depreciation: Equipment", "Asset", "Accumulated Depreciation: Equipment", false, "3001.001"));
        accountItems.add(new Account("4002.001", "Accumulated Depreciation: Building & Property", "Asset", "Accumulated Depreciation: Building & Property", false, "3002.001"));
        accountItems.add(new Account("4003.001", "Accumulated Amortization: Patents", "Asset", "Accumulated Amortization: Patents", false, "1007.001"));
        accountItems.add(new Account("4004.001", "Accumulated Amortization: Licenses", "Asset", "Accumulated Amortization: Licenses", false, "1008.001"));

        //Shareholders Equity
        accountItems.add(new Account("5001.001", "Common Stock", "Stockholders Equity", "Common Stock", false, null));
        accountItems.add(new Account("5002.001", "Preferred Stock", "Stockholders Equity", "Preferred Stock", false, null));
        accountItems.add(new Account("5003.001", "Additional Paid-In Capital", "Stockholders Equity", "Additional Paid-In Capital", false, null));
        accountItems.add(new Account("5004.001", "Retained Earnings", "Stockholders Equity", "Retained Earnings", false, null));
        accountItems.add(new Account("5005.001", "Treasury Stock", "Stockholders Equity", "Treasury Stock", false, "5004.001"));
        accountItems.add(new Account("5006.001", "Other Comprehensive Income", "Stockholders Equity", "Treasury Stock", false, null));

        //Revenue
        accountItems.add(new Account("6001.001", "Sales Revenue", "Revenue", "Sales Revenue", false, null));
        accountItems.add(new Account("6002.001", "Service Revenue", "Revenue", "Service Revenue", false, null));
        accountItems.add(new Account("6003.001", "Rental Income", "Revenue", "Rental Income", false, null));
        accountItems.add(new Account("6004.001", "Interest Income", "Revenue", "Interest Income", false, null));
        accountItems.add(new Account("6005.001", "Gain on Sale of Asset", "Revenue", "Gain on Sale of Asset", false, null));
        accountItems.add(new Account("6006.001", "Dividend Income", "Revenue", "Dividend Income", false, null));
        accountItems.add(new Account("6007.001", "Misc Income", "Revenue", "Misc Income", false, null));
        accountItems.add(new Account("6008.001", "Sales Returns & Allowances", "Revenue", "Sales Returns & Allowances", false, "6001.001"));
        accountItems.add(new Account("6009.001", "Sales Discounts", "Revenue", "Sales Discounts", false, "6001.001"));
        accountItems.add(new Account("6010.001", "Service Discounts", "Revenue", "Service Discounts", false, "6002.001"));
        accountItems.add(new Account("6011.001", "Rental Discounts", "Revenue", "Rental Discounts", false, "6003.001"));

        //Expenses
        accountItems.add(new Account("7001.001", "Cost of Goods Sold", "Expense", "Cost of Goods Sold", false, null));
        accountItems.add(new Account("7002.001", "Salaries & Wages Expense", "Expense", "Salaries & Wages Expense", false, null));
        accountItems.add(new Account("7003.001", "Advertising Expense", "Expense", "Advertising Expense", false, null));
        accountItems.add(new Account("7004.001", "Administration Expense", "Expense", "Administration Expense", false, null));
        accountItems.add(new Account("7004.002", "Commissions Expense", "Expense", "Commissions Expense", false, null));
        accountItems.add(new Account("7005.001", "Shipping Expense", "Expense", "Shipping Expense", false, null));
        accountItems.add(new Account("7006.001", "Rent Expense", "Expense", "Rent Expense", false, null));
        accountItems.add(new Account("7007.001", "Utilities Expense", "Expense", "Utilities Expense", false, null));
        accountItems.add(new Account("7008.001", "Office Supplies Expense", "Expense", "Office Supplies Expense", false, null));
        accountItems.add(new Account("7009.001", "Insurance Expense", "Expense", "Insurance Expense", false, null));
        accountItems.add(new Account("7010.001", "Legal Expense", "Expense", "Legal Expense", false, null));
        accountItems.add(new Account("7011.001", "Accounting Expense", "Expense", "Accounting Expense", false, null));
        accountItems.add(new Account("7012.001", "Interest Expense", "Expense", "Interest Expense", false, null));
        accountItems.add(new Account("7013.001", "Bank Fees Expense", "Expense", "Bank Fees Expense", false, null));
        accountItems.add(new Account("7014.001", "Loss on Disposal of Asset", "Expense", "Loss on Disposal of Asset", false, null));

        accountItems.add(new Account("8001.001", "Depreciation Expense", "Expense", "Depreciation Expense", false, null));
        accountItems.add(new Account("8002.001", "Amortization Expense", "Expense", "Amortization Expense", false, null));
    }

    //hasContraAccount() - determines if the passed account has any contra account(s), returns result as boolean.
    public boolean hasContraAccount(String code)
    {
        Account foundAccount = getAccountByCode(code);

        if(foundAccount == null)
        {
            return false;
        }

        if(foundAccount.hasContraAccount())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //isContraAccount() - determines if the passed account is a contra account (other accounts reference account code), returns result as boolean.
    public boolean isContraAccount(String code)
    {
        Account foundAccount = getAccountByCode(code);

        if(foundAccount == null)
        {
            return false;
        }

        for(Account currentAccount : accountItems)
        {
            if(currentAccount.getContraAccountCode() != null)
            {
                if(currentAccount.getContraAccountCode().compareTo(foundAccount.getAccountCode()) == 0)
                {
                    return true;
                }
            }
        }

        return false;
    }

    //getContraAccountReferenceByCode() - returns a String containing the account code of the referenced contra-account for the passed account's code.
    public String getContraAccountReferenceByCode(String code)
    {
        Account foundAccount = getAccountByCode(code);

        if(foundAccount == null)
        {
            return null;
        }

        return foundAccount.getContraAccountCode();
    }
}
