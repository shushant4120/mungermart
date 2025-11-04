package com.example.loyalty.config;

import com.example.loyalty.multitenant.TenantInterceptor;
import com.example.loyalty.security.RoleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private TenantInterceptor tenantInterceptor;

    @Autowired
    private RoleInterceptor roleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Role checks should run before tenant resolution to short-circuit unauthorized
        // requests
        registry.addInterceptor(roleInterceptor).addPathPatterns("/api/v1/**");
        registry.addInterceptor(tenantInterceptor).addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/tenants/register");
    }
}