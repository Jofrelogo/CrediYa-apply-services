package com.crediya.apply.r2dbc.security;


import com.crediya.apply.model.jwt.PasswordHashProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Password hash provider using BCrypt algorithm
 */
@Component
public class BCryptPasswordHashProvider implements PasswordHashProvider {

    private static final Logger log = LoggerFactory.getLogger(BCryptPasswordHashProvider.class);

    private final BCryptPasswordEncoder encoder;

    public BCryptPasswordHashProvider() {
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Verifica si la contraseña sin encriptar coincide con el hash
     */
    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        boolean result = encoder.matches(rawPassword, encodedPassword);
        log.debug("🔑 Password match result: {}", result);
        return result;
    }

    /**
     * Hashea la contraseña usando BCrypt
     */
    @Override
    public String encode(String rawPassword) {
        String hash = encoder.encode(rawPassword);
        log.debug("🔒 Password hashed successfully");
        return hash;
    }
}
