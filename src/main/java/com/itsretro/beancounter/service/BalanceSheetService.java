package com.itsretro.beancounter.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsretro.beancounter.logic.BalanceSheetLogic;
import com.itsretro.beancounter.model.FinancialStatement;
import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.viewmodel.BalanceSheetView;

@Service
public class BalanceSheetService
{
    @Autowired
    private FinancialStatementService financialStatementService;

    @Autowired
    private BusinessInfoService businessInfoService;

    @Autowired
    private BalanceSheetLogic balanceSheetLogic;

    //
    // Service Methods
    //

    public BalanceSheetView getBalanceSheetView(FinancialStatement fs)
    {
        BalanceSheetView bsv = new BalanceSheetView();

        LocalDate endDate = businessInfoService.getEndDate(fs);

        //create the formatted date header based on the statement and range type.
        bsv.setDateRangeString(financialStatementService.createDateRangeString(
            fs.getRangeType(),
            endDate, 
            true
        ));

        //
        // Column Creation Operation
        //  The balance sheet will generally only show the range in which it was created and include all transactions from the incorporation date. 
        //  However, the user can specify to include the previous period for comparison. Future updates may include percentage change.
        //
        
        if("YTD".compareToIgnoreCase(fs.getRangeType()) == 0)
        {

        }
        else if("QTD".compareToIgnoreCase(fs.getRangeType()) == 0)
        {
            int quarter = businessInfoService.getQuarterByMonth(fs.getStartingDate().getMonth());

            if(fs.getIncludePreviousQuarter())
            {
                LocalDate previousQuarterEndDate = businessInfoService.getPreviousQuarterEndDate(endDate, quarter);
                createBalanceSheetColumn(bsv, fs, "Q" + quarter, previousQuarterEndDate);
            }

            createBalanceSheetColumn(bsv, fs, "Q" + quarter, businessInfoService.getQuarterEndDate(fs.getStartingDate(), quarter));
        }
        else
        {
            createBalanceSheetColumn(bsv, fs, fs.getStartingDate().getMonth().toString(), endDate);
        }

        //calculate totals
        balanceSheetLogic.calculateTotals(bsv);

        //extract all found accounts in each column (necessary for multiple columns where values may not be present)
        balanceSheetLogic.extractMatchedAccountNames(bsv);

        return bsv;
    }

    private void createBalanceSheetColumn(BalanceSheetView bsv, FinancialStatement fs, String columnLabel, LocalDate endDate)
    {
        int colIndex = bsv.getColumnCount();

        balanceSheetLogic.createColumn(bsv, columnLabel, colIndex);

        //fetch the incorporation date once to be used in repo calls
        LocalDate startDate = businessInfoService.getIncorporationDate();

        //call the journalEntry repository to get list of 'Asset' account journal entries
        List<FinancialStatementLine> queriedAssets = financialStatementService.fetchJournalEntries(
            startDate,
            endDate,
            fs.getGeneratedDate(), 
            "A"
        );

        //populate the fetched journal entries into the balance sheet view
        balanceSheetLogic.addLinesToColumn(bsv, queriedAssets, "A", false, colIndex);

        //call the journalEntry repository to get list of 'Liability' account journal entries
        List<FinancialStatementLine> queriedLiabilities = financialStatementService.fetchJournalEntries(
            startDate,
            endDate,
            fs.getGeneratedDate(), 
            "L"
        );

        //populate the fetched journal entries into the balance sheet view
        balanceSheetLogic.addLinesToColumn(bsv, queriedLiabilities, "E", false, colIndex);

        //call the journalEntry repository to get list of 'Liability' account journal entries
        List<FinancialStatementLine> queriedEquity = financialStatementService.fetchJournalEntries(
            startDate,
            endDate,
            fs.getGeneratedDate(), 
            "E"
        );

        //populate the fetched journal entries into the balance sheet view
        balanceSheetLogic.addLinesToColumn(bsv, queriedEquity, "E", false, colIndex);
    }
}
