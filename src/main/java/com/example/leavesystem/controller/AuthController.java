package com.example.leavesystem.controller;

import com.example.leavesystem.entity.Role;
import com.example.leavesystem.entity.UserAccount;
import com.example.leavesystem.repo.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            // 驗證帳號密碼
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // 建立 Security Context
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 強制創建 Session 並保存 Security Context
            var session = request.getSession(true);
            session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
            );

            // 確保 Session 被保存到資料庫
            session.setAttribute("FORCE_PERSIST", System.currentTimeMillis());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("username", username);
            response.put("sessionId", session.getId()); // 除錯用
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 輸出詳細錯誤
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "帳號或密碼錯誤: " + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        UserAccount user = userAccountRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", user.getUsername());
        userInfo.put("displayName", user.getDisplayName());
        userInfo.put("roles", user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList()));

        return ResponseEntity.ok(userInfo);
    }
}
