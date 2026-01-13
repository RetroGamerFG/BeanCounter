package com.itsretro.beancounter.logic;

import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.Account;
import com.itsretro.beancounter.model.AccountDetail;
import com.itsretro.beancounter.model.JournalEntry;
import com.itsretro.beancounter.model.JournalEntryLine;
import com.itsretro.beancounter.viewmodel.AccountDetailBlockMonth;
import com.itsretro.beancounter.viewmodel.AccountDetailBlockYear;
import com.itsretro.beancounter.viewmodel.AccountDetailLine;
import com.itsretro.beancounter.viewmodel.AccountDetailView;

@Component
public class AccountDetailLogic 
{
    //
    //
    //
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
            Map<Year, AccountDetailBlockYear> adbyMap = new HashMap<>();
            
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

    //
    //
    //
    public void addJournalEntriesToAccountDetailView(AccountDetailView adv, Account account, List<Object[]> queryResults)
    {
        //using a map, store the results, keeping information from 'JournalEntry' and 'JournalEntryLine'
        //includes comparator to keep output sorted by the post date
        //Map<JournalEntry, List<JournalEntryLine>> mappedResults = new TreeMap<>((je1, je2) -> je1.getPostDate().compareTo(je2.getPostDate()));

        //for each queried object, convert to proper models, then store results
        for(Object[] row : queryResults)
        {
            JournalEntry je = (JournalEntry) row[0];
            JournalEntryLine jel = (JournalEntryLine) row[1];

            AccountDetailLine adl = mapToLine(je, jel);

            // 2. Safely navigate the nested structure
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

    //
    //
    //

    //
    //
    //
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
}
