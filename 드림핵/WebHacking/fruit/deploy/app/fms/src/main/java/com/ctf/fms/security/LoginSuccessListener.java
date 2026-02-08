package com.ctf.fms.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.StringRedisTemplate;

@Component
public class LoginSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final StringRedisTemplate redis;
    public LoginSuccessListener(StringRedisTemplate redis) { this.redis = redis; }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        Object details = event.getAuthentication().getDetails();
        if (details instanceof WebAuthenticationDetails d) {
            String ip = d.getRemoteAddress();
            redis.delete("login:fail:" + ip);
            redis.delete("login:block:" + ip);
        }
    }
}
