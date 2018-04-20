package com.whoeverlovely.chatapptest;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.junit.Test;

public class EncryptionTest {

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

	public static byte[] encrypt(String plainText, String keyStr) {

		byte[] result = null;
		try {
			byte[] decodedKey = Base64.getDecoder().decode(keyStr);
			SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivForCBC = createIV(cipher.getBlockSize(), Optional.of(new SecureRandom()));

			cipher.init(Cipher.ENCRYPT_MODE, originalKey, ivForCBC);

			byte[] encryptedText = cipher.doFinal(plainText.getBytes("UTF-8"));
			byte[] iv = cipher.getIV();

			result = new byte[encryptedText.length + iv.length];
			for (int i = 0; i < iv.length; i++)
				result[i] = iv[i];
			for (int i = iv.length; i < iv.length + encryptedText.length; i++)
				result[i] = encryptedText[i];

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static IvParameterSpec createIV(int ivSizeBytes, Optional<SecureRandom> rng) {
		final byte[] iv = new byte[ivSizeBytes];
		final SecureRandom theRNG = rng.orElse(new SecureRandom());
		theRNG.nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	public static String decrypt(String keyStr, byte[] cipherText) {

		String result = null;

		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			byte[] decodedKey = Base64.getDecoder().decode(keyStr);
			SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

			byte[] iv = new byte[cipher.getBlockSize()];
			byte[] text = new byte[cipherText.length - cipher.getBlockSize()];

			for (int i = 0; i < cipherText.length; i++) {
				if (i < cipher.getBlockSize())
					iv[i] = cipherText[i];
				else
					text[i - cipher.getBlockSize() - 1] = cipherText[i];
			}

			cipher.init(Cipher.DECRYPT_MODE, originalKey, new IvParameterSpec(iv));
			byte[] decrypted = cipher.doFinal(text);
			result = new String(decrypted, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * public static IvParameterSpec readIV(final int ivSizeBytes, final InputStream
	 * is) throws IOException { final byte[] iv = new byte[ivSizeBytes]; int offset
	 * = 0; while (offset < ivSizeBytes) { final int read = is.read(iv, offset,
	 * ivSizeBytes - offset); if (read == -1) { throw new
	 * IOException("Too few bytes for IV in input stream"); } offset += read; }
	 * return new IvParameterSpec(iv); }
	 */

	public static void main(String[] args) {
		String key = generateKey();
		System.out.println("key is " + key);

		String originalStr = "today is sunday";
		byte[] encrypted = encrypt(originalStr, key);
		
		String finalStr = decrypt(key, encrypted);
		System.out.println("decrypted text is " + finalStr);
	}
	
	@Test
	public void test() {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int size = cipher.getBlockSize();
		final byte[] iv = new byte[size];
		final SecureRandom theRNG = new SecureRandom();
		theRNG.nextBytes(iv);
		System.out.println(size);
		System.out.println(iv);
	}
	
	

}
