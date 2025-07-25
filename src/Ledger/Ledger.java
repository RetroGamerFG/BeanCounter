//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// Ledger - the base class for all operations regarding the use of journal entries and transactions. Used to call operations
// that involve the use of account codes and transactions.

package Ledger;

import java.io.Serializable;
import java.util.ArrayList;

import Ledger.Accounts.Account;
import Ledger.Accounts.AccountEntries;
import Ledger.Transactions.Transaction;
import Ledger.Transactions.TransactionEntry;

public class Ledger implements Serializable
{
    private AccountEntries accountItems; //holds all account identifiers
    private ArrayList<Transaction> transactions; //holds all transactions made

    public Ledger()
    {
        accountItems = new AccountEntries();
        accountItems.createDefaults();
        transactions = new ArrayList<>();
    }

    public AccountEntries getAccountEntries()
    {
        return accountItems;
    }

    public ArrayList<Transaction> getTransactions()
    {
        return transactions;
    }

    //note - there should be no instances of any set methods for class members beyond initializer

//
// Account Retrieval Functions
//

    //getAllUnpostedTransactions() - will retrieve all unposted transactions and stored into an ArrayList.
    public ArrayList<Transaction> getAllUnpostedTransactions()
    {
        ArrayList<Transaction> unpostedTransactions = new ArrayList<>();

        for(Transaction currentTransaction : transactions)
        {
            if(currentTransaction.getPostStatus().compareTo("Posted") != 0)
            {
                unpostedTransactions.add(currentTransaction);
            }
        }

        return unpostedTransactions;
    }

    //getAllPostedTransactions() - will retrieve all posted transactions and stored into an ArrayList.
    public ArrayList<Transaction> getAllPostedTransactions()
    {
        ArrayList<Transaction> postedTransactions = new ArrayList<>();

        for(Transaction currentTransaction : transactions)
        {
            if(currentTransaction.getPostStatus().compareTo("Posted") == 0)
            {
                postedTransactions.add(currentTransaction);
            }
        }

        return postedTransactions;
    }

    //addTransaction() - adds a "Transaction" object into the member ArrayList
    public void addTransaction(Transaction newTransaction)
    {
        transactions.add(newTransaction);
    }

    //removeTransaction() - removes a "Transaction" object from the member ArrayList
    public void removeTransaction(Transaction removeTransaction)
    {
        transactions.remove(removeTransaction);
    }

    //searchTransactionByNumber() - performs a search by the provided number, and returns Transaction if found.
    //additional search queries will be added in the future, including
        //search by description keywords
        //search by date (created & post)
        //search by post status
    public Transaction searchTransactionByNumber(String searchNum)
    {
        try
        {
            return transactions.get(Integer.valueOf(searchNum));
        }
        catch (Exception error)
        {
            System.out.println("No results from search query");
            return null;
        }

    }

    //getAccountCodes() - creates and returns an ArrayList of String containing all account codes.
    public ArrayList<String> getAccountCodes()
    {
        ArrayList<String> output = new ArrayList<>();

        for(Account entry : accountItems.getAllAccounts())
        {
            output.add(entry.getAccountCode());
        }

        return output;
    }

    //validateTransaction() - performs various steps to validate the values of a transaction, and returns boolean based on result.
    public boolean validateTransaction(Transaction newTransaction)
    {
        //
        // 1 - Validate Input Entries

        if(newTransaction.getPostDate() == null)
        {
            System.out.println("No post date selected");
            return false;
        }

        if(newTransaction.getTransactionDescription() == null)
        {
            System.out.println("No description was added");
            return false;
        }

        //
        // 2 - Validate Account Codes

        for(TransactionEntry current : newTransaction.getTransactionEntries())
        {
            String currentAccountCode = current.getAccountCode();

            if(currentAccountCode == null)
            {
                System.out.println("Account(s) left blank");
                return false;
            }

            if(!accountItems.accountExists(currentAccountCode))
            {
                System.out.println("Account(s) invalid");
                return false;
            }
        }

        //
        // 3 - Validate Amounts

        if(!newTransaction.validateDifference())
        {
            System.out.println("Transaction does not balance");
            return false;
        }

        return true;
    }

    //setTransactionNumber() - will automatically assign a transaction number based on existing transactions in the ArrayList member.
    public void setTransactionNumber(Transaction newTransaction)
    {
        int result = 1;

        if(transactions.size() > 0)
        {
            result = transactions.getLast().getTransactionNumber(); //crucial to account for deleted entries
            result++;
        }

        newTransaction.setTransactionNumber(result);
    }
}
