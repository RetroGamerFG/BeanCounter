//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// IncomeStatementLogic: a logic class for IncomeStatement and related classes.
//  Includes methods related to class member assignment and computation of amounts.
//

package com.itsretro.beancounter.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.model.IncomeStatementColumn;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

@Component
public class IncomeStatementLogic 
{
    //
    // Public Methods
    //

    //createColumn() - creates a column formatted for the usage of an income statement.
    //inputs -
        //isv: the IncomeStatementView to add the column to.
        //columnLabel: an identifier for the column, such as a month or year.
        //columnIndex: the index for the column.
    //output - none; creates and appends the column to the view.
    public void createColumn(IncomeStatementView isv, String columnLabel, int columnIndex)
    {
        IncomeStatementColumn isc = new IncomeStatementColumn();

        isc.setColumnLabel(columnLabel);
        isc.setColumnIndex(columnIndex);

        isv.getColumns().add(isc);
        isv.incrementColumnCount();
    }

    //addLinesToColumn() - given a list of FinancialStatementLine instances with summarized amounts, appends results to an indexed column.
    //inputs -
        //isv: the IncomeStatementView instance that contains the column.
        //queryResults: a list of FinancialStatementLine instances to append.
        //type: specifies "R" for revenue lines or "E" for expense lines.
        //colIndex: the index of the column to add the lines to.
    //output - none; updates the column to include line values.
    public void addLinesToColumn(IncomeStatementView isv, List<FinancialStatementLine> queryResults, String type, int colIndex)
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

    //calculateTotals() - iterates each column stored in the view and calculates the totals.
    //inputs - isv: the IncomeStatementView instance.
    //output - none; updates values within the columns stored.
    public void calculateTotals(IncomeStatementView isv)
    {
        for(int c = 0; c < isv.getColumnCount(); c++)
        {
            calculateIncomeStatementColumn(isv.getColumns().get(c));
        }
    }

    //extractMatchedAccountNames() - pulls all accounts contained within columns, and stores as individual sets. Used to handle accounts with null
        //values or are added later for a given fiscal year.
    //inputs - isv: the IncomeStatementView instance.
    //output - none; updates values within the view.
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

    //
    // Private Methods
    //

    //calculateIncomeStatementColumn() - iterates through revenue and expense lines and performs calculations to find net income.
    //inputs - isc: the IncomeStatementColumn to perform the calculation on.
    //output - none; updates values within the column.
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
