package org.stock_game;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class SecurityUtilities {

    public static String getHashedPassword(String password, String salt) {
        String hashedPassword = null;
        try {
            String saltedPassword = password + salt;
            hashedPassword = hashAndReturnSaltedPassword(saltedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    private static String hashAndReturnSaltedPassword(String saltedPassword) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] securePasswordBytes = messageDigest.digest(saltedPassword.getBytes());
        StringBuilder hashedPasswordBuilder = new StringBuilder();
        for (byte passwordByte : securePasswordBytes) {
            hashedPasswordBuilder.append(Integer.toString((passwordByte & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return hashedPasswordBuilder.toString();
    }

    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        HexFormat hf = HexFormat.of();
        return hf.formatHex(salt);
    }
}
