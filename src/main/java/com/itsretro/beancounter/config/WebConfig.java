//overrides for the front-end
//currently used to remove values typed after database submissions using the back button

package com.itsretro.beancounter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import com.itsretro.beancounter.interceptor.SetupInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    @Autowired
    private SetupInterceptor setupInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.setCacheSeconds(0); // Disables cache
        
        registry.addInterceptor(interceptor);

        registry.addInterceptor(setupInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/setup/**", "/css/**", "/js/**", "/images/**");
    }
}
