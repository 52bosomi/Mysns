package com.app.mysns.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


// @유가희 참고!!
// https://www.javaguides.net/2020/02/java-sha-256-hash-with-salt-example.html
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
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


    // uuid 생성용
    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong + variant3BitFlag;
    }
    
    private static long get64MostSignificantBitsForVersion1() {
        LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
        Duration duration = Duration.between(start, LocalDateTime.now());
        long seconds = duration.getSeconds();
        long nanos = duration.getNano();
        long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
        long least12SignificatBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
        long version = 1 << 12;
        return (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificatBitOfTime;
    }

    public static UUID generateType1UUID() {

        long most64SigBits = get64MostSignificantBitsForVersion1();
        long least64SigBits = get64LeastSignificantBitsForVersion1();
    
        return new UUID(most64SigBits, least64SigBits);
    }
}
