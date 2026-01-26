package com.itsretro.beancounter.logic;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.BusinessInfo;
import com.itsretro.beancounter.model.FinancialStatement;

@Component
public class BusinessInfoLogic 
{
    public Month getQuarterStartMonth(BusinessInfo businessInfo, int quarter)
    {
        int monthValue = businessInfo.getIncorporationDate().getMonthValue();

        //example logic if the incorporation date was in January
            //1st quarter: January - March
            //2nd quarter: April - June
            //3rd quarter: July - September
            //4th quarter: October - December

        switch(quarter)
        {
            case 1 -> monthValue += 0;
            case 2 -> monthValue += 2;
            case 3 -> monthValue += 5;
            case 4 -> monthValue += 8;

            default -> { return null;}
        }

        //force rollback if value exceeds 12 months
        if(monthValue > 12)
        {
            monthValue -= 12;
        }

        return Month.of(monthValue);
    }

    public Month getQuarterEndMonth(BusinessInfo businessInfo, int quarter)
    {
        int monthValue = businessInfo.getIncorporationDate().getMonthValue();

        //example logic if the incorporation date was in January
            //1st quarter: January - March
            //2nd quarter: April - June
            //3rd quarter: July - September
            //4th quarter: October - December

        switch(quarter)
        {
            case 1 -> monthValue += 2;
            case 2 -> monthValue += 5;
            case 3 -> monthValue += 8;
            case 4 -> monthValue += 11;

            default -> { return null;}
        }

        //force rollback if value exceeds 12 months
        if(monthValue > 12)
        {
            monthValue -= 12;
        }

        return Month.of(monthValue);
    }

    public int getQuarterByMonth(BusinessInfo businessInfo, Month month)
    {
        for(int c = 1; c <= 4; c++)
        {
            int monthPos = businessInfo.getIncorporationDate().getMonthValue() * c;

            if(monthPos >= 12)
            {
                monthPos -= 12;
            }

            for(int m = 1; m <= 3; m++)
            {
                if((monthPos + m) == month.getValue())
                {
                    return c;
                }
            }
        }

        return -1;
    }

    public LocalDate determineEndDate(BusinessInfo businessInfo, FinancialStatement financialStatement)
    {
        int monthPos = financialStatement.getStartingDate().getMonthValue();
        int yearPos = financialStatement.getStartingDate().getYear();

        //mtd is the current month contained in startMonth, only need to get ending day.
        //qtd and ytd require determining the current quarter and/or current fiscal year.

        if("QTD".compareToIgnoreCase(financialStatement.getStatementType()) == 0)
        {
            //determine the current quarter of the financial statement's starting date
            int matchedQuarter = getQuarterByMonth(businessInfo, financialStatement.getStartingDate().getMonth());

            //increment the month-end position based on the quarter's ending month
            monthPos += getQuarterEndMonth(businessInfo, matchedQuarter).getValue();
        }
        else if("YTD".compareToIgnoreCase(financialStatement.getStatementType()) == 0)
        {
            //determine the 4th quarter's month-end
            int fourthQuarter = getQuarterEndMonth(businessInfo, 4).getValue();

            //increment the month-end position based on the fourth quarter's ending month
            monthPos += getQuarterEndMonth(businessInfo, fourthQuarter).getValue();
        }

        //if the calculated month-end is greater than 12 (past December), roll back and increment year
        if(monthPos > 12)
        {
            monthPos -= 12;
            yearPos += 1;
        }

        //return built result, rolling forward to last day of month
        return LocalDate.of(yearPos, monthPos, 1).with(TemporalAdjusters.lastDayOfMonth());
    }  
}
