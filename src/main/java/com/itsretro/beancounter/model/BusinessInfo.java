//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// BusinessInfo: a model used for information about the business. Includes required members that
//  determine program operation and the generation of statement dates.
//

package com.itsretro.beancounter.model;

import java.io.Serializable;
import java.time.LocalDate;

public class BusinessInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String phone;

    private LocalDate incorporationDate;

    private boolean usageType; //false for single user, true for multiple users w/UserAccount

    //
    // Getters & Setters
    //

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getZipcode()
    {
        return zipcode;
    }

    public void setZipcode(String zipcode)
    {
        this.zipcode = zipcode;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public LocalDate getIncorporationDate()
    {
        return incorporationDate;
    }

    public void setIncorporationDate(LocalDate incorporationDate)
    {
        this.incorporationDate = incorporationDate;
    }

    public boolean getUsageType()
    {
        return usageType;
    }

    public void setUsageType(boolean usageType)
    {
        this.usageType = usageType;
    }
}
