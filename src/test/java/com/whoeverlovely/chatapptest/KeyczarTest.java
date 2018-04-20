package com.whoeverlovely.chatapptest;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.keyczar.AesKey;
import org.keyczar.Crypter;
import org.keyczar.HmacKey;
import org.keyczar.Signer;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.interfaces.KeyczarReader;

public class KeyczarTest {

	public static SecretKey generateSercurityKey() {
		SecretKey secretKey = null;
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(256);
			secretKey = keyGenerator.generateKey();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return secretKey;
	}

	public static String generateKeyczarKey(SecretKey secretKey) {
		
		AesKey aesKey = null;
		try {
			aesKey = new AesKey(secretKey.getEncoded(), new HmacKey(secretKey.getEncoded()));
		} catch (KeyczarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String myKey = aesKey.toString();
		return myKey;
	}

	public static String encrypt(String plainText, String keyczarKey) {
		
		KeyczarReader reader = new MyKeyReader(keyczarKey);
		Crypter crypter;
		String cipherText = null;
		try {
			crypter = new Crypter(reader);
			cipherText = crypter.encrypt(plainText);
		} catch (KeyczarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cipherText;
	}

	public static String decrypt(String cipherText, String keyczarKey) {
		
		KeyczarReader reader = new MyKeyReader(keyczarKey);
		Crypter crypter;
		String plainText = null;
		try {
			crypter = new Crypter(reader);
			plainText = crypter.decrypt(cipherText);
		} catch (KeyczarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plainText;
	}

	public static void main(String[] args) {
		SecretKey key = generateSercurityKey();
		String s = "today is Monday.";
		String cipherText = encrypt(s, generateKeyczarKey(key));
		String resultText = decrypt(cipherText, generateKeyczarKey(key));
		System.out.println(resultText);

	}
	
	
}
