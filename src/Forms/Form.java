//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// Form - the base class for forms. Used to create instances of ArrayList that hold generated forms by the user. This includes account detail forms, income statements, balance sheets,
// retained earnings forms, and other forms tbd.


package Forms;

import java.io.Serializable;
import java.util.ArrayList;

import Forms.Statement.AccountDetail;
import Forms.Statement.DatedStatement;

public class Form implements Serializable
{
    private ArrayList<AccountDetail> accountDetail;
    private ArrayList<DatedStatement> datedStatement;

    public Form()
    {
        accountDetail = new ArrayList<>();
        datedStatement = new ArrayList<>();
    }

    public ArrayList<AccountDetail> getAccountDetail()
    {
        return accountDetail;
    }

    public ArrayList<DatedStatement> getDatedStatement()
    {
        return datedStatement;
    }

//
// Additional Functions
//

    public void insertAccountDetail(AccountDetail inAccountDetail)
    {
        accountDetail.add(inAccountDetail);
    }

    public void insertStatement(DatedStatement inStatement)
    {
        datedStatement.add(inStatement);
    }
}
