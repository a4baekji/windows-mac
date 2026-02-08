package com.ctf.fms.controller;

import com.ctf.fms.dto.PasswordChangeRequest;
import com.ctf.fms.user.UserEntity;
import com.ctf.fms.user.UserRepository;
import com.ctf.fms.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DaoAuthenticationProvider dao;
    private final UserRepository repo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.uploads}")
    private String uploadDir;
    @Value("${app.resourceBaseUrl}")
    private String resourceBase;

    public AdminController(DaoAuthenticationProvider dao, UserRepository repo, UserService userService, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.repo = repo;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String admin(Model model) {
        model.addAttribute("resourceBase", resourceBase);
        return "admin";
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<java.util.Map<String,String>> upload( @RequestParam("file") MultipartFile file) throws Exception {
        long MAX = 3L * 1024 * 1024; // 3MB

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "empty file"));
        }
        if (file.getSize() > MAX) {
            return ResponseEntity.status(413).body(java.util.Map.of("message", "max 3MB"));
        }

        String name = StringUtils.cleanPath(file.getOriginalFilename());
        if (name.toLowerCase().endsWith(".jsp")) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "JSP not allowed"));
        }

        File dir = new java.io.File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        Path target = dir.toPath().resolve(System.currentTimeMillis() + "_" + name);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String webPath = "/uploads/" + target.getFileName().toString();
        return ResponseEntity.ok(java.util.Map.of("path", webPath));
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        model.addAttribute("resourceBase", resourceBase);
        model.addAttribute("fruitId", id);
        return "edit";
    }

    @GetMapping(value = "/api/uploads", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> listUploads() throws Exception {
        Path base = Paths.get(uploadDir);
        if (!Files.isDirectory(base)) return List.of();
        try (Stream<Path> s = Files.list(base)) {
            return s.filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .filter(name -> name.matches("(?i).+\\.(png|jpe?g|gif|webp)$"))
                    .sorted()
                    .toList();
        }
    }

}

