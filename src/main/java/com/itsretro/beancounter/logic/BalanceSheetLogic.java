package com.itsretro.beancounter.logic;

import java.util.List;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.BalanceSheetColumn;
import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.viewmodel.BalanceSheetView;

@Component
public class BalanceSheetLogic
{
    //
    // Public Methods
    //

    public void createColumn(BalanceSheetView bsv, String columnLabel, int columnIndex)
    {
        BalanceSheetColumn bsc = new BalanceSheetColumn();

        bsc.setColumnLabel(columnLabel);
        bsc.setColumnIndex(columnIndex);

        bsv.getColumns().add(bsc);
        bsv.incrementColumnCount();
    }

    public void addLinesToColumn(BalanceSheetView bsv, List<FinancialStatementLine> queryResults, String type, boolean isCurrent, int colIndex)
    {
        for(FinancialStatementLine fsl : queryResults)
        {
            if("A".compareToIgnoreCase(type) == 0)
            {
                if(isCurrent)
                {
                    bsv.getColumns().get(colIndex).getCurrentAssetLines().put(fsl.getAccountName(), fsl);
                }
                else
                {
                    bsv.getColumns().get(colIndex).getLongTermAssetLines().put(fsl.getAccountName(), fsl);
                }
            }
            else if ("L".compareToIgnoreCase(type) == 0)
            {
                if(isCurrent)
                {
                    bsv.getColumns().get(colIndex).getCurrentLiabilityLines().put(fsl.getAccountName(), fsl);
                }
                else
                {
                    bsv.getColumns().get(colIndex).getLongTermLiabilityLines().put(fsl.getAccountName(), fsl);
                }
            }
            else
            {
                bsv.getColumns().get(colIndex).getEquityLines().put(fsl.getAccountName(), fsl);
            }
        }
    }

    public void calculateTotals(BalanceSheetView bsv)
    {
        for(int c = 0; c < bsv.getColumnCount(); c++)
        {
            calculateColumn(bsv.getColumns().get(c));
        }
    }

    public void extractMatchedAccountNames(BalanceSheetView bsv)
    {
        for(int c = 0; c < bsv.getColumnCount(); c++)
        {
            for(String accountName : bsv.getColumns().get(c).getCurrentAssetLines().keySet())
            {
                bsv.getCurrentAssetAccounts().add(accountName);
            }

            for(String accountName : bsv.getColumns().get(c).getLongTermAssetLines().keySet())
            {
                bsv.getLongTermAssetAccounts().add(accountName);
            }

            for(String accountName : bsv.getColumns().get(c).getCurrentLiabilityLines().keySet())
            {
                bsv.getCurrentLiabilityAccounts().add(accountName);
            }

            for(String accountName : bsv.getColumns().get(c).getLongTermLiabilityLines().keySet())
            {
                bsv.getLongTermLiabilityAccounts().add(accountName);
            }

            for(String accountName : bsv.getColumns().get(c).getEquityLines().keySet())
            {
                bsv.getEquityAccounts().add(accountName);
            }
        }
    }

    //
    // Private Methods
    //

    private void calculateColumn(BalanceSheetColumn bsc)
    {
        for(FinancialStatementLine fsl : bsc.getCurrentAssetLines().values())
        {
            if(fsl.getIsCredit() == false)
            {
                bsc.setTotalCurrentAssets(bsc.getTotalCurrentAssets().add(fsl.getTotalAmount()));
            }
            else
            {
                bsc.setTotalCurrentAssets(bsc.getTotalCurrentAssets().subtract(fsl.getTotalAmount()));
            }
        }

        for(FinancialStatementLine fsl : bsc.getLongTermAssetLines().values())
        {
            if(fsl.getIsCredit() == false)
            {
                bsc.setTotalLongTermAssets(bsc.getTotalLongTermAssets().add(fsl.getTotalAmount()));
            }
            else
            {
                bsc.setTotalLongTermAssets(bsc.getTotalLongTermAssets().subtract(fsl.getTotalAmount()));
            }

            System.out.println();
        }

        for(FinancialStatementLine fsl : bsc.getCurrentLiabilityLines().values())
        {
            if(fsl.getIsCredit() == false)
            {
                bsc.setTotalCurrentLiability(bsc.getTotalCurrentLiability().add(fsl.getTotalAmount()));
            }
            else
            {
                bsc.setTotalCurrentLiability(bsc.getTotalCurrentLiability().subtract(fsl.getTotalAmount()));
            }
        }

        for(FinancialStatementLine fsl : bsc.getLongTermLiabilityLines().values())
        {
            if(fsl.getIsCredit() == false)
            {
                bsc.setTotalLongTermLiability(bsc.getTotalLongTermLiability().add(fsl.getTotalAmount()));
            }
            else
            {
                bsc.setTotalLongTermLiability(bsc.getTotalLongTermLiability().subtract(fsl.getTotalAmount()));
            }
        }

        for(FinancialStatementLine fsl : bsc.getEquityLines().values())
        {
            if(fsl.getIsCredit() == false)
            {
                bsc.setTotalEquity(bsc.getTotalEquity().add(fsl.getTotalAmount()));
            }
            else
            {
                bsc.setTotalEquity(bsc.getTotalEquity().subtract(fsl.getTotalAmount()));
            }
        }

        //summarize total assets and total liabilities with equity
        bsc.setTotalAssets(bsc.getTotalCurrentAssets().add(bsc.getTotalLongTermAssets()));
        bsc.setTotalLiabilityAndEquity(bsc.getTotalCurrentLiability().add(bsc.getTotalLongTermLiability().add(bsc.getTotalEquity())));

        System.out.println();
    }
}
