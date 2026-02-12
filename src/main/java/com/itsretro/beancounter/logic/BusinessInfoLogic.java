//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// BusinessInfoLogic: a logic class for BusinessInfo and related classes.
//  Includes methods related to class member assignment and computation of amounts.
//

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

    //getQuarterStartMonth() - based on the incorporation date of businessInfo, returns the starting month of a passed quarter.
    //inputs - quarter: an int representing the fiscal year's quarter.
    //output - a Month instance initialized to the matched quarter.
    public Month getQuarterStartMonth(int quarter)
    {
        if(quarter < 1 || quarter > 4)
        {
            return null;
        }

        int startMonth = getFiscalYearStartMonth();
        int monthValue = ((startMonth - 1 + ((quarter - 1) * 3)) % 12) + 1;

        return Month.of(monthValue);
    }

    //getQuarterStartMonth() - based on the incorporation date of businessInfo, returns the ending month of a passed quarter.
    //inputs - quarter: an int representing the fiscal year's quarter.
    //output - a Month instance initialized to the matched quarter.
    public Month getQuarterEndMonth(int quarter)
    {
        if(quarter < 1 || quarter > 4)
        {
            return null;
        }

        int startMonth = getFiscalYearStartMonth();
        int monthValue = ((startMonth - 1 + ((quarter * 3) - 1)) % 12) + 1;

        return Month.of(monthValue);
    }

    //getQuarterByMonth() - based on the incorporation date of businessInfo, returns the quarter number for a given month.
    //inputs - month: a Month instance to search with.
    //output - an int representing the matched quarter, 1-4.
    public int getQuarterByMonth(Month month)
    {
        int startMonth = getFiscalYearStartMonth();
        int offset = (month.getValue() - startMonth + 12) % 12;

        return (offset / 3) + 1;
    }

    //determineEndDate() - based on the incorporation date of businessInfo, determines the ending date based on the range type.
    //inputs -
        //startDate: a LocalDate instance representing the starting date.
        //useQTD: a boolean flagged to calculate based on quarter-to-date.
        //useYTD: a boolean flagged to calculate based on year-to-date.
    //output - a LocalDate initialized to the ending date based on given parameters.
    public LocalDate determineEndDate(LocalDate startDate, boolean useQTD, boolean useYTD)
    {
        if(useQTD)
        {
            int matchedQuarter = getQuarterByMonth(startDate.getMonth());

            return getQuarterEndDate(startDate, matchedQuarter);
        }
        else if(useYTD)
        {
            return getFiscalYearEndDate(startDate);
        }

        return startDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    //getFiscalYearStartDate() - based on the incorporation date of businessInfo, determines the starting year for offset quarters.
    //inputs - referenceDate: a LocalDate instance to determine the fiscal year.
    //output - a LocalDate instance based on the fiscal year's start date.
    public LocalDate getFiscalYearStartDate(LocalDate referenceDate)
    {
        int startMonth = getFiscalYearStartMonth();
        int startYear = referenceDate.getYear();

        if(referenceDate.getMonthValue() < startMonth)
        {
            startYear--;
        }

        return LocalDate.of(startYear, startMonth, 1);
    }

    //getFiscalYearEndDate() - based on the incorporation date of businessInfo, determines the ending year for offset quarters.
    //inputs - referenceDate: a LocalDate instance to determine the fiscal year.
    //output - a LocalDate instance based on the fiscal year's end date.
    public LocalDate getFiscalYearEndDate(LocalDate referenceDate)
    {
        return getFiscalYearStartDate(referenceDate).plusYears(1).minusDays(1);
    }

    //getQuarterStartDate() - based on the incorporation date of businessInfo, determines the starting date for a given quarter.
    //inputs - 
        //referenceDate: a LocalDate instance to determine the year.
        //quarter: an int representing the quarter.
    //output - a LocalDate instance based on the quater's start date.
    public LocalDate getQuarterStartDate(LocalDate referenceDate, int quarter)
    {
        if(quarter < 1 || quarter > 4)
        {
            throw new IllegalArgumentException("Quarter must be 1-4");
        }

        return getFiscalYearStartDate(referenceDate).plusMonths((quarter - 1) * 3L);
    }

    //getQuarterEndDate() - based on the incorporation date of businessInfo, determines the ending date for a given quarter.
    //inputs - 
        //referenceDate: a LocalDate instance to determine the year.
        //quarter: an int representing the quarter.
    //output - a LocalDate instance based on the quater's end date.
    public LocalDate getQuarterEndDate(LocalDate referenceDate, int quarter)
    {
        return getQuarterStartDate(referenceDate, quarter).plusMonths(3).minusDays(1);
    }

    //
    // Private Methods
    //

    //getFiscalYearStartMonth() - a helper function to retrieve businessInfo's month value of the incorporation date.
    //inputs - none.
    //output - an int representing the month value.
    private int getFiscalYearStartMonth()
    {
        return businessInfo.getIncorporationDate().getMonthValue();
    }
}
