package com.ctf.fms.controller;

import com.ctf.fms.exception.UserException;
import com.ctf.fms.user.UserEntity;
import com.ctf.fms.user.UserRepository;
import com.ctf.fms.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserRepository repo;

    public AuthController(UserService userService, UserRepository repo) {
        this.userService = userService; this.repo = repo;
    }

    @GetMapping("/login") public String login() { return "login"; }

    @GetMapping("/register") public String regPage() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam @NotBlank String userId,
                           @RequestParam @NotBlank String password,
                           @RequestParam @NotBlank String nickname,
                           @RequestParam @NotBlank String phone,
                           Model model) {
        try {
            userService.register(userId, password, nickname, phone);
            return "redirect:/login?registered";
        } catch (UserException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req) throws Exception {
        req.logout();
        return "redirect:/";
    }

    @ModelAttribute
    public void nickModel(Authentication auth, Model model) {
        if (auth != null) {
            UserEntity u = repo.findByUserId(auth.getName()).orElse(null);
            if (u != null) model.addAttribute("nickname", u.getNickname());
        }
    }
}
