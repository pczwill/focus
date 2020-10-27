package com.focus.test.util;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;
 
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 
 
public class AESUtils {
 
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";//默认的加密算法
	
	
	
    /**
     * AES 加密操作
     * @param content 待加密内容
     * @param password 加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
	        SecretKeySpec skeySpec = getKey(password);
	        byte[] clearText = content.getBytes("UTF8");
	        final byte[] iv = new byte[16];
	        Arrays.fill(iv, (byte) 0x00);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	        
	        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
	        
	        String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
	        return encrypedValue;
        } catch (Exception ex) {
        }
        return null;
    }
 
    /**
     * AES 解密操作
     *
     * @param content 待解密内容
     * @param password 解密密钥
     * @return 返回解密后数据
     */
    public static String decrypt(String content, String password) {
        try {
	        SecretKey key = getKey(password);
	        final byte[] iv = new byte[16];
	        Arrays.fill(iv, (byte) 0x00);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	        
	        byte[] encrypedPwdBytes = Base64.decode(content, Base64.DEFAULT);
	        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
	        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
	        byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));
	        
	        String decrypedValue = new String(decrypedValueBytes , "UTF-8");
	        return decrypedValue;
        } catch (Exception ex) {
        	System.err.println("Decrypt exception: " + ex.getMessage());
        }
        return null;
    }
    
	private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {
	    int keyLength = 256;
	    byte[] keyBytes = new byte[keyLength / 8];
	    Arrays.fill(keyBytes, (byte) 0x0);
	    
	    byte[] passwordBytes = password.getBytes("UTF-8");
	    int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
	    System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
	    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
	    return key;
	}
	
	public static void main(String[] args) {
		String password = ")O[NB]6YF}+efcaj{+oESb9d8>Ze9M";//密钥
		//String content = "u2/As6doF6+6L4X5DkkBqA==";
		
		String origin = "t";
		System.out.println("原文：" + origin +"\n");
		String ValueB = encrypt(origin, password);
		System.out.println("(encrypt) 加密过后的密文：" + ValueB);
		
		String ValueA = decrypt(ValueB,password);
		System.out.println("(decrypt) 解密过后的密码：" + ValueA);
		
		
	}
	
}