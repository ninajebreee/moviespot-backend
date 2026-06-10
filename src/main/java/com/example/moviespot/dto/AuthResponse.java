package com.example.moviespot.dto;
public class AuthResponse {
    private String accessToken; private String refreshToken;
    private String username; private String email; private String role;
    public AuthResponse(String accessToken, String refreshToken, String username, String email, String role) {
        this.accessToken = accessToken; this.refreshToken = refreshToken;
        this.username = username; this.email = email; this.role = role;
    }
    public String getAccessToken() { return accessToken; } public void setAccessToken(String t) { this.accessToken = t; }
    public String getRefreshToken() { return refreshToken; } public void setRefreshToken(String t) { this.refreshToken = t; }
    public String getUsername() { return username; } public void setUsername(String u) { this.username = u; }
    public String getEmail() { return email; } public void setEmail(String e) { this.email = e; }
    public String getRole() { return role; } public void setRole(String r) { this.role = r; }
}
