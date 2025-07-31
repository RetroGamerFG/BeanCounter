//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// Ledger - the base class for all operations regarding the use of journal entries and transactions. Used to call operations
// that involve the use of account codes and transactions.

package Ledger;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

import Ledger.Accounts.Account;
import Ledger.Accounts.AccountEntries;
import Ledger.Transactions.Transaction;
import Ledger.Transactions.TransactionEntry;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
// Transaction Retrieval Functions
//

    //getTransactionsByDateRange() - return transactions within a set range provided.
    public ArrayList<Transaction> getTransactionsByDateRange(LocalDate inStartDate, LocalDate inEndDate)
    {
        ArrayList<Transaction> result = new ArrayList<>();

        for(Transaction currentTransaction : transactions)
        {
            if(currentTransaction.getPostDate().compareTo(inStartDate) >= 0 && currentTransaction.getPostDate().compareTo(inEndDate) <= 0)
            {
                result.add(currentTransaction);
            }
        }

        return result;
    }

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

    //getTransactionByCodeArray() - used for account detail forms, will retrieve all transactions referenced in the passed array.
    public ArrayList<Transaction> getTransactionByCodeArray(ArrayList<Integer> inCodes)
    {
        ArrayList<Transaction> output = new ArrayList<>();

        for(int currentNumber : inCodes)
        {
            for(Transaction currentTransaction : transactions)
            {
                if(currentNumber == currentTransaction.getTransactionNumber())
                {
                    output.add(currentTransaction);
                }
            }
        }

        return output;
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

//
// Transaction Filtering Functions
//
    //filterTransactions() - cleans an ArrayList of Transactions based on status.
    public void filterTransactionsByStatus(ArrayList<Transaction> inTransactionList, String comparator)
    {
        Iterator<Transaction> iterator = inTransactionList.iterator();
        while(iterator.hasNext())
        {
            Transaction current = iterator.next();
            
            if(current.getPostStatus().compareTo(comparator) != 0)
            {
                iterator.remove();
            }
        }
    }

    //filterTransactionsByAccountCode() - cleans an ArrayList of Transactions whose entries at least contain the passed account code.
    public void filterTransactionsByAccountCode(ArrayList<Transaction> inTransactionList, String accountCode)
    {
        Iterator<Transaction> iterator = inTransactionList.iterator();

        while(iterator.hasNext())
        {
            Transaction currentTransaction = iterator.next();
            boolean containsCode = false;

            for(TransactionEntry currentEntry : currentTransaction.getTransactionEntries())
            {
                if(currentEntry.getAccountCode().compareTo(accountCode) == 0)
                {
                    containsCode = true;
                }
            }

            if(containsCode == false)
            {
                iterator.remove();
            }
        }
    }

    //filterTransactionsByAccountCodeRange() - cleans an ArrayList of Transactions whose entries match a specified range of codes
    public void filterTransactionsByAccountCodeRange(ArrayList<Transaction> inTransactionList, String startCode, String endCode)
    {
        Iterator<Transaction> iterator = inTransactionList.iterator();

        while(iterator.hasNext())
        {
            Transaction currentTransaction = iterator.next();
            boolean containsCode = false;

            for(TransactionEntry currentEntry : currentTransaction.getTransactionEntries())
            {
                String currentCode = currentEntry.getAccountCode();

                if(currentCode.compareTo(startCode) >= 0 && currentCode.compareTo(endCode) <= 0)
                {
                    containsCode = true;
                }
            }

            if(containsCode == false)
            {
                iterator.remove();
            }
        }
    }

//
// Transaction Search Functions
//

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
            Alert alert = new Alert(AlertType.ERROR, "No results from search query. Check your query and try again.");
            alert.showAndWait();
            return null;
        }

    }

//
// Account Retrieval Functions
//

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

    //getAccountDescriptions() - creates and returns an ArrayList of String containing all account descriptions.
    public ArrayList<String> getAccountDescriptions()
    {
        ArrayList<String> output = new ArrayList<>();

        for(Account entry : accountItems.getAllAccounts())
        {
            output.add(entry.getAccountDescription());
        }

        return output;
    }

    //getAccountDescriptionByCode() - returns the found account's description based on the passed account code.
    public String getAccountDescriptionByCode(String inCode)
    {
        for(Account current : accountItems.getAllAccounts())
        {
            if(current.getAccountCode().compareTo(inCode) == 0)
            {
                return current.getAccountDescription();
            }
        }

        return null;
    }

//
// Additional Functions
//

    //validateTransaction() - performs various steps to validate the values of a transaction, and returns boolean based on result.
    public boolean validateTransaction(Transaction newTransaction)
    {
        //
        // 1 - Validate Input Entries

        if(newTransaction.getPostDate() == null)
        {
            Alert alert = new Alert(AlertType.ERROR, "No post date was added...");
            alert.showAndWait();
            return false;
        }

        if(newTransaction.getTransactionDescription() == null)
        {
            Alert alert = new Alert(AlertType.ERROR, "No description was added...");
            alert.showAndWait();
            return false;
        }

        //
        // 2 - Validate Account Codes

        for(TransactionEntry current : newTransaction.getTransactionEntries())
        {
            String currentAccountCode = current.getAccountCode();

            if(currentAccountCode == null)
            {
                Alert alert = new Alert(AlertType.ERROR, "Account(s) left blank...");
                alert.showAndWait();
                return false;
            }

            if(!accountItems.accountExists(currentAccountCode))
            {
                Alert alert = new Alert(AlertType.ERROR, "Typed account(s) do not exist in ledger. Confirm the account codes are correct.");
                alert.showAndWait();
                return false;
            }
        }

        //
        // 3 - Validate Amounts

        if(!newTransaction.validateDifference())
        {
            Alert alert = new Alert(AlertType.ERROR, "Transaction does not balance...");
            alert.showAndWait();
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
