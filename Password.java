package banksystem;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Random;
public class Password
{
	 public byte[] getHash(String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	 {
	       MessageDigest digest = MessageDigest.getInstance("SHA-1");
	       digest.reset();
	       digest.update(salt);
	       return digest.digest(password.getBytes("UTF-8"));
	       
	 }
	public byte[] getSalt()
	{
		Random ranGen = new SecureRandom();
		byte[] aesKey = new byte[16]; // 16 bytes = 128 bits
		ranGen.nextBytes(aesKey);
		
		return aesKey;
	}/*
	public String hashPassword(String passwordToHash) throws NoSuchProviderException
	{
		StringBuffer sb = null;
		String generatedPassword = null;

		//hash password
	    try {
	    	// Create MessageDigest instance for MD5
	        MessageDigest md = MessageDigest.getInstance("SHA-1");
	        //Add password bytes to digest
	        md.update(passwordToHash.getBytes());
	        //Get the hash's bytes
	        byte[] bytes = md.digest();
	        //This bytes[] has bytes in decimal format;
	        //Convert it to hexadecimal format
	        sb = new StringBuffer();
	        for(int i=0; i< bytes.length ;i++)
	        {
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        //Get complete hashed password in hex format
	        generatedPassword = sb.toString();        
	    }
	    
	    catch (NoSuchAlgorithmException e){
	        e.printStackTrace();
	    }
	    return generatedPassword;
	    
	}*/
}



