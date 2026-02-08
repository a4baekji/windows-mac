package com.ctf.fms.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@Component
public class LoginFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private final StringRedisTemplate redis;
    public LoginFailureListener(StringRedisTemplate redis) { this.redis = redis; }

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        Object details = event.getAuthentication().getDetails();
        String ip = "unknown";
        if (details instanceof WebAuthenticationDetails d) {
            ip = d.getRemoteAddress();
        }
        String key = "login:fail:" + ip;
        String blockedKey = "login:block:" + ip;

        Long c = redis.opsForValue().increment(key);
        redis.expire(key, Duration.ofMinutes(10));
        if (c != null && c >= 5) {
            redis.opsForValue().set(blockedKey, "1", Duration.ofMinutes(10));
        }
    }
}




