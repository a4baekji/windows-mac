package com.ctf.fms.controller;

import com.ctf.fms.user.UserRepository;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalHeaderAdvice {

    private final UserRepository repo;
    public GlobalHeaderAdvice(UserRepository repo) { this.repo = repo; }

    @ModelAttribute("nickname")
    public String nickname(Authentication auth) {
        if (auth == null) return "Guest";
        return repo.findByUserId(auth.getName())
                .map(u -> u.getNickname())
                .orElse("Guest");
    }

    @ModelAttribute("headerGreeting")
    public String headerGreeting(@ModelAttribute("nickname") String nickname) {
        String tmpl = """
            Hello %s! today is %s.
            """.formatted(nickname, LocalDate.now().toString());

        return interpolate(tmpl);
    }

    private String interpolate(String input) {
        final StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
        String out = interpolator.replace(input);
        return out;
    }
}

