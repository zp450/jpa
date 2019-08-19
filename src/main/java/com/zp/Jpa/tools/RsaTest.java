package com.zp.Jpa.tools;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RsaTest {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密钥对
     * 
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     * 
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     * 
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }
    
    /**
     * RSA加密
     * 
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64String(encryptedData));
    }

    /**
     * RSA解密
     * 
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容 
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     * 
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     * 
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static void main(String[] args) {
        try {
            // 生成密钥对
//            KeyPair keyPair = getKeyPair();
//            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
//            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
//            System.out.println("私钥:" + privateKey);
//            System.out.println("公钥:" + publicKey);
//            // RSA加密
//            String data = "待加密的文字内容";
//            String encryptData = encrypt(data, getPublicKey(publicKey));
//            System.out.println("加密后内容:" + encryptData);
//        	String data = "123456";
//        	String publicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRwS+LAGzAt4Gl1xsckMU5kxC2ARX95vx4PZxcWsRjhYNanLUcVNL1dJJgTyu6hhdXZBbLuXI7n4faA/J0BVnybUdFGCO8IWAaFd4uYsc5SD7aBsJQ016xem4x8WRGRPMpYu2bJOpx8B5ORWzwuKajOjV9oKz6ReR8ll9y9kh9EwIDAQAB";
        	String privateKey ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJHBL4sAbMC3gaXXGxyQxTmTELYBFf3m/Hg9nFxaxGOFg1qctRxU0vV0kmBPK7qGF1dkFsu5cjufh9oD8nQFWfJtR0UYI7whYBoV3i5ixzlIPtoGwlDTXrF6bjHxZEZE8yli7Zsk6nHwHk5FbPC4pqM6NX2grPpF5HyWX3L2SH0TAgMBAAECgYAtCmHCtpLkytBJiztZjHMl44hadeNx3ptOHNvgvjvJ9UwuCBb/dkbqiudg05ZTwXQdEVTqB5iBTD8S4/1cVPsCMH3Y0lX9bCTcUqszVfPWlni4HJSF6AaPC1Dnr1WeHpu5kHaLWuKuR6v0cpPAGtebE2X40Fqn+5c9xvYD6XJ4oQJBAPSxovfLw725DlejKYN0H4uyrC4pZwWqhh+NxLB918/rB8zqpEFbHvosfmfMfoe7R7RtZyhRzhQXq6hqNckfCBcCQQCYfT7Rh/UgUw4TLqgGN8or/zpEpGkDS2ouVNXQiGqpfiSRGwjfCLX71QpI0U51/fC/Dl4Wn+8WDQOD4S4Dq5RlAkB5Y6Z1R6DTffqff1IY5ILByuGgBOoW5YGkJbBt3gAyJWa5Qa46vfmgInKTC9+5di8cUynZ1rtlPFjsM8R7AeoBAkAkJqUq5ZGOfUI79/arqrRKY/K7bULcHhfpLgGWs5Cd6CLhJ8idn6INNv9+Lejs/iwCD3Ts5jO3RZSMwmi3RAklAkAbutyPBRsBbWGPxTcA1dP8uZF8v+ftDPPt4wH116xFIX9GeXzvV+aKNDne8mz+xGUx7H6FtFli9zeK1z1nKIiE";
//        	String encryptData="%7B%22userName%22%3A%22admin%22%2C%22userPassWord%22%3A%22dH0LIKC5JHi7Bb57A5LnyHBOcliFFmkhUxXUBe9Slif77FiuPy+TMg6hMiMMUoWmQ8xDVLDyAdVpOO%2FA8Is7+nQGwbNvPpsuuc3nCb8suPlrsh5pVXwItYCr5G0H0vjN07rhrmrs4Kf2FzuxPd4h1cilOAvGHBb2gWeZOHDUm0s=%22%7D";
//        	String encryptData = encrypt(data, getPublicKey(publicKey));
//            System.out.println("加密后内容:" + encryptData);
            String  encryptData="NRZR+EPCjFpvplG4zT+OEkaYCa3JWpsjYJmEft7sgl3G0btGGkrIgt0wJt4XabyMndNEVChXPU2HI85sPSTz5Hpxd+sWzExSFRLFNvN7DVkNOqofEjCFSsCd9U3EChVWV9BMH7jCw/Un1+F+yQqBuKL5YE3GYr1zawcH4SCSti8=";
        	// RSA解密
            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
            System.out.println("解密后内容:" + decryptData);
            
            // RSA签名
//            String sign = sign(data, getPrivateKey(privateKey));
//            // RSA验签
//            boolean result = verify(data, getPublicKey(publicKey), sign);
//            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加解密异常");
        }
    }
}
