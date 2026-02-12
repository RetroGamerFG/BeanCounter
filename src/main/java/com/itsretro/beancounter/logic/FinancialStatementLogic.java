//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// FinancialStatementLogic: a logic class for FinancialStatement and related classes.
// Includes methods related to class member assignment and computation of amounts.
//

package com.itsretro.beancounter.logic;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class FinancialStatementLogic 
{
    //createDateRangeString() - creates a formatted string display the statement date for a given financial statement type and range.
    //inputs -
        //rangeType - the range of the statement (i.e. MTD, QTD, YTD).
        //endingDate - the ending date of the statement.
        //dateOnly - enabled to ignore the intro text (i.e. "For the Month Ended...").
    //output - a string containing the formatted statement date.
    public String createDateRangeString(String rangeType, LocalDate endingDate, boolean dateOnly)
    {
        String output = "";

        if(dateOnly == false)
        {
            if("MTD".compareToIgnoreCase(rangeType) == 0)
            {
                output += "For the Month Ended ";
            }
            else if("QTD".compareToIgnoreCase(rangeType) == 0)
            {
                output += "For the Quarter Ended ";
            }
            else if("YTD".compareToIgnoreCase(rangeType) == 0)
            {
                output += "For the Year Ended ";
            }
        }

        String monthStr = endingDate.getMonth().toString();
        
        output += monthStr.charAt(0) + monthStr.substring(1).toLowerCase() + " "
            + endingDate.getDayOfMonth() + ", "
            + endingDate.getYear();

        return output;
    }
}
