package com.app.mysns.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

// public class UserSha256 {
//     public static String encrypt(String planText) {
//         try {
//             MessageDigest md = MessageDigest.getInstance("SHA-256");
//             md.update(planText.getBytes());
//             byte byteData[] = md.digest();
//             StringBuffer sb = new StringBuffer();
//             for (int i = 0; i < byteData.length; i++) {
//                 sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
//             }
//             StringBuffer hexString = new StringBuffer();
//             for (int i = 0; i < byteData.length; i++) {
//                 String hex = Integer.toHexString(0xff & byteData[i]);
//                 if (hex.length() == 1) {
//                     hexString.append('0');
//                 }
//                 hexString.append(hex);
//             }
//             return hexString.toString();
//         } catch (Exception e) {
//             e.printStackTrace();
//             throw new RuntimeException();
//         }
//     }

// }

// @유가희 참고!!
// https://www.javaguides.net/2020/02/java-sha-256-hash-with-salt-example.html
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class SecureUtilsService {

    public String getSecurePassword(String password, String salt) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(getSalt(salt));
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    // default salt
    public String SHA256(String target) {

        String generatedPassword = null;
        try {
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(target.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private byte[] getSalt(String salt) throws NoSuchAlgorithmException {
        // SecureRandom random = new SecureRandom();
        // byte[] salt = new byte[16];
        // random.nextBytes(salt);
        // 16 byte key 생성
        byte[] _salt = salt.getBytes(StandardCharsets.UTF_8);
        return Arrays.copyOfRange(_salt, 0, 16);
    }

    // example
    // public static void main(String[] args) throws NoSuchAlgorithmException {

    // same salt should be passed
    //     byte[] salt = getSalt();
    //     String password1 = getSecurePassword("Password", salt);
    //     String password2 = getSecurePassword("Password", salt);
    //     System.out.println(" Password 1 -> " + password1);
    //     System.out.println(" Password 2 -> " + password2);
    //     if (password1.equals(password2)) {
    //         System.out.println("passwords are equal");
    //     }
    // }
}
