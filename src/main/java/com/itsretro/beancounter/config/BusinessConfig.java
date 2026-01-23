package com.itsretro.beancounter.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.itsretro.beancounter.model.BusinessInfo;

@Configuration
public class BusinessConfig 
{
    @Bean
    @ConfigurationProperties(prefix = "business.dat")
    public BusinessInfo businessInfo()
    {
        BusinessInfo businessInfo = new BusinessInfo();
        File file = new File("data/business.dat");

        if (file.exists())
        {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
            {
                return (BusinessInfo) ois.readObject();
            } 
            catch (IOException | ClassNotFoundException e)
            {
                //fails to load data...
            }
        }

        return businessInfo;
    }

}
