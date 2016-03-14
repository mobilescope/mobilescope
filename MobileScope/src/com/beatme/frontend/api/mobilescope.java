package com.beatme.frontend.api;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: Nov 5, 2011
 * Time: 10:07:04 PM
 * To change this template use File | Settings | File Templates.
 */


import com.beatme.restclient.http.HttpCreater;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class mobilescope {
    HttpCreater hc = new HttpCreater();
    public  String encryPassword(String userID){
         return formUserAuth(userID);
    }

    private String formUserAuth(String userID){

        String  userKey = encryptString(userID);
        System.out.println("userkey:"+userKey);
        String  encryUserid= encryptString(userKey.toLowerCase()+hc.getlicKey());
        System.out.println("userkey:"+encryUserid);
        return encryUserid;
    }



private String encryptString(String Key){

	byte[] defaultBytes = Key.getBytes();
	try {
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();

		StringBuffer hexString = new StringBuffer();
		for (int co = 0; co < messageDigest.length; co++) {
				int i = 0xFF & messageDigest[co];
				if (i < 16) {
					hexString.append('0');
				}
					hexString.append(Integer.toHexString(i));
		}
			return hexString.toString().toLowerCase();
		}
	catch (NoSuchAlgorithmException nsae){
		System.out.println("Failed to encrypt the String");
	}
	return Key;
}


}
