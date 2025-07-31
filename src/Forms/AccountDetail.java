//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// AccountDetail - the base class for account details. Takes user inputs and creates referenced ArrayLists as to keep operating memory to a minimum. Account detail are forms
// that allow user(s) to see all posted transactions for one or many codes and within a specified time frame.


package Forms;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class AccountDetail implements Serializable
{
    private LocalDate startDate;
    private LocalDate endDate;
    private String startCode;
    private String endCode;
    private Date detailDate;

    private String startDateString;
    private String endDateString;

    private ArrayList<Integer> referencedTransactions;
    private ArrayList<String> referencedAccountCodes;

    public AccountDetail(LocalDate startDate, LocalDate endDate, String startCode, String endCode)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startCode = startCode;
        this.endCode = endCode;
        this.detailDate = new Date();

        referencedTransactions = new ArrayList<>();
        referencedAccountCodes = new ArrayList<>();

        this.startDateString = formattedStartDate();
        this.endDateString = formattedEndDate();
    }

    public void insertReferencedTransaction(int reference)
    {
        referencedTransactions.add(reference);
    }

    public void insertReferencedAccountCode(String reference)
    {
        referencedAccountCodes.add(reference);
    }

    public ArrayList<Integer> getReferencedTransactions()
    {
        return referencedTransactions;
    }

    public String formattedStartDate()
    {
        return startDate.toString();
    }

    public String formattedEndDate()
    {
        return endDate.toString();
    }

    public String getStartCode()
    {
        return startCode;
    }

    public String getEndCode()
    {
        return endCode;
    }

    public Date getDetailDate()
    {
        return detailDate;
    }

    public String getStartDateString()
    {
        return startDateString;
    }

    public String getEndDateString()
    {
        return endDateString;
    }

    public ArrayList<String> getReferencedAccountCodes()
    {
        return referencedAccountCodes;
    }

    //setReferencedAccountCodes() - creates an array of all account codes between the starting and ending codes, and stored as a member.
    public void setReferencedAccountCodes(ArrayList<String> allAccountCodes)
    {
        //for all the account codes passed...
        for(String current : allAccountCodes)
        {
            //if the current code matches the start or end, or is within, add to the referenced ArrayList
            if(current.compareTo(startCode) >= 0 && current.compareTo(endCode) <= 0)
            {
                insertReferencedAccountCode(current);
            }
        }
    }
}
