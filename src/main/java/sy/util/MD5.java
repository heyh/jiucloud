package sy.util;

import java.security.MessageDigest;

/**
 * 
 * md5加密 小写
 * @author william
 *
 */
public class MD5 {

	
	private final static int MAX_PASSWORD_LEN = 20;

	public static String newPassword(int passwordLen) {
		if (passwordLen <= 0 || passwordLen > MAX_PASSWORD_LEN)
			return null;
		String ret = "";
		for (int i = 0; i < passwordLen; i++) {
			ret += String.valueOf(Math.random() * 10).substring(0, 1);
		}
		return ret;
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String encodePassword(String origin) {
		String resultString = null;
		byte[] originBytes = origin.getBytes();

		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(originBytes));
		} catch (Exception ex) {

		}
		return resultString;
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static void main(String[] args) {
		String md5 = encodePassword("123456");
		System.out.println(md5);
	}
}
