package com.example.leavesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(uds);
        auth.setPasswordEncoder(encoder);
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**","/js/**","/images/**").permitAll()
                .requestMatchers("/view/apply").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/portal", true)
                    .usernameParameter("username")
                    .passwordParameter("password")
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout").permitAll()
            )
            .sessionManagement(session -> session
                .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
                .maximumSessions(1).maxSessionsPreventsLogin(false)
            )
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
