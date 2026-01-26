package com.itsretro.beancounter.service;

import org.springframework.stereotype.Service;

import com.itsretro.beancounter.model.BusinessInfo;

@Service
public class BusinessInfoService 
{
    private final BusinessInfo businessInfo; //does not change

    //
    // Initializer(s)
    //

    public BusinessInfoService(BusinessInfo businessInfo)
    {
        this.businessInfo = businessInfo;
    }

    //
    // Service Methods
    //
}
