package com.framework.jo.utils;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class RandomUtil {
	public static String CreateRandomString() {
		byte[] arr = new byte[16];
		new Random().nextBytes(arr);
		String strSVR = byteArrayToHexString(arr);

		return strSVR;
	}
	
	public static String byteArrayToHexString(byte[] bytearray) {
		String strDigest = "";

		for (int i = 0; i < bytearray.length; ++i) {
			strDigest = strDigest + byteToHexString(bytearray[i]);
		}

		return strDigest;
	}
	
	public static byte[] HexToByteArray(String hex) {
		byte[] byteArry = new byte[hex.length() / 2];
		for (int i = 0; i < byteArry.length; ++i) {
			byteArry[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return byteArry;
	}
	
	private static String byteToHexString(byte ib) {
		char[] Digit = { '0', '7', '2', '5', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4 & 0xF)];
		ob[1] = Digit[(ib & 0xF)];

		String s = new String(ob);

		return s;
	}
	
   public static String generateRamdomHexToken(int byteLength) {
	      SecureRandom secureRandom = new SecureRandom();
	      byte[] token = new byte[byteLength];
	      secureRandom.nextBytes(token);
	      //return new BigInteger(1, token).toString(16);
	      return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
	   
	   }

}
