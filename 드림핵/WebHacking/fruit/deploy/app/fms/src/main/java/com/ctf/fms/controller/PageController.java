package com.ctf.fms.controller;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class PageController {

    @Value("${app.resourceBaseUrl}")
    private String resourceBase;

    @GetMapping("/")
    public String index(Model model, Authentication auth, @RequestParam(required=false) String q) {
        model.addAttribute("resourceBase", resourceBase);
        model.addAttribute("q", q == null ? "" : q);
        return "index";
    }

    @GetMapping("/fruit/{id}")
    public String fruitDetail(@PathVariable("id") String id, Model model, Authentication auth) {
        model.addAttribute("resourceBase", resourceBase);
        model.addAttribute("fruitId", id);
        return "detail";
    }

    @GetMapping("/mypage")
    public String mypage() { return "mypage"; }
}
