package com.ctf.fms;

import com.ctf.fms.user.UserEntity;
import com.ctf.fms.user.UserRepository;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class InsertAdminRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(InsertAdminRunner.class);
    private final UserRepository repo;
    private final BCryptPasswordEncoder enc;

    public InsertAdminRunner(UserRepository repo, BCryptPasswordEncoder enc) {
        this.repo = repo; this.enc = enc;
    }

    @Override
    public void run(String... args) {


        String userId = "admin_" + randomToken(3);
        System.out.println(userId);
        String rawPass = System.getenv("ADMIN_PASSWORD");
        System.out.println(rawPass);

        UserEntity u = new UserEntity();
        u.setUserId(userId);
        u.setPassword(enc.encode(rawPass));
        u.setNickname("Admin");
        u.setPhone("000-0000");
        u.setAdmin(true);
        repo.save(u);


        log.warn("Seeded admin account -> userid='{}' password='{}'", userId, rawPass);
    }

    private static String randomToken(int len) {
        final String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
        return sb.toString();
    }
}
