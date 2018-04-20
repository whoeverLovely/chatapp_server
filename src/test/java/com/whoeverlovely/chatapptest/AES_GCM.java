package com.whoeverlovely.chatapptest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES_GCM {

	public static String generateKey() {
		String keyStr = null;
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(256);
			SecretKey secretKey = keyGenerator.generateKey();

			keyStr = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return keyStr;
    }

    public static String encrypt(String plainText, String keyStr) {
        String encryptedText = null;

        try {
        	byte[] decodedKey = Base64.getDecoder().decode(keyStr);
			SecretKey keyStoreKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            //prepare iv
            String FIXED_IV = "just give it a try";
            byte[] iv = new byte[cipher.getBlockSize()];
            for(int i = 0; i < cipher.getBlockSize(); i++)
                iv[i] = FIXED_IV.getBytes()[i];

            cipher.init(Cipher.ENCRYPT_MODE, keyStoreKey, new GCMParameterSpec(128, iv));
            byte[] encryptedByte = cipher.doFinal(plainText.getBytes("UTF-8"));
            encryptedText = Base64.getEncoder().encodeToString(encryptedByte);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return encryptedText;
    }

    public static String decrypt(String encryptedText, String keyStr) {
    	String decryptedText = null;

        try {
        	byte[] decodedKey = Base64.getDecoder().decode(keyStr);
			SecretKey keyStoreKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            //prepare iv
            String FIXED_IV = "just give it a try";
            byte[] iv = new byte[cipher.getBlockSize()];
            for(int i = 0; i < cipher.getBlockSize(); i++)
                iv[i] = FIXED_IV.getBytes()[i];

            cipher.init(Cipher.DECRYPT_MODE, keyStoreKey, new GCMParameterSpec(128, iv));
            byte[] encryptedByte = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            decryptedText = new String(encryptedByte,"UTF-8");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return decryptedText;
    }
    
    public static void main(String[] args) {
		String key = generateKey();
		String text = "today is monday hahahahahahahahahahahahahah";
		String encrypted = encrypt(text, key);
		String decrypted = decrypt(encrypted,key);
		System.out.println(encrypted);
		System.out.println(decrypted);
		
	}

}
