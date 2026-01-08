package com.itsretro.beancounter.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController
{
    @GetMapping("/")
    public String index()
    {
        return "main";
    }

    @GetMapping("/statements")
    public String statements()
    {
        return "statements";
    }
}
