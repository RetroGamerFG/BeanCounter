package com.itsretro.beancounter.interceptor;

import java.io.File;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SetupInterceptor implements HandlerInterceptor 
{
    //preHandle() - will prevent any pages from continuing if business info does not exist (required for specific modules to run BeanCounter).
    //
    //
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception 
    {
        String path = request.getRequestURI();

        //
        // 1. Don't intercept the setup page itself or CSS/JS files, 
        // otherwise you'll get an infinite redirect loop!
        if (path.startsWith("/setup") || path.startsWith("/css") || path.startsWith("/js"))
        {
            return true;
        }

        //
        // 2. Check if business info exists.
        File businessFile = new File("data/business.dat");

        if(businessFile.exists() == false)
        {
            response.sendRedirect("/setup");
            return false;
        }

        return true;
    }
}
