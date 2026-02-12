//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalLogic: a logic class for JournalEntry and related classes.
//  Includes methods related to class member assignment and validation of amounts.
//

package com.itsretro.beancounter.logic;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.itsretro.beancounter.model.JournalEntryLine;

@Component
public class JournalLogic
{
    //isLineValid() - validates that a debit/credit has an amount assigned, and only one is assigned.
    //inputs - line: the JournalEntryLine instance to validate.
    //output - a boolean based on the result of the operation.
    public boolean isLineValid(JournalEntryLine line)
    {
        if(line.getDebitAmount() != null && "D".equalsIgnoreCase(line.getTransactionType()))
        {
            return true;
        }
        else if(line.getCreditAmount() != null && "C".equalsIgnoreCase(line.getTransactionType()))
        {
            return true;
        }

        return false;
    }

    //isBalanced() - performs validation checks to ensure each line balances, and is properly formatted.
    //inputs - lines: a List of JournalEntryLine instances to validate.
    //output - a boolean based on the result of the operation.
    public boolean isBalanced(List<JournalEntryLine> lines)
    {
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for(JournalEntryLine line : lines)
        {
            if(isLineValid(line) == false) //if the line is not a valid debit/credit, return false
            {
                return false;
            }

            if("D".equalsIgnoreCase(line.getTransactionType())) //add if debit, subtract if credit
            {
                totalAmount = totalAmount.add(line.getDebitAmount());
            }
            else
            {
                totalAmount = totalAmount.subtract(line.getCreditAmount());
            }
        }

        return totalAmount.compareTo(BigDecimal.ZERO) == 0; //if balanced, is valid
    }
}
