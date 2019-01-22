package com.along.util;


import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Description: AES加解密
 * @Author along
 * @Date 2019/1/22 15:58
 */
public class AESUtil {

    /**
     * 加密1
     * @param content 加密内容，字符串
     * @param password 盐
     * @return 加密之后的字节数组
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);// 加密
            return result;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密2
     * @param content 加密内容，字符串
     * @param password 盐
     * @return 加密之后的字符串
     */
    public static String encryptString(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            String encryptResultStr = parseByte2HexStr(result);
            return encryptResultStr;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密1
     * @param content 解密内容，字节数组
     * @param password 盐
     * @return 解密之后的字节数组
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密2
     * @param content 解密内容
     * @param password 盐
     * @return 解密之后的字符串
     */
    public static String decrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptFrom = parseHexStr2Byte(content);
            byte[] result = cipher.doFinal(decryptFrom);
            return new String(result);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * byte转String
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * String转byte
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        String content = "test"; // 加密内容
        String password = "12345678"; // 盐
        // 加密
        System.out.println("加密前：" + content);
        byte[] encryptResult = encrypt(content, password);
        String encryptResultStr = parseByte2HexStr(encryptResult);
        System.out.println("加密后：" + encryptResultStr);
        // 解密
        byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);
        byte[] decryptResult = decrypt(decryptFrom, password);
        System.out.println("解密后：" + new String(decryptResult));

        String enStr = encryptString("123", "@#&^%-$#@Coupon#$%^&@*");
        System.out.println(enStr);
        System.out.println(decrypt("2DA4A4EEA4777CB2CC342815FC84B539", "@#&^%-$#@Coupon#$%^&@*"));
    }
}
