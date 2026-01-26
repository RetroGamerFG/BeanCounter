package com.itsretro.beancounter.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.FinancialStatementBlock;
import com.itsretro.beancounter.model.JournalEntry;
import com.itsretro.beancounter.model.JournalEntryLine;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

@Component
public class IncomeStatementLogic 
{
    public void addJournalEntriesToIncomeStatementView(IncomeStatementView incomeStatementView, List<Object[]> queryResults)
    {
        FinancialStatementBlock fsb = new FinancialStatementBlock();

        for(Object[] row : queryResults)
        {
            JournalEntry je = (JournalEntry) row[0];
            JournalEntryLine jel = (JournalEntryLine) row[1];

            
        }
    }
}
