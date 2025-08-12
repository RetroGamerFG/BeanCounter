//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// AccountDetail - takes user inputs and creates referenced ArrayLists as to keep operating memory to a minimum. Account detail are forms
// that allow user(s) to see all posted transactions for one or many codes and within a specified time frame.


package Forms.Statement;

import java.time.LocalDate;
import java.util.ArrayList;

public class AccountDetail extends Statement
{
    private String startCode;
    private String endCode;

    private ArrayList<String> referencedAccountCodes;

    public AccountDetail(LocalDate startDate, LocalDate endDate, String startCode, String endCode)
    {
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.startCode = startCode;
        this.endCode = endCode;

        referencedAccountCodes = new ArrayList<>();
    }

    public String getStartCode()
    {
        return startCode;
    }

    public String getEndCode()
    {
        return endCode;
    }

    public ArrayList<String> getReferencedAccountCodes()
    {
        return referencedAccountCodes;
    }

//
// Referenced Account Codes Functions
//

    public void insertReferencedAccountCode(String reference)
    {
        referencedAccountCodes.add(reference);
    }

    public void removeReferencedAccountCode(String reference)
    {
        referencedAccountCodes.remove(reference);
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
