package com.example.leavesystem.service;

import com.example.leavesystem.entity.UserAccount;
import com.example.leavesystem.repo.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserAccountRepository repo;

    public DatabaseUserDetailsService(UserAccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount u = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(u.getUsername(), u.getPassword(), u.isEnabled(), true, true, true,
                u.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toSet()));
    }
}
