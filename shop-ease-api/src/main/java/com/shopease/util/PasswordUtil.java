package com.shopease.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hash a plain password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 rounds for security
    }

    // Check raw password against hashed password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            throw new IllegalArgumentException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
