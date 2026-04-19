package com.GabrielTiziano.Notify.interceptor;

import com.GabrielTiziano.Notify.security.JWTUserData;
import com.GabrielTiziano.Notify.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        JWTUserData userPrincipal = (JWTUserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean allowed = rateLimiterService.isAllowed(userPrincipal.id());

        if (!allowed) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Rate limit exceeded. Maximum 5 requests per minute.\"}");
            return false;
        }

        return true;
    }
}
