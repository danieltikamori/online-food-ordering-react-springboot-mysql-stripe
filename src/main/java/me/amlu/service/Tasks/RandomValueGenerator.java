package me.amlu.service.Tasks;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomValueGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomValue() {
        byte[] randomBytes = new byte[16]; // adjust the length as needed
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }
}