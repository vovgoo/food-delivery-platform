package org.vovgoo.userservice.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class VerificationUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    private VerificationUtils() {}

    public static String generateOtp() {
        int min = 100_000;
        int max = 999_999;
        return String.valueOf(RANDOM.nextInt(max - min + 1) + min);
    }

    public static UUID generateEmailToken() {
        return UUID.randomUUID();
    }

    public static boolean verifyToken(String actual, String provided, AtomicInteger attempts, int maxAttempts) {
        boolean valid = MessageDigest.isEqual(
                actual.getBytes(StandardCharsets.UTF_8),
                provided.getBytes(StandardCharsets.UTF_8)
        );

        if (!valid) {
            int currentAttempts = attempts.incrementAndGet();
            if (currentAttempts >= maxAttempts) {
                return false;
            }
        }

        return valid;
    }
}
