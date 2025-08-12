//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// DatedStatement - used for statements whose transactions are based on a timed range (monthly, quarterly, yearly). This includes the income statement,
// balance sheet, and statement of retained earnings.


package Forms.Statement;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;

public class DatedStatement extends Statement
{
    String statementType; //"Income Statement", "Balance Sheet", "Statement of Retained Earnings"
    String statementRange; //"Month", "Quarter", "Year"
    String statementPeriodDesc; //only set for a month or a quarter
    Year fiscalYear;

    public DatedStatement()
    {
        statementType = null;
        statementRange = null;
        statementPeriodDesc = null;
        fiscalYear = null;
    }

    public String getStatementType()
    {
        return statementType;
    }

    public void setStatementType(String statementType)
    {
        this.statementType = statementType;
    }

    public String getStatementRange()
    {
        return statementRange;
    }

    //setStatementRange part of determineDateRange()

    public String getStatementPeriodDesc()
    {
        return statementPeriodDesc;
    }

    public void setStatementPeriodDesc(String statementPeriodDesc)
    {
        this.statementPeriodDesc = statementPeriodDesc;
    }

    public Year getFiscalYear()
    {
        return fiscalYear;
    }

    //setFiscalYear part of determineDateRange()

//
// Print Functions
//

    public String printDateHeader()
    {
        if(statementType.compareTo("Income Statement") == 0 || statementType.compareTo("Statement of Retained Earnings") == 0)
        {
            String output = "For " + statementRange + " ended " + printEndDate();
            return output;
        }
        else
        {
            String output = printEndDate();
            return output;
        }
    }

//
// Additional Functions
//

    //
    //Month - no further calculations, get last day of selected month
    public void setDateRangeMonth(Month selectedMonth, Year selectedYear, boolean rollForwardYear)
    {
        int selectedMonthValue = selectedMonth.getValue();
        int selectedYearValue = selectedYear.getValue();
        
        if(rollForwardYear)
        {
            selectedYearValue++;
        }

        fiscalYear = selectedYear;

        //Set the starting date to the first day of the specified month and year.
        this.setStartDate(LocalDate.of(selectedYearValue, selectedMonthValue, 1));

        this.statementRange = "Month";

        //YearMonth used to determine the last day of the determined month and year.
        YearMonth yearMonth = YearMonth.of(selectedYearValue, selectedMonthValue);
        this.setEndDate(yearMonth.atEndOfMonth());
    }

    //
    //Quarter - set end date ahead 3 months from selected month
    public void setDateRangeQuarter(Month selectedMonth, Year selectedYear, boolean rollForwardYear)
    {
        int selectedMonthValue = selectedMonth.getValue();
        int selectedYearValue = selectedYear.getValue();

        if(rollForwardYear)
        {
            selectedYearValue++;
        }

        fiscalYear = selectedYear;

        //Set the starting date to the first day of the specified month and year.
        this.setStartDate(LocalDate.of(selectedYearValue, selectedMonthValue, 1));

        //if after incrementing by 2 the month is greater than 12, subtract 12, then increment the year.
        selectedMonthValue = selectedMonthValue + 2;

        if(selectedMonthValue > 12)
        {
            selectedMonthValue = selectedMonthValue - 12;
            selectedYearValue++;
        }

        //assign statementRange member
        this.statementRange = "Quarter";

        //YearMonth used to determine the last day of the determined month and year.
        YearMonth yearMonth = YearMonth.of(selectedYearValue, selectedMonthValue);
        this.setEndDate(yearMonth.atEndOfMonth());
    }

    //
    //Year - set end date tweleve months ahead of the selected month and increment year.
    public void setDateRangeYear(Month selectedMonth, Year selectedYear)
    {
        int selectedMonthValue = selectedMonth.getValue();
        int selectedYearValue = selectedYear.getValue();

        fiscalYear = selectedYear;

        //Set the starting date to the first day of the specified month and year.
        this.setStartDate(LocalDate.of(selectedYearValue, selectedMonthValue, 1));

        //if the selected month is 1, roll back to 12, else decrement as usual.
        if(selectedMonthValue == 1)
        {
            selectedMonthValue = 12;
        }
        else
        {
            selectedMonthValue--;
        }

        //increment the year value by 1
        selectedYearValue++;

        //assign statementRange member
        this.statementRange = "Year";

        //YearMonth used to determine the last day of the determined month and year.
        YearMonth yearMonth = YearMonth.of(selectedYearValue, selectedMonthValue);
        this.setEndDate(yearMonth.atEndOfMonth());
    }
}
