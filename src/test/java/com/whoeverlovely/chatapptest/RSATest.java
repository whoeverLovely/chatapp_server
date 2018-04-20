package com.whoeverlovely.chatapptest;

import java.io.IOException;
import org.junit.Test;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.json.JSONException;
import org.json.JSONObject;

public class RSATest {
	final private static String TAG = "RSAKeyStoreUtil";
    final private static String defaultAlias = "myKeyPair";

    private static void generateKeyPairWithKeyStore(String alias) {

        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(alias, null);

            //alias doesn't exists in KeyStore, generate a new key pair
            if (entry == null) {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                kpg.generateKeyPair();
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    

    /**
     * generate default key pair
     */
    public static void generateKeyPairWithKeyStore() {
        generateKeyPairWithKeyStore(defaultAlias);
    }

    private static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(defaultAlias, null);

            if (entry != null) {
                privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
            } else {
                generateKeyPairWithKeyStore();
                getPrivateKey();
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return privateKey;
    }

    private static PublicKey getPublicKey() {

        PublicKey publicKey = null;
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(defaultAlias, null);

            if (entry != null) {
                publicKey = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
            } else {
                generateKeyPairWithKeyStore();
                getPublicKey();
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static String getPublicKeyStr() {
        PublicKey publicKey = getPublicKey();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
        /*KeyFactory fact = null;
        X509EncodedKeySpec spec = null;
        try {
            fact = KeyFactory.getInstance("RSA");
            spec = fact.getKeySpec(publicKey, X509EncodedKeySpec.class);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        if (spec != null)
            return Base64.getEncoder().encodeToString(spec.getEncoded());
        else
            return null;*/
    }
    
    

    public static String sign(String data) {

        byte[] signature = null;
        try {
            Signature s = Signature.getInstance("SHA256withRSA");
            s.initSign(getPrivateKey());
            s.update(data.getBytes());
            signature = s.sign();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * @param
     * @return
     * @throws JSONException data is not encrypted properly
     */
    public static boolean verify(String data, String signature, String publicKeyStr) throws JSONException {

        byte[] signatureByte = null;
        Signature s = null;
        boolean verified = false;
        try {
            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicSpec);

            s = Signature.getInstance("SHA256withECDSA");
            s.initVerify(publicKey);
            s.update(Base64.getDecoder().decode(data));

            signatureByte = Base64.getDecoder().decode(signature);
            verified = s.verify(signatureByte);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return verified;
    }

    public static String encryptWithPublicKey(String publicKeyStr, String plainText) {

        byte[] data = Base64.getDecoder().decode(publicKeyStr);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        byte[] encryptedByte = null;
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey publicKey = fact.generatePublic(spec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedByte = cipher.doFinal(plainText.getBytes());
        } catch(GeneralSecurityException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encryptedByte);
    }

    public static String decryptWithPrivateKey(String encryptedText) {
        String decryptedText = null;
        try {
            PrivateKey privateKey = getPrivateKey();

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedByte = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

            decryptedText = new String(decryptedByte, "UTF-8");
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
    
    
    private static KeyPair generateKeyPair() {
    	KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	kpg.initialize(2048);
    	return kpg.genKeyPair();
    }
    
    @Test
    public void getPublicKeyNew() {
    	
    	
		
    	KeyPair kp = generateKeyPair();
    	PublicKey key = kp.getPublic();
    	PrivateKey privateKey = kp.getPrivate();
    	String publicStr = Base64.getEncoder().encodeToString(key.getEncoded());
    	System.out.println("public key is " + publicStr);

    	byte[] data = null;
    	byte[] signature = null;
        try {
        	KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    		keyGenerator.init(128);
    		SecretKey secretKey = keyGenerator.generateKey();
    		data = secretKey.getEncoded();
    		
            Signature s = Signature.getInstance("SHA256withRSA");
            s.initSign(privateKey);
            s.update(data);
            signature = s.sign();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        String signatureStr = Base64.getEncoder().encodeToString(signature);
        System.out.println("signature is " + signatureStr);
        
        JSONObject json = new JSONObject();
        try {
            json.put("text", data);
            json.put("sign", signatureStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String dataStr = json.toString();
        String encryptedData = encryptWithPublicKey(publicStr,dataStr);
        
        String decryptedText = null;
        try {

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedByte = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

            decryptedText = new String(decryptedByte, "UTF-8");
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        String decryptedData = decryptWithPrivateKey(encryptedData);
        System.out.println("encrypted data: " + encryptedData);
        System.out.println("decrypted data: " + decryptedData);
        
        

        
    	
    }
    

    public void test() {
        /*generateKeyPairWithKeyStore();
        String publicKey = getPublicKeyStr();
        System.out.println("public key is: " + publicKey);

        String text = "today is a good day";
        String sign = sign(text);
        JSONObject data = new JSONObject();
        try {
            data.put("text", text);
            data.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String dataStr = data.toString();
        String encryptedData = encryptWithPublicKey(publicKey,dataStr);
        String decryptedData = decryptWithPrivateKey(encryptedData);
        System.out.println("encrypted data: " + encryptedData);
        System.out.println("decrypted data: " + decryptedData);

        try {
            JSONObject newData = new JSONObject(decryptedData);
            String textNew = newData.getString("text");
            String signNew = newData.getString("sign");

            boolean verify = verify(textNew,signNew,publicKey);
            System.out.println("verification result: " + String.valueOf(verify));
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/

    }
}
