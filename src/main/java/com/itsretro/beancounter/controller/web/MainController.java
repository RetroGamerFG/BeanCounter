//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// MainController: a Spring Boot @Controller used for navigation with the Main Menu of BeanCounter.
//

package com.itsretro.beancounter.controller.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itsretro.beancounter.model.BusinessInfo;

@Controller
public class MainController
{
    //index() - the main view for the main menu, currently treated as an index.
    //inputs - none.
    //output - the main page loaded into view.
    @GetMapping("/")
    public String index()
    {
        return "main";
    }

    //setup() - confirms BusinessInfo was created, or returns the first time setup page.
    //inputs - none.
    //output - the setup page loaded into view, or an error for unknown issue.
    @GetMapping("/setup")
    public String setup()
    {
        //confirm file does not exist. DO NOT OVERWRITE THE EXISTING FILE HERE.
        File file = new File("data/business.dat");
        
        if(file.exists())
        {
            return "error";
        }
        
        return "setup";
    }

    //setup() - POST version of setup, used when the setup is completed to save the inputs as a BusinessInfo instance.
    //inputs - businessInfo: a BusinessInfo instance with values to be written to data.
    //output - the main page loaded into view, or an error for a write issue.
    @PostMapping("/setup")
    public String finalizeSetup(BusinessInfo businessInfo)
    {
        //confirm file does not exist. DO NOT OVERWRITE THE EXISTING FILE HERE.
        File file = new File("data/business.dat");
        
        if(file.exists())
        {
            return "error";
        }

        //attempt to save the inputs.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/business.dat")))
        {
            oos.writeObject(businessInfo);
        } 
        catch (IOException e) 
        {
            return "error";
        }
        
        return "redirect:/";
    }
}
