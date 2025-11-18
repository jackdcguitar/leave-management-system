package com.example.leavesystem.controller;

import com.example.leavesystem.entity.Role;
import com.example.leavesystem.entity.UserAccount;
import com.example.leavesystem.repo.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserRestController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<UserAccount> users = userAccountRepository.findAll();

        List<Map<String, Object>> userList = users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("displayName", user.getDisplayName());
            userMap.put("enabled", user.isEnabled());
            userMap.put("roles", user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
            return userMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(userList);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> request) {
        // TODO: Implement user creation logic
        return ResponseEntity.ok(Map.of("success", true, "message", "User created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> request) {
        // TODO: Implement user update logic
        return ResponseEntity.ok(Map.of("success", true, "message", "User updated successfully"));
    }
}
