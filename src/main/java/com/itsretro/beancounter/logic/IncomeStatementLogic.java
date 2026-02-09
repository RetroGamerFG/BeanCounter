package com.itsretro.beancounter.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.model.IncomeStatementColumn;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

@Component
public class IncomeStatementLogic 
{
    public void createColumn(IncomeStatementView isv, String columnLabel, int columnIndex)
    {
        IncomeStatementColumn isc = new IncomeStatementColumn();

        isc.setColumnLabel(columnLabel);
        isc.setColumnIndex(columnIndex);

        isv.getColumns().add(isc);
        isv.incrementColumnCount();
    }

    public void addJournalEntriesToColumn(IncomeStatementView isv, List<FinancialStatementLine> queryResults, String type, int colIndex)
    {
        for(FinancialStatementLine fsl : queryResults)
        {
            if("R".compareToIgnoreCase(type) == 0)
            {
                isv.getColumns().get(colIndex).getRevenueLines().put(fsl.getAccountName(), fsl);
            }
            else
            {
                isv.getColumns().get(colIndex).getExpenseLines().put(fsl.getAccountName(), fsl);
            }
        }
    }

    public void calculateTotals(IncomeStatementView isv)
    {
        for(int c = 0; c < isv.getColumnCount(); c++)
        {
            calculateIncomeStatementColumn(isv.getColumns().get(c));
        }
    }

    public void extractMatchedAccountNames(IncomeStatementView isv)
    {
        for(int c = 0; c < isv.getColumnCount(); c++)
        {
            for(String accountName : isv.getColumns().get(c).getRevenueLines().keySet())
            {
                isv.getRevenueAccounts().add(accountName);
            }

            for(String accountName : isv.getColumns().get(c).getExpenseLines().keySet())
            {
                isv.getExpenseAccounts().add(accountName);
            }
        }
    }

    private void calculateIncomeStatementColumn(IncomeStatementColumn isc)
    {
        //revenue is reported as credits, so reverse amount when calculating total
        for(FinancialStatementLine fsl : isc.getRevenueLines().values())
        {
            if(fsl.getIsCredit() == false)
            {
                isc.setTotalRevenue(isc.getTotalRevenue().subtract(fsl.getTotalAmount()));
            }
            else
            {
                isc.setTotalRevenue(isc.getTotalRevenue().add(fsl.getTotalAmount()));
            }
        }

        for(FinancialStatementLine fsl : isc.getExpenseLines().values())
        {
            if(fsl.getIsCredit() == false)
            {
                isc.setTotalExpense(isc.getTotalExpense().add(fsl.getTotalAmount()));
            }
            else
            {
                isc.setTotalExpense(isc.getTotalExpense().subtract(fsl.getTotalAmount()));
            }
        }

        //net income = revenue - expenses
        isc.setNetIncome(isc.getTotalRevenue().subtract(isc.getTotalExpense()));
    }
}
