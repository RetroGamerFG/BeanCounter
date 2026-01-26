package com.itsretro.beancounter.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.FinancialStatementBlock;
import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.model.JournalEntryLine;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

@Component
public class IncomeStatementLogic 
{
    public void addJournalEntriesToIncomeStatementView(IncomeStatementView incomeStatementView, List<Object[]> queryResults, String type)
    {
        FinancialStatementBlock fsb = new FinancialStatementBlock();

        for(Object[] row : queryResults)
        {
            //JournalEntry je = (JournalEntry) row[0];
            JournalEntryLine jel = (JournalEntryLine) row[1];

            //confirm account/line was not previously added into the block, append if it was
            if(fsb.getStatementLines().containsKey(jel.getAccount().getAccountName()))
            {
                incrementFinancialStatementLine(
                    fsb.getStatementLines().get(jel.getAccount().getAccountName()),
                    jel
                );
            }
            else
            {
                FinancialStatementLine fsl = new FinancialStatementLine();
                
                //if the account is a revenue account, flag as credit.
                if("R".compareToIgnoreCase(jel.getAccount().getAccountType()) == 0)
                {
                    fsl.setIsCredit(true);
                }

                incrementFinancialStatementLine(fsl, jel);

                fsb.getStatementLines().put(jel.getAccount().getAccountName(), fsl);
            }
        }

        if("R".compareToIgnoreCase(type) == 0)
        {
            incomeStatementView.setRevenueItems(fsb);
        }
        else
        {
            incomeStatementView.setExpenseItems(fsb);
        }
    }

    public void calculateTotals(IncomeStatementView isv)
    {
        calculateBlockTotal(isv.getRevenueItems());
        calculateBlockTotal(isv.getExpenseItems());

        //profit/loss = revenue - expenses

        isv.setGrandTotalMTD(
            isv.getRevenueItems().getTotalAmountMTD().subtract(isv.getExpenseItems().getTotalAmountMTD())
        );

        isv.setGrandTotalQTD(
            isv.getRevenueItems().getTotalAmountQTD().subtract(isv.getExpenseItems().getTotalAmountQTD())
        );

        isv.setGrandTotalYTD(
            isv.getRevenueItems().getTotalAmountYTD().subtract(isv.getExpenseItems().getTotalAmountYTD())
        );
    }

    private void incrementFinancialStatementLine(FinancialStatementLine fsl, JournalEntryLine jel)
    {
        if("D".compareToIgnoreCase(jel.getTransactionType()) == 0)
        {
            if(fsl.getIsCredit() == false)
            {
                fsl.setTotalAmountMTD(fsl.getTotalAmountMTD().add(jel.getDebitAmount()));
                fsl.setTotalAmountQTD(fsl.getTotalAmountQTD().add(jel.getDebitAmount()));
                fsl.setTotalAmountYTD(fsl.getTotalAmountYTD().add(jel.getDebitAmount()));
            }
            else
            {
                fsl.setTotalAmountMTD(fsl.getTotalAmountMTD().subtract(jel.getDebitAmount()));
                fsl.setTotalAmountQTD(fsl.getTotalAmountQTD().subtract(jel.getDebitAmount()));
                fsl.setTotalAmountYTD(fsl.getTotalAmountYTD().subtract(jel.getDebitAmount()));
            }
        }
        else
        {
            if(fsl.getIsCredit() == false)
            {
                fsl.setTotalAmountMTD(fsl.getTotalAmountMTD().add(jel.getCreditAmount()));
                fsl.setTotalAmountQTD(fsl.getTotalAmountQTD().add(jel.getCreditAmount()));
                fsl.setTotalAmountYTD(fsl.getTotalAmountYTD().add(jel.getCreditAmount()));
            }
            else
            {
                fsl.setTotalAmountMTD(fsl.getTotalAmountMTD().subtract(jel.getCreditAmount()));
                fsl.setTotalAmountQTD(fsl.getTotalAmountQTD().subtract(jel.getCreditAmount()));
                fsl.setTotalAmountYTD(fsl.getTotalAmountYTD().subtract(jel.getCreditAmount()));
            }
        }
    }

    private void calculateBlockTotal(FinancialStatementBlock fsb)
    {
        for(FinancialStatementLine fsl : fsb.getStatementLines().values())
        {
            fsb.setTotalAmountMTD(fsb.getTotalAmountMTD().add(fsl.getTotalAmountMTD()));
            fsb.setTotalAmountQTD(fsb.getTotalAmountQTD().add(fsl.getTotalAmountQTD()));
            fsb.setTotalAmountYTD(fsb.getTotalAmountYTD().add(fsl.getTotalAmountYTD()));
        }
    }
}
