package com.itsretro.beancounter.exception;

public class BusinessValidationException extends RuntimeException 
{
    private final String fieldName;

    public BusinessValidationException(String fieldName, String message)
    {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName()
    { 
        return fieldName; 
    }
}