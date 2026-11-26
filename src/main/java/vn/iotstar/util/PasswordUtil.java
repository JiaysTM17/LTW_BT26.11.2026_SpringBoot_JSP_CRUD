package vn.iotstar.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return plainPassword;
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            if (BCrypt.checkpw(plainPassword.trim(), hashedPassword.trim())) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return plainPassword.trim().equals(hashedPassword.trim());
    }
}
