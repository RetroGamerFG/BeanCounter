//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// BusinessInfoService: a service class which holds a single linked instance of BusinessInfo.
//  The BusinessInfo class/member is retrieved through the "business.dat" file, and extracted by BusinessConfig.
//

package com.itsretro.beancounter.service;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itsretro.beancounter.logic.BusinessInfoLogic;
import com.itsretro.beancounter.model.BusinessInfo;
import com.itsretro.beancounter.model.FinancialStatement;

@Service
public class BusinessInfoService 
{
    @Autowired
    private BusinessInfoLogic businessInfoLogic;

    private final BusinessInfo businessInfo; //does not change

    //
    // Initializer(s)
    //

    public BusinessInfoService(BusinessInfo businessInfo)
    {
        this.businessInfo = businessInfo;
    }

    //
    // Getters & Setters
    //

    public BusinessInfo getBusinessInfo()
    {
        return this.businessInfo;
    }

    //
    // Service Methods
    //

    public LocalDate getIncorporationDate()
    {
        return this.businessInfo.getIncorporationDate();
    }

    public int getQuarterByMonth(Month month)
    {
        return businessInfoLogic.getQuarterByMonth(month);
    }

    public LocalDate getQuarterStartDate(LocalDate startDate, int quarter)
    {
        return businessInfoLogic.getQuarterStartDate(startDate, quarter);
    }

    public LocalDate getQuarterEndDate(LocalDate startDate, int quarter)
    {
        return businessInfoLogic.getQuarterEndDate(startDate, quarter);
    }

    public LocalDate getPreviousQuarterStartDate(LocalDate referenceDate, int currentQuarter)
    {
        return businessInfoLogic.getPreviousQuarterStartDate(referenceDate, currentQuarter);
    }

    public LocalDate getPreviousQuarterEndDate(LocalDate referenceDate, int currentQuarter)
    {
        return businessInfoLogic.getPreviousQuarterEndDate(referenceDate, currentQuarter);
    }

    public LocalDate getEndDate(FinancialStatement fs)
    {
        //determine the end date to query journal entries to. Should match the statement's range type (MTD, QTD, YTD).
        if("QTD".compareToIgnoreCase(fs.getRangeType()) == 0)
        {
            return determineEndDate(fs.getStartingDate(), true, false);
        }
        else if("YTD".compareToIgnoreCase(fs.getRangeType()) == 0)
        {
            return determineEndDate(fs.getStartingDate(), false, true);
        }
        else
        {
            return determineEndDate(fs.getStartingDate(), false, false);
        }
    }

    //
    // Private Methods
    //

    private LocalDate determineEndDate(LocalDate startDate, boolean useQTD, boolean useYTD)
    {
        return businessInfoLogic.determineEndDate(startDate, useQTD, useYTD);
    }
}
