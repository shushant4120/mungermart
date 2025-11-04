package com.example.loyalty.multitenant;

import com.example.loyalty.repository.TenantRepository;
import com.example.loyalty.entity.Tenant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantInterceptor implements HandlerInterceptor {
    private final TenantRepository tenantRepo;

    public TenantInterceptor(TenantRepository tenantRepo) {
        this.tenantRepo = tenantRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey == null || apiKey.isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("Missing X-API-Key header");
            return false;
        }
        Tenant t = tenantRepo.findByApiKey(apiKey).orElse(null);
        if (t == null) {
            response.setStatus(401);
            response.getWriter().write("Invalid API key");
            return false;
        }
        TenantContext.setTenantId(t.id);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // Clear the ThreadLocal to avoid leaking tenant id across threads
        TenantContext.clear();
    }
}