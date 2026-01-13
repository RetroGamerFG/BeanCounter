package com.itsretro.beancounter.model;

import java.math.BigDecimal;

public interface FinancialBlock 
{
    BigDecimal getTotalDebits();
    BigDecimal getTotalCredits();
    BigDecimal getGrandTotal();
    
    void setGrandTotal(BigDecimal amount);
    void setGrandTotalType(String type);
}
