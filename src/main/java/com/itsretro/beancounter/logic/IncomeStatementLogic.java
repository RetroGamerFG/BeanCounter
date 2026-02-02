package com.itsretro.beancounter.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.FinancialStatementColumn;
import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.model.JournalEntryLine;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

@Component
public class IncomeStatementLogic 
{
    public void createColumn(IncomeStatementView isv, String columnLabel, String columnRange)
    {
        //create new column for revenue
        FinancialStatementColumn fsc = new FinancialStatementColumn();
        fsc.setColumnLabel(columnLabel);
        fsc.setColumnRange(columnRange);

        isv.getRevenueItems().add(fsc);
        
        //force new column for expenses
        fsc = new FinancialStatementColumn();
        fsc.setColumnLabel(columnLabel);
        fsc.setColumnRange(columnRange);

        isv.getExpenseItems().add(fsc);

        isv.incrementColumnCount();
    }

    public void addJournalEntriesToColumn(IncomeStatementView isv, List<FinancialStatementLine> queryResults, String type, int colIndex)
    {
        for(FinancialStatementLine fsl : queryResults)
        {
            if("R".compareToIgnoreCase(type) == 0)
            {
                isv.getRevenueItems().get(colIndex).getLines().add(fsl);
            }
            else
            {
                isv.getExpenseItems().get(colIndex).getLines().add(fsl);
            }
        }
    }

    public void calculateTotals(IncomeStatementView isv)
    {
        //calculateBlockTotals(isv.getRevenueItems());
        //calculateBlockTotals(isv.getExpenseItems());

        //profit/loss = revenue - expenses

        //calculate net income for each block's revenue and expense totals, add to view's list member
        for(int c = 0; c < isv.getRevenueItems().size(); c++)
        {
            //BigDecimal result = isv.getRevenueItems().get(c).getTotalAmount();
            //result = result.subtract(isv.getExpenseItems().get(c).getTotalAmount());
            //isv.getNetIncomeByBlock().add(result);
        }
    }

    public void extractMatchedAccountNames(IncomeStatementView isv)
    {
        for(FinancialStatementColumn fsc : isv.getRevenueItems())
        {
            for(FinancialStatementLine fsl : fsc.getLines())
            {
                isv.getRevenueAccounts().add(fsl.getAccountName());
            }
        }

        for(FinancialStatementColumn fsc : isv.getExpenseItems())
        {
            for(FinancialStatementLine fsl : fsc.getLines())
            {
                isv.getExpenseAccounts().add(fsl.getAccountName());
            }
        }
    }

    private void incrementFinancialStatementLine(FinancialStatementLine fsl, JournalEntryLine jel)
    {
        if("D".compareToIgnoreCase(jel.getTransactionType()) == 0)
        {
            if(fsl.getIsCredit() == false)
            {
                fsl.setTotalAmount(fsl.getTotalAmount().add(jel.getDebitAmount()));
            }
            else
            {
                fsl.setTotalAmount(fsl.getTotalAmount().subtract(jel.getDebitAmount()));
            }
        }
        else
        {
            if(fsl.getIsCredit() == false)
            {
                fsl.setTotalAmount(fsl.getTotalAmount().add(jel.getCreditAmount()));
            }
            else
            {
                fsl.setTotalAmount(fsl.getTotalAmount().subtract(jel.getCreditAmount()));
            }
        }
    }

    private void calculateBlockTotals(List<FinancialStatementColumn> fscAll)
    {
        for(FinancialStatementColumn fsc : fscAll)
        {
            //for(FinancialStatementLine fsl : fsc.getStatementLines().values())
            //{
                //fsc.setTotalAmount(fsc.getTotalAmount().add(fsl.getTotalAmount()));
            //}
        }
    }
}
