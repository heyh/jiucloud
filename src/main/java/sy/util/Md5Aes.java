/*
 * EncryptUtil.java
 * 版权所有：江苏电力信息技术有限公司 2007 - 2013
 * 江苏电力信息技术有限公司保留所有权利，未经允许不得以任何形式使用。
 */
package sy.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密解密类
 * <p>
 * 创建日期：2013-4-11<br>
 * 修改历史：<br>
 * 修改日期：<br>
 * 修改作者：<br>
 * 修改内容：<br>
 * 
 * @author Administrator
 * @version 1.0
 */
public class Md5Aes {

	/**
	 * 构造方法
	 */
	private Md5Aes() {

	};

	/**
	 * 16
	 */
	private static final int INT16 = 16;

	/**
	 * 0xff
	 */
	private static final int INTFF = 0xff;

	/**
	 * MD5加密方法
	 * 
	 * @param source
	 *            明文
	 * @return 加密后的密文
	 */
	public static String md5Encrypt(String source) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			// throw new ApplicationException("无法获得MD5加密实例:"+e.getMessage(),e);
		}
		char[] charArray = source.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];

		StringBuffer hexValue = new StringBuffer();

		if (md5 != null) {
			byte[] md5Bytes = md5.digest(byteArray);

			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & INTFF;
				if (val < INT16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
		}

		return hexValue.toString();
	}

	public static String calMd5(String src) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(src.getBytes("utf8"));
			byte[] re = md.digest();
			return toHexStrign(re);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String toHexStrign(byte[] buf) {
		char[] hex = "0123456789abcdef".toCharArray();
		int nbyte = buf.length;
		char[] result = new char[nbyte * 2];
		for (int i = 0; i < nbyte; i++) {

			result[i * 2] = hex[(0xf0 & buf[i]) >>> 4];
			result[i * 2 + 1] = hex[(0x0f & buf[i])];
		}
		return new String(result);
	}

	public static void main(String[] args) {
		System.out.println(md5Encrypt("大家好"));
		System.out.println(calMd5("大家好"));
	}
}
