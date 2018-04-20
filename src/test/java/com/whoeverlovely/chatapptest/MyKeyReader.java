package com.whoeverlovely.chatapptest;

import org.keyczar.DefaultKeyType;
import org.keyczar.KeyMetadata;
import org.keyczar.KeyVersion;
import org.keyczar.enums.KeyPurpose;
import org.keyczar.enums.KeyStatus;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.interfaces.KeyczarReader;

public class MyKeyReader implements KeyczarReader{
	String aesKeyStr;
	private final KeyMetadata metadata;
	
	public MyKeyReader(String aesKeyStr) {
		this.metadata = new KeyMetadata(
	            "My Reader", KeyPurpose.DECRYPT_AND_ENCRYPT, DefaultKeyType.AES);
	    KeyVersion version = new KeyVersion(0, KeyStatus.PRIMARY, false);
	    this.metadata.addVersion(version);
		this.aesKeyStr = aesKeyStr;
	}

	public String getKey(int version) throws KeyczarException {
		// TODO Auto-generated method stub
		return aesKeyStr;
	}

	public String getKey() throws KeyczarException {
		return aesKeyStr;
	}

	public String getMetadata() throws KeyczarException {
		return metadata.toString();
	}

}
