//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountDetailLogic: a logic class for AccountDetail and related classes.
// Includes methods related to class member assignment and computation of amounts.
//

package com.itsretro.beancounter.logic;

import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.Account;
import com.itsretro.beancounter.model.AccountDetail;
import com.itsretro.beancounter.model.AccountDetailBlockMonth;
import com.itsretro.beancounter.model.AccountDetailBlockYear;
import com.itsretro.beancounter.model.AccountDetailLine;
import com.itsretro.beancounter.model.FinancialBlock;
import com.itsretro.beancounter.model.JournalEntry;
import com.itsretro.beancounter.model.JournalEntryLine;
import com.itsretro.beancounter.viewmodel.AccountDetailView;

@Component
public class AccountDetailLogic 
{
    //buildEmptyAccountDetailView() - called first, initializes an AccountDetailView instance with a list of accounts and valid
        //attributes passed. Assumes passed attributes are valid.
    //inputs -
        //accountDetail: an AccountDetail instance to create a view from.
        //accounts: a list of Account instances to include in the view.
    //output - an AccountDetailView with AccountDetailBlock(s) for each passed account.
    public AccountDetailView buildEmptyAccountDetailView(AccountDetail accountDetail, List<Account> accounts)
    {
        AccountDetailView adv = new AccountDetailView();

        boolean multipleYears = true;

        if(accountDetail.getStartingDate().getYear() == accountDetail.getEndingDate().getYear())
        {
            multipleYears = false;
        }

        for(Account account : accounts)
        {
            SortedMap<Year, AccountDetailBlockYear> adbyMap = new TreeMap<>();
            
            //determine the number of years and months from the start and end date
            for(int y = accountDetail.getStartingDate().getYear(); y <= accountDetail.getEndingDate().getYear(); y++)
            {
                AccountDetailBlockYear adby = new AccountDetailBlockYear();

                //perform different logic for starting and ending years (assumes not the same start or end months)
                if(multipleYears == false) //for a period within a single year
                {
                    for(int m = accountDetail.getStartingDate().getMonthValue(); m <= accountDetail.getEndingDate().getMonthValue(); m++)
                    {
                        adby.getMonthBlocks().put(Month.of(m), new AccountDetailBlockMonth());
                    }
                }
                else if(y == accountDetail.getStartingDate().getYear()) //for a period more than a year that starts on a different month than end
                {
                    for(int m = accountDetail.getStartingDate().getMonthValue(); m <= 12; m++)
                    {
                        adby.getMonthBlocks().put(Month.of(m), new AccountDetailBlockMonth());
                    }
                }
                else if(y == accountDetail.getEndingDate().getYear()) //for a period more than a year that ends on a different month than start
                {
                    for(int m = 1; m <= accountDetail.getEndingDate().getMonthValue(); m++)
                    {
                        adby.getMonthBlocks().put(Month.of(m), new AccountDetailBlockMonth());
                    }
                }
                else //for the entire year
                {
                    for(int m = 1; m <= 12; m++)
                    {
                        adby.getMonthBlocks().put(Month.of(m), new AccountDetailBlockMonth());
                    }
                }

                adbyMap.put(Year.of(y), adby);
            }

            adv.getAccountDetailBlocks().put(account, adbyMap);
        }

        return adv;
    }

    //addJournalEntriesToAccountDetailView() - given an initialized AccountDetailView, AccountDetailLine instances are created and 
        //populated for the given account and the contents of a query to JournalEntryRepository (called from service).
    //inputs -
        //adv: an AccountDetailView instance to add to.
        //account: the current Account instance to append AccountDetailLine instances to.
        //queryResults: a list of objects containing queried results from the JournalEntryRepository.
    //output - none; appends the AccountDetailView with journal entries.
    public void addJournalEntriesToAccountDetailView(AccountDetailView adv, Account account, List<Object[]> queryResults)
    {
        //for each queried object, convert to proper models, then store results
        for(Object[] row : queryResults)
        {
            JournalEntry je = (JournalEntry) row[0];
            JournalEntryLine jel = (JournalEntryLine) row[1];

            AccountDetailLine adl = mapToLine(je, jel);

            //navigate the structure to insert the account detail line to the appropriate account->year->month
            Map<Year, AccountDetailBlockYear> yearMap = adv.getAccountDetailBlocks().get(account);
            if (yearMap == null)
            {
                continue; // Skip if account isn't in the view
            }

            AccountDetailBlockYear yearBlock = yearMap.get(Year.of(je.getPostDate().getYear()));
            if (yearBlock == null) 
            {
                continue; // Skip if year is out of range
            }

            AccountDetailBlockMonth monthBlock = yearBlock.getMonthBlocks().get(je.getPostDate().getMonth());
            if (monthBlock != null) 
            {
                monthBlock.insert(adl);
            }
        }
    }

    //createTotalsForAccountDetailView() - performs calculations to get debit, credit, and grand total amounts for AccountDetailView.
    //inputs - adv: an instance of AccountDetailView.
    //output - none; updates instance values.
    public void calculateTotalsForAccountDetailView(AccountDetailView adv)
    {
        //reset values to zero
        adv.setTotalDebits(BigDecimal.ZERO);
        adv.setTotalCredits(BigDecimal.ZERO);
        adv.setGrandTotal(BigDecimal.ZERO);

        //iterate the account detail view to perform total calculations for each year and each month
        for(SortedMap<Year, AccountDetailBlockYear> yearMap : adv.getAccountDetailBlocks().values())
        {
            //iterate through each account
            for(AccountDetailBlockYear yearBlock : yearMap.values())
            {
                //process the current yearBlock and it's child values
                calculateYearTotals(yearBlock);

                //increment the view totals from calculated values
                adv.setTotalDebits(adv.getTotalDebits().add(yearBlock.getTotalDebits()));
                adv.setTotalCredits(adv.getTotalCredits().add(yearBlock.getTotalCredits()));
            }

            //perform the grand total calculation
            finalizeGrandTotal(adv);
        }

        System.out.println();
    }

    //createDateString() - given a valid accountDetail, initializes the dateString member with passed values.
    //inputs - ad: an AccountDetail instance.
    //output - a formatted String with passed values.
    public String createDateString(AccountDetail ad)
    {
        String output = ad.getStartingDate().toString() + " - " + ad.getEndingDate();
        return output;
    }

    //mapToLine() - helper function to create the AccountDetailLine instance.
    //inputs -
        //je: a JournalEntry instance.
        //jel: a JounalEntryLine instance.
    //output - an initialized AccountDetailLine with passed values.
    private AccountDetailLine mapToLine(JournalEntry je, JournalEntryLine jel)
    {
        AccountDetailLine adl = new AccountDetailLine();
        adl.setJournalEntryID(je.getJournalEntryID().toString());
        adl.setDescription(je.getTransactionDescription());
        adl.setPostSignature(je.getPostSignature());
        adl.setPostDate(je.getPostDate().toString()); 

        if ("D".equalsIgnoreCase(jel.getTransactionType()))
        {
            adl.setAmount(jel.getDebitAmount());
            adl.setType("D");
        }
        else 
        {
            adl.setAmount(jel.getCreditAmount());
            adl.setType("C");
        }

        return adl;
    }

    //calculateYearTotals() - helper function to calculate totals for the AccountDetailBlockYear instance.
    //inputs - yearBlock: an instance of an AccountDetailBlockYear to calculate totals for.
    //output - none; updates instance values.
    private void calculateYearTotals(AccountDetailBlockYear yearBlock)
    {
        //reset values to zero
        yearBlock.setTotalDebits(BigDecimal.ZERO);
        yearBlock.setTotalCredits(BigDecimal.ZERO);
        yearBlock.setGrandTotal(BigDecimal.ZERO);

        //iterate through each yearBlock to calculate each containing monthBlock
        for(AccountDetailBlockMonth monthBlock : yearBlock.getMonthBlocks().values())
        {
            //process the current monthBlock and it's child values
            calculateMonthTotals(monthBlock);

            //increment the yearBlock totals from calculated values
            yearBlock.setTotalDebits(yearBlock.getTotalDebits().add(monthBlock.getTotalDebits()));
            yearBlock.setTotalCredits(yearBlock.getTotalCredits().add(monthBlock.getTotalCredits()));
        }

        //perform the grand total calculation
        finalizeGrandTotal(yearBlock);
    }

    //calculateMonthTotals() - helper function to calculate totals for the AccountDetailBlockMonth instance.
    //inputs - monthBlock: an instance of an AccountDetailBlockMonth to calculate totals for.
    //output - none; updates instance values.
    private void calculateMonthTotals(AccountDetailBlockMonth monthBlock)
    {
        //reset values to zero
        monthBlock.setTotalDebits(BigDecimal.ZERO);
        monthBlock.setTotalCredits(BigDecimal.ZERO);
        monthBlock.setGrandTotal(BigDecimal.ZERO);

        //iterate through each monthBlock, incrementing totals based on each AccountDetailLine
        for(AccountDetailLine line : monthBlock.getAccountDetailLines())
        {
            monthBlock.setTotalDebits(monthBlock.getTotalDebits().add(line.getDebitAmount()));
            monthBlock.setTotalCredits(monthBlock.getTotalCredits().add(line.getCreditAmount()));
        }

        //perform the grand total calculation
        finalizeGrandTotal(monthBlock);
    }

    //finalizeGrandTotal() - helper function to calculate grand totals for a given FinancialBlock instance.
    //inputs - block: a class that utilizes the FinancialBlock interface.
    //output - none; updates instance values.
    private void finalizeGrandTotal(FinancialBlock block)
    {
        BigDecimal debits = block.getTotalDebits();
        BigDecimal credits = block.getTotalCredits();
        
        // Reset grand total before starting
        BigDecimal runningTotal = BigDecimal.ZERO;

        if(debits.compareTo(credits) > 0)
        {
            runningTotal = runningTotal.add(debits).subtract(credits);
            block.setGrandTotal(runningTotal);
            block.setGrandTotalType("D");
        } 
        else 
        {
            runningTotal = runningTotal.add(credits).subtract(debits);
            block.setGrandTotal(runningTotal);
            block.setGrandTotalType("C");
        }
    }
}
