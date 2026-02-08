package com.ctf.fms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.uploads}")
    private String uploadsDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String loc = uploadsDir;
        if (!loc.endsWith("/")) loc += "/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:"+loc);
    }
}
