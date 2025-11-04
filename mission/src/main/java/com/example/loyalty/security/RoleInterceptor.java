package com.example.loyalty.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    public static final String HEADER = "X-User-Role";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Set role in context if header present
        String roleHeader = request.getHeader(HEADER);
        if (roleHeader != null && !roleHeader.isEmpty()) {
            try {
                RoleContext.set(Role.valueOf(roleHeader.toUpperCase()));
            } catch (IllegalArgumentException e) {
                // invalid role -> treat as no role
                RoleContext.clear();
            }
        }

        // If handler is a controller method, check annotation
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            RequireRole rr = hm.getMethodAnnotation(RequireRole.class);
            if (rr == null)
                rr = hm.getBeanType().getAnnotation(RequireRole.class);
            if (rr != null) {
                Role[] allowed = rr.value();
                Role current = RoleContext.get();
                if (current == null) {
                    response.setStatus(403);
                    response.getWriter().write("Forbidden: missing role header");
                    return false;
                }
                boolean ok = Arrays.stream(allowed).anyMatch(r -> r == current);
                if (!ok) {
                    response.setStatus(403);
                    response.getWriter().write("Forbidden: insufficient role");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        RoleContext.clear();
    }
}
