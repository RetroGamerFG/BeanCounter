//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// Business - the base class for business. Contains user/business information that is carried over to specific forms.

package Business;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Business
{
    private String businessName;
    private Month startingMonth; //month business started
    private Year startingYear; //year business started

    private LinkedHashMap<Month, String> quarterMonths;
    
    //need to figure out more about what this class would entail.

    public Business()
    {
        businessName = "Placeholder";
        startingMonth = Month.JANUARY;
        startingYear = Year.of(1969);
        determineQuarterMonths();
    }

    public Business(String businessName, Month startingMonth, Year startingYear)
    {
        this.businessName = businessName;
        this.startingMonth = startingMonth;
        this.startingYear = startingYear;
        determineQuarterMonths();
    }

    public String getBusinessName()
    {
        return businessName;
    }

    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }

    public Month getStartingMonth()
    {
        return startingMonth;
    }

    public void setStartingMonth(Month startingMonth)
    {
        this.startingMonth = startingMonth;
    }

    public Year getStartingYear()
    {
        return startingYear;
    }

    public void setStartingYear(Year startingYear)
    {
        this.startingYear = startingYear;
    }

//
// Additional functions
//

    //determineQuarterMonths() - creates the quarterMonths() Map to assign a quarter value to a month.
    private void determineQuarterMonths()
    {
        this.quarterMonths = new LinkedHashMap<>();

        int counter = 0;
        int pos = startingMonth.getValue();

        while(counter < 12)
        {
            if(pos > 12)
            {
                pos = 1;
            }

            if(counter >= 0 && counter <= 2)
            {
                quarterMonths.put(Month.of(pos), "Q1");
            }

            if(counter >= 3 && counter <= 5)
            {
                quarterMonths.put(Month.of(pos), "Q2");
            }

            if(counter >= 6 && counter <= 8)
            {
                quarterMonths.put(Month.of(pos), "Q3");
            }

            if(counter >= 9 && counter <= 11)
            {
                quarterMonths.put(Month.of(pos), "Q4");
            }

            pos++;
            counter++;
        }
    }

    public String getQuarterByMonth(Month inMonth)
    {
        return quarterMonths.get(inMonth);
    }

    private String getNextQuarter(String inQuarter)
    {
        if(inQuarter.compareTo("Q1") == 0)
        {
            return "Q2";
        }

        if(inQuarter.compareTo("Q2") == 0)
        {
            return "Q3";
        }

        if(inQuarter.compareTo("Q3") == 0)
        {
            return "Q4";
        }

        if(inQuarter.compareTo("Q4") == 0)
        {
            return "Q1";
        }

        return null;
    }

    public Month getQuarterStartMonth(String inQuarter)
    {
        ArrayList<Month> foundMonths = new ArrayList<>();
        
        //for each item in the Map, find the first instance of the next quarter
        for (Map.Entry<Month, String> entry : quarterMonths.entrySet())
        {
            if (entry.getValue().equals(inQuarter))
            {
                foundMonths.add(entry.getKey());
            }
        }

        if(foundMonths.size() > 0)
        {
            return foundMonths.getFirst();
        }

        return null;
    }

    public Month getQuarterEndMonth(String inQuarter)
    {
        String searchVal = getNextQuarter(inQuarter);

        ArrayList<Month> foundMonths = new ArrayList<>();
        
        //for each item in the Map, find the first instance of the next quarter
        for (Map.Entry<Month, String> entry : quarterMonths.entrySet())
        {
            if (entry.getValue().equals(searchVal))
            {
                foundMonths.add(entry.getKey());
            }
        }

        if(foundMonths.size() > 0)
        {
            return foundMonths.getLast();
        }

        return null;
    }
}
