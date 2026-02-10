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
        if(quarter < 1 || quarter > 4)
        {
            return null;
        }

        int startMonth = getFiscalYearStartMonth();
        int monthValue = ((startMonth - 1 + ((quarter - 1) * 3)) % 12) + 1;

        return Month.of(monthValue);
    }

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

    public int getQuarterByMonth(Month month)
    {
        int startMonth = getFiscalYearStartMonth();
        int offset = (month.getValue() - startMonth + 12) % 12;

        return (offset / 3) + 1;
    }

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

    public int determineCurrentYearByQuarter(LocalDate startDate, int quarter)
    {
        return getQuarterStartDate(startDate, quarter).getYear();
    }

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

    public LocalDate getFiscalYearEndDate(LocalDate referenceDate)
    {
        return getFiscalYearStartDate(referenceDate).plusYears(1).minusDays(1);
    }

    public LocalDate getQuarterStartDate(LocalDate referenceDate, int quarter)
    {
        if(quarter < 1 || quarter > 4)
        {
            throw new IllegalArgumentException("Quarter must be 1-4");
        }

        return getFiscalYearStartDate(referenceDate).plusMonths((quarter - 1) * 3L);
    }

    public LocalDate getQuarterEndDate(LocalDate referenceDate, int quarter)
    {
        return getQuarterStartDate(referenceDate, quarter).plusMonths(3).minusDays(1);
    }

    //
    // Private Methods
    //

    private int getFiscalYearStartMonth()
    {
        return businessInfo.getIncorporationDate().getMonthValue();
    }
}
