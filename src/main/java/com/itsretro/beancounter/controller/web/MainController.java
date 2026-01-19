//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// MainController: a Spring Boot @Controller used for navigation with the Main Menu of BeanCounter.
//

package com.itsretro.beancounter.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    //to be migrated later.
    @GetMapping("/statements")
    public String statements()
    {
        return "statements";
    }
}
