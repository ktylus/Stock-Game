package org.stock_game;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class SecurityUtilities {

    public static String getSecurePassword(String password, String salt) {
        String securePassword = null;
        try {
            String saltedPassword = password + salt;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] securePasswordBytes = messageDigest.digest(saltedPassword.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte passwordByte : securePasswordBytes) {
                stringBuilder.append(Integer.toString((passwordByte & 0xff) + 0x100, 16).substring(1));
            }
            securePassword = stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return securePassword;
    }

    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        HexFormat hf = HexFormat.of();
        return hf.formatHex(salt);
    }
}
