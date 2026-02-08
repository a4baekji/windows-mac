package com.ctf.fms.security;

import com.ctf.fms.user.UserEntity;
import com.ctf.fms.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Lazy
    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity u = repo.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("not found"));
        String role = (u.isAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";
        return new User(u.getUserId(), u.getPassword(), AuthorityUtils.createAuthorityList(role));
    }
}

