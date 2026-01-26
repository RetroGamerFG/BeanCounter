package com.itsretro.beancounter.logic;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.viewmodel.IncomeStatementView;

@Component
public class IncomeStatementLogic 
{
    public IncomeStatementView createIncomeStatementView()
    {
        IncomeStatementView isv = new IncomeStatementView();
        return isv;
    }
}
