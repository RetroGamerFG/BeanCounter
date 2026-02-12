//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// BusinessInfoService: a service class which holds a single linked instance of BusinessInfo.
//  The BusinessInfo class/member is retrieved through the "business.dat" file, and extracted by BusinessConfig.
//

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
    // Getters & Setters
    //

    public BusinessInfo getBusinessInfo()
    {
        return this.businessInfo;
    }
}
