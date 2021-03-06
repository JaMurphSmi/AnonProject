package org.anonymize.anonymizationapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

//small utility class for certain issues related to the file structure and directory creation
@Component
public class FileStructureAspects {
	
	//private String secret = "this1$4d3faults3cr€tinmy4ppl1cat10n";
	
	
	//make the user's unique file more secure through hashing the provided empOrg
	/*public S   
	MessageDigest digest;
		try {
			empOrg = "1extra" + empOrg + "padding1";
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(
				     empOrg.getBytes(StandardCharsets.UTF_8));
			empOrg = new String(encodedhash);
			empOrg = "1ja4%" + empOrg + "$1nfdh";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}*/ //doesn't work as directory
	
	public String toHex(String employeeOrganization) {
	    return String.format("%040x", new BigInteger(1, employeeOrganization.getBytes(StandardCharsets.UTF_8)));
	}
	
	
	public void makeDirectory(File dir) {
		boolean successful = dir.mkdir();
		if (successful)
	    {
	      // creating the directory succeeded
	      System.out.println("Directory was created safely and successfully");
	    }
	    else
	    {
	      // creating the directory failed
	      System.out.println("Failed trying to create unique directory");
	    }
	}

	public void fileEnDeCrypt(int cipherMode,String secret, File inputFile,File outputFile){
		 try {
		       Key secretKey = new SecretKeySpec(secret.getBytes(), "AES");
		       Cipher cipher = Cipher.getInstance("AES");
		       cipher.init(cipherMode, secretKey);
		       
		       FileInputStream inputStream = new FileInputStream(inputFile);
		       byte[] inputBytes = new byte[(int) inputFile.length()];
		       inputStream.read(inputBytes);

		       byte[] outputBytes = cipher.doFinal(inputBytes);

		       FileOutputStream outputStream = new FileOutputStream(outputFile);
		       outputStream.write(outputBytes);

		       inputStream.close();
		       outputStream.close();

		    } catch (NoSuchPaddingException | NoSuchAlgorithmException 
	                     | InvalidKeyException | BadPaddingException
		             | IllegalBlockSizeException | IOException e) {
			e.printStackTrace();
	            }
	     }
	
	//only a setter method as we do not want any access 
	/*public void setSecret(String secret) {
		this.secret = secret;
	}*/
}
