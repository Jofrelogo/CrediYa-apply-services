package com.crediya.apply.model.jwt;

public interface PasswordHashProvider {
    boolean matches(String rawPassword, String encodedPassword);
    String encode(String rawPassword);
}