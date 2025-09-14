package com.crediya.apply.model.jwt;

public interface JwtProvider {

    String generateToken(String dni, String email, String role);

    boolean validateToken(String token);

    String getEmailFromToken(String token);

    String getRoleFromToken(String token);

    String getDniFromToken(String token);
}
