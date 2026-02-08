package com.ctf.fms.user;

import com.ctf.fms.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder enc;

    public UserService(UserRepository repo, BCryptPasswordEncoder enc) {
        this.repo = repo; this.enc = enc;
    }

    public UserEntity register(String userId, String rawPass, String nickname, String phone) {
        if (userId.startsWith("admin_")){
            throw new UserException("Invalid ID format", HttpStatus.BAD_REQUEST);
        }

        if (repo.existsByUserId(userId)) {
            throw new UserException("User ID already exists", HttpStatus.BAD_REQUEST);
        }
        UserEntity u = new UserEntity();
        u.setUserId(userId);
        u.setPassword(enc.encode(rawPass));
        u.setNickname(nickname);
        u.setPhone(phone);
        return repo.save(u);
    }

    public void updatePassword(Authentication authUser, String newPassword) {
        UserEntity user = repo.findByUserId(authUser.getName()).orElseThrow();
        user.setPassword(enc.encode(newPassword));
        repo.save(user);
    }
}

