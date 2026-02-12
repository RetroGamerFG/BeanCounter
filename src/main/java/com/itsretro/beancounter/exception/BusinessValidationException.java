//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// BusinessValidationException: a RuntimeException extension to handle errors related to journal entries.
//

package com.itsretro.beancounter.exception;

public class BusinessValidationException extends RuntimeException 
{
    private final String fieldName;

    //
    // Initializer(s)
    //

    public BusinessValidationException(String fieldName, String message)
    {
        super(message);
        this.fieldName = fieldName;
    }

    //
    // Getters & Setters
    //

    public String getFieldName()
    { 
        return fieldName; 
    }
}