package com.itsretro.beancounter.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsretro.beancounter.logic.IncomeStatementLogic;
import com.itsretro.beancounter.model.FinancialStatement;
import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.viewmodel.IncomeStatementView;

@Service
public class IncomeStatementService
{
    @Autowired
    private FinancialStatementService financialStatementService;

    @Autowired
    private BusinessInfoService businessInfoService;

    @Autowired
    private IncomeStatementLogic incomeStatementLogic;

    //
    // Service Methods
    //

    public IncomeStatementView getIncomeStatementView(FinancialStatement fs)
    {
        IncomeStatementView isv = new IncomeStatementView();

        LocalDate endDate = businessInfoService.getEndDate(fs);

        //create the formatted date header based on the statement and range type.
        isv.setDateRangeString(financialStatementService.createDateRangeString(
            fs.getRangeType(),
            endDate, 
            false
        ));

        //
        // Column Creation Operation
        //  The income statement summarizes based on the user specification. The incorporation date determines the start and end dates for
        //  quarters and fiscal years. Users can include additional columns when the statement type is QTD or YTD.

        if("YTD".compareToIgnoreCase(fs.getRangeType()) == 0) //Year-To-Date, the sum of 12 months in the fiscal year.
        {
            if(fs.getIncludeAllMonths() && fs.getIncludeAllQuarters()) //includes 12 months, 4 quarters, and the fiscal year total.
            {
                createColumnsForMonthRange(isv, fs, 1);
                createColumnForQuarter(isv, fs, 1);

                createColumnsForMonthRange(isv, fs, 2);
                createColumnForQuarter(isv, fs, 2);

                createColumnsForMonthRange(isv, fs, 3);
                createColumnForQuarter(isv, fs, 3);

                createColumnsForMonthRange(isv, fs, 4);
                createColumnForQuarter(isv, fs, 4);
            }
            else if (fs.getIncludeAllMonths()) //includes 12 months and the fiscal year total.
            {
                createColumnsForMonthRange(isv, fs, 1);
                createColumnsForMonthRange(isv, fs, 2);
                createColumnsForMonthRange(isv, fs, 3);
                createColumnsForMonthRange(isv, fs, 4);
            }
            else if (fs.getIncludeAllQuarters()) //includes 4 quarters and fiscal year total.
            {
                createColumnForQuarter(isv, fs, 1);
                createColumnForQuarter(isv, fs, 2);
                createColumnForQuarter(isv, fs, 3);
                createColumnForQuarter(isv, fs, 4);
                
            }

            createColumnForYear(isv, fs);
        }
        else if ("QTD".compareToIgnoreCase(fs.getRangeType()) == 0) //Quarter-To-Date, the sum of 3 months which represent a quarter.
        {
            if (fs.getIncludeAllMonths()) //includes 3 months and the quarter total.
            {
                createColumnsForMonthRange(isv, fs, businessInfoService.getQuarterByMonth(endDate.getMonth()));
            }

            createColumnForQuarter(isv, fs, businessInfoService.getQuarterByMonth(fs.getStartingDate().getMonth()));
        }
        else //Month-To-Date, the sum of a single month
        {
            createIncomeStatementColumn(isv, fs, fs.getStartingDate().getMonth().toString(), fs.getStartingDate(), endDate);
        }

        //calculate totals
        incomeStatementLogic.calculateTotals(isv);

        //extract all found accounts in each column (necessary for multiple columns where values may not be present)
        incomeStatementLogic.extractMatchedAccountNames(isv);

        return isv;
    }

    //
    // Private Methods
    //

    private void createIncomeStatementColumn(IncomeStatementView isv, FinancialStatement fs, String columnLabel, LocalDate startDate, LocalDate endDate)
    {
        int colIndex = isv.getColumnCount();

        incomeStatementLogic.createColumn(isv, columnLabel, colIndex);

        //call the journalEntry repository to get list of 'Revenue' account journal entries
        List<FinancialStatementLine> queriedRev = financialStatementService.fetchJournalEntries(
            startDate,
            endDate,
            fs.getGeneratedDate(), 
            "R"
        );

        //populate the fetched journal entries into the income statement view
        incomeStatementLogic.addLinesToColumn(isv, queriedRev, "R", colIndex);

        //call the journalEntry repository to get list of "Expense" account journal entries
        List<FinancialStatementLine> queriedExp = financialStatementService.fetchJournalEntries(
            startDate,
            endDate,
            fs.getGeneratedDate(), 
            "X"
        );

        //populate the fetched journal entries into the income statement view
        incomeStatementLogic.addLinesToColumn(isv, queriedExp, "X", colIndex);
    }

    private void createColumnsForMonthRange(IncomeStatementView isv, FinancialStatement fs, int quarter)
    {
        LocalDate quarterStart = businessInfoService.getQuarterStartDate(fs.getStartingDate(), quarter);

        for(int m = 0; m < 3; m++)
        {
            LocalDate currentStart = quarterStart.plusMonths(m);
            LocalDate currentEnd = currentStart.with(TemporalAdjusters.lastDayOfMonth());

            createIncomeStatementColumn(isv, fs, currentStart.getMonth().toString(), currentStart, currentEnd);
        }
    }

    private void createColumnForQuarter(IncomeStatementView isv, FinancialStatement fs, int quarter)
    {
        LocalDate quarterStartDate = businessInfoService.getQuarterStartDate(fs.getStartingDate(), quarter);
        LocalDate quarterEndDate = businessInfoService.getQuarterEndDate(fs.getStartingDate(), quarter);

        createIncomeStatementColumn(isv, fs, "Q" + quarter, quarterStartDate, quarterEndDate);
    }

    private void createColumnForYear(IncomeStatementView isv, FinancialStatement fs)
    {
        LocalDate fiscalYearStartDate = businessInfoService.getQuarterStartDate(fs.getStartingDate(), 1);
        LocalDate fiscalYearEndDate = businessInfoService.getQuarterEndDate(fs.getStartingDate(), 4);

        createIncomeStatementColumn(isv, fs, String.valueOf(fiscalYearStartDate.getYear()), fiscalYearStartDate, fiscalYearEndDate);
    }
}
