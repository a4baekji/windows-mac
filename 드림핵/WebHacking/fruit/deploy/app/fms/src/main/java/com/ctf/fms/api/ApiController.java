package com.ctf.fms.api;


import com.ctf.fms.user.UserRepository;
import com.ctf.fms.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Value("${app.uploads}")
    private String uploadDir;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public ApiController(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService, UserService userService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }



    @PostMapping("/changePassword")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestParam("userId") String userId, @RequestParam("currentPassword") String currentPassword,
                                                              @RequestParam("newPassword") String newPassword, Authentication authUser){
        Map<String, Object> response = new HashMap<>();
        if (authUser == null) {
            response.put("message", "Login first");
            return ResponseEntity.status(401).body(response);
        }
        try {
            UserDetails user = userDetailsService.loadUserByUsername(userId);
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                response.put("message", "Bad credentials");
                return ResponseEntity.status(401).body(response);
            }

            if (!authUser.getName().equals(userId)){
                response.put("message", "Bad credentials");
                return ResponseEntity.status(401).body(response);
            }

            userService.updatePassword(authUser, newPassword);
            response.put("message", "Password changed successfully");
            return ResponseEntity.status(200).body(response);

        } catch (UsernameNotFoundException e) {
            response.put("message", "Bad credentials");
            return ResponseEntity.status(401).body(response);
        } catch (Exception e){
            response.put("message", "An error occured");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/admin/upload")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file, Authentication authUser) {
        Map<String, Object> response = new HashMap<>();
        try {

            if (authUser == null) {
                response.put("message", "Login first");
                return ResponseEntity.status(401).body(response);
            }
            if (!authUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                response.put("message", "Not an admin");
                return ResponseEntity.status(403).body(response);
            }
            String name = StringUtils.cleanPath(file.getOriginalFilename());
            if (!name.toLowerCase().endsWith(".png") && !name.toLowerCase().endsWith(".jpg")) {
                response.put("message", "Only PNG and JPG files are allowed");
                return ResponseEntity.status(400).body(response);
            }
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            Path target = dir.toPath().resolve(System.currentTimeMillis() + "_" + name);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            String webPath = "/uploads/" + target.getFileName().toString();
            response.put("path", webPath);
            response.put("message", "File uploaded successfully");
            return ResponseEntity.status(200).body(response);
        } catch (IOException e){
            response.put("message", "An error occured");
            return ResponseEntity.status(500).body(response);
        }
    }
}
