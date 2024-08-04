package me.amlu.config;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtConstant {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int KEY_LENGTH = 64;

    public static final String JWT_SECRET = generateSecretKey();
    public static final String JWT_HEADER = "Authorization";

    private JwtConstant() {
    }

    private static String generateSecretKey() {
        byte[] randomBytes = new byte[KEY_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }
}