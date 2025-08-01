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

public class Form implements Serializable
{
    private ArrayList<AccountDetail> accountDetail;

    public Form()
    {
        accountDetail = new ArrayList<>();
    }

    public ArrayList<AccountDetail> getAccountDetail()
    {
        return accountDetail;
    }

    public void insertAccountDetail(AccountDetail inAccountDetail)
    {
        accountDetail.add(inAccountDetail);
    }
}
