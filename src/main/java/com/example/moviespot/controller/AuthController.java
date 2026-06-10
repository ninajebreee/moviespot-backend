package com.example.moviespot.controller;

import com.example.moviespot.dto.*;
import com.example.moviespot.entity.*;
import com.example.moviespot.repository.*;
import com.example.moviespot.security.JwtTokenProvider;
import com.example.moviespot.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired private AuthenticationManager authManager;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwt = tokenProvider.generateToken(auth);
            RefreshToken rt = refreshTokenService.createRefreshToken(req.getUsername());
            User user = userRepository.findByUsername(req.getUsername()).orElseThrow();
            String role = user.getRoles().stream().map(Role::getName).findFirst().orElse("ROLE_USER");
            return ResponseEntity.ok(new AuthResponse(jwt, rt.getToken(), user.getUsername(), user.getEmail(), role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername()))
            return ResponseEntity.badRequest().body(Map.of("message", "Username already taken"));
        if (userRepository.existsByEmail(req.getEmail()))
            return ResponseEntity.badRequest().body(Map.of("message", "Email already in use"));

        User user = new User(req.getUsername(), req.getEmail(), passwordEncoder.encode(req.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        user.setRoles(new HashSet<>(Set.of(userRole)));
        userRepository.save(user);

        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = tokenProvider.generateToken(auth);
        RefreshToken rt = refreshTokenService.createRefreshToken(req.getUsername());
        return ResponseEntity.ok(new AuthResponse(jwt, rt.getToken(), user.getUsername(), user.getEmail(), "ROLE_USER"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest req) {
        try {
            RefreshToken rt = refreshTokenService.findByToken(req.getRefreshToken())
                    .orElseThrow(() -> new RuntimeException("Refresh token not found"));
            refreshTokenService.verifyExpiration(rt);
            String newJwt = tokenProvider.generateTokenFromUsername(rt.getUser().getUsername());
            User user = rt.getUser();
            String role = user.getRoles().stream().map(Role::getName).findFirst().orElse("ROLE_USER");
            return ResponseEntity.ok(new AuthResponse(newJwt, req.getRefreshToken(), user.getUsername(), user.getEmail(), role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        String role = user.getRoles().stream().map(Role::getName).findFirst().orElse("ROLE_USER");
        return ResponseEntity.ok(new AuthResponse(null, null, user.getUsername(), user.getEmail(), role));
    }
}
