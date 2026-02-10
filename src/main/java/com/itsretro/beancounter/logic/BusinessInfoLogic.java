package com.itsretro.beancounter.logic;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.BusinessInfo;

@Component
public class BusinessInfoLogic 
{
    private final BusinessInfo businessInfo;

    //
    // Initializer(s)
    //

    public BusinessInfoLogic(BusinessInfo businessInfo)
    {
        this.businessInfo = businessInfo;
    }

    //
    // Public Methods
    //

    public Month getQuarterStartMonth(int quarter)
    {
        int monthValue = businessInfo.getIncorporationDate().getMonthValue();

        System.out.println();

        //example logic if the incorporation date was in January
            //1st quarter: January - March
            //2nd quarter: April - June
            //3rd quarter: July - September
            //4th quarter: October - December

        switch(quarter)
        {
            case 1 -> monthValue += 0;
            case 2 -> monthValue += 3;
            case 3 -> monthValue += 6;
            case 4 -> monthValue += 9;

            default -> { return null;}
        }

        //force rollback if value exceeds 12 months
        if(monthValue > 12)
        {
            monthValue -= 12;
        }

        return Month.of(monthValue);
    }

    //VALID
    public Month getQuarterEndMonth(int quarter)
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

    //VALID
    public int getQuarterByMonth(Month month)
    {
        int businessMonthVal = businessInfo.getIncorporationDate().getMonthValue();

        for(int c = 0; c < 4; c++)
        {
            int monthPos = businessMonthVal + (c * 3);

            if(monthPos >= 12)
            {
                monthPos -= 12;
            }

            for(int m = 0; m < 3; m++)
            {
                if((monthPos + m) == month.getValue())
                {
                    return c + 1;
                }
            }
        }

        return -1;
    }

    public LocalDate determineEndDate(LocalDate startDate, boolean useQTD, boolean useYTD)
    {
        int monthPos = startDate.getMonthValue();
        int yearPos = startDate.getYear();

        //mtd is the current month contained in startMonth, only need to get ending day.
        //qtd and ytd require determining the current quarter and/or current fiscal year.

        if(useQTD)
        {
            //determine the current quarter of the financial statement's starting date
            int matchedQuarter = getQuarterByMonth(startDate.getMonth());

            //increment the month-end position based on the quarter's ending month
            monthPos = getQuarterEndMonth(matchedQuarter).getValue();

            System.out.println();
        }
        else if(useYTD)
        {
            //determine the 4th quarter's month-end
            monthPos = getQuarterEndMonth(4).getValue();

            //if the last month of the fiscal year is less than the starting month, increment the year
            if(monthPos < startDate.getMonthValue())
            {
                yearPos++;
            }
            
            System.out.println();
        }

        //return built result, rolling forward to last day of month
        return LocalDate.of(yearPos, monthPos, 1).with(TemporalAdjusters.lastDayOfMonth());
    }  
}
