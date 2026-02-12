//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// SetupInterceptor: an interceptor that checks a business has been set up, which is required for functions of the
//  program to run correctly.
//

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
    //inputs -
        //request: the http request, used to get the URL path of the user.
        //response: the http response, used to return the redirect URL on false.
        //handler: an object instance. Not used in this overridden method.
    //output - a boolean based on the result; will redirect to first time setup if false.
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
