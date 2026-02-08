package com.ctf.fms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redis;

    public RateLimitFilter(StringRedisTemplate redis) { this.redis = redis; }

    private boolean isLoginPost(HttpServletRequest req) {
        if (!"POST".equalsIgnoreCase(req.getMethod())) return false;
        String ctx = req.getContextPath();
        String path = req.getRequestURI().substring(ctx.length());
        return "/login".equals(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        if (isLoginPost(req)) {
            String ip = req.getRemoteAddr();
            System.out.println(ip);
            if (Boolean.TRUE.equals(redis.hasKey("login:block:" + ip))) {
                res.sendRedirect("/login?lock");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}

