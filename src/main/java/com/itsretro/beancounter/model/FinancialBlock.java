//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// FinancialBlock: an interface model used by AccountDetail models which associates totals
//  and streamlines computations for grand totals.
//

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
