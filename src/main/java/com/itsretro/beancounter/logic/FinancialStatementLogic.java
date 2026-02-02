package com.itsretro.beancounter.logic;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class FinancialStatementLogic 
{
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
