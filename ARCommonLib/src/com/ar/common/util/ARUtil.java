package com.ar.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import com.ar.common.logging.ARLog;

/************************
 * <pre>
 * 유틸리티 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ************************/
public class ARUtil {
	private static ARLog log = ARLog.getLogger(ARUtil.class);
	
	/********************************
	 * <pre>
	 * Byte데이타를 Hex문자열로 변환한다.
	 * </pre>
	 * @param value
	 * @return
	 ********************************/
	public static String byte2hex(byte[] value) {
		if (value == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < value.length; i++) {
			int j = (value[i] >> 4) & 0xf;
			if (j <= 9) {
				sb.append(j);
			} else {
				sb.append((char) (j + 'a' - 10));
			}
			j = value[i] & 0xf;
			if (j <= 9) {
				sb.append(j);
			} else {
				sb.append((char) (j + 'a' - 10));
			}
		}
		return sb.toString();
	} // byte2hex()
	
	/********************************
	 * <pre>
	 * Hex 문자열을 Byte배열로 변환한다.
	 * </pre>
	 * @param hex
	 * @return
	 ********************************/
	public static byte[] hex2byte(String hex) {
    	if (hex == null || hex.length() <= 0)
    		return null;
    	
    	byte[] bytes = new byte[hex.length() / 2];
    	for (int i = 0; i < bytes.length; i++) {
    		bytes[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
    	} // for
    	
    	return bytes;
    } // hex2byte()
	
	/**********************************
	 * <pre>
	 * Byte데이타를 Integer형태로 변환한다.
	 * </pre>
	 * @param data
	 * @return
	 **********************************/
	public static int byte2int(byte[] data) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer = ByteBuffer.wrap(data);
		return buffer.getInt();
	} // byte2int()
	
	/*****************************
	 * <pre>
	 * 문자열을 Byte데이타로 변환한다.
	 * </pre>
	 * @param str
	 * @param length
	 * @return
	 *****************************/
	public static byte[] str2byte(String str, int length) {
		byte[] dest = new byte[length];
		byte[] src = str.getBytes();
		if (src.length >= dest.length) {
			System.arraycopy(src, 0, dest, 0, dest.length);
		} else if (src.length < dest.length) {
			System.arraycopy(src, 0, dest, 0, src.length);
		} // if
		return dest;
	} // str2byte()
		
	/******************************
	 * <pre>
	 * 포멧팅된 날짜 문자열을 생성한다.
	 * </pre>
	 * @param date
	 * @param format
	 * @return
	 ******************************/
	public static String formattedDate(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String formattedDate = formatter.format(date);
		
		return formattedDate;
	} // formattedDate()
	
	/************************************
	 * <pre>
	 * 포멧팅된 문자열에서 Date값을 추출한다.
	 * </pre>
	 * @param format
	 * @param date
	 * @return
	 ************************************/
	public static Date dateFromFormatString(String format, String src) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = formatter.parse(src);
		} catch (ParseException e) {
			log.error(e);
		} // try
		return date;
	} // dateFromFormatString()

	/*************************************
	 * <pre>
	 * 문자열을 특정 케릭터로 좌측으로 패딩한다.
	 * </pre>
	 * @param src
	 * @param length
	 * @param padding
	 * @return
	 *************************************/
	public static String paddingLeft(String src, int length, char padding) {
		if (src.length() == length) {
			return src;
		} else if (src.length() > length) {
			return src.substring(src.length() - length, src.length());
		} else if (src.length() < length) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < length - src.length(); i++) buffer.append(padding);
			buffer.append(src);
			return buffer.toString();
		} else {
			return null;
		} // if
	} // paddingLeft()
	
	/*************************************
	 * <pre>
	 * 문자열을 특정 케릭터로 우측으로 패딩한다.
	 * </pre>
	 * @param src
	 * @param length
	 * @param padding
	 * @return
	 *************************************/
	public static String paddingRight(String src, int length, char padding) {
		if (src.length() == length) {
			return src;
		} else if (src.length() > length) {
			return src.substring(0, length);
		} else if (src.length() < length) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(src);
			for (int i = 0; i < length - src.length(); i++) buffer.append(padding);
			return buffer.toString();
		} else {
			return null;
		} // if
	} // paddingRight()
	
	
	public static void putInt2Byte(int src, byte[] desc) throws Exception{
		String srcStr = Integer.toString(src);
		byte[] tmp = srcStr.getBytes("US-ASCII");
		System.arraycopy(tmp, 0, desc, 0, desc.length);
	} // putInt2Byte()
	
	public static void putStr2Byte(String src, byte[] desc) throws Exception {
		byte[] tmp = src.getBytes("US-ASCII");
		System.arraycopy(tmp, 0, desc, 0, desc.length);
	} // putStr2Byte()
	
	/*********************************
	 * <pre>
	 * SHA1 해시 생성
	 * </pre>
	 * @param input
	 * @return
	 * @throws Exception
	 *********************************/
	public static String sha1(String input) {
		String result = null;
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			byte[] encData = messageDigest.digest(input.getBytes());
			byte[] base64Data = Base64.encodeBase64(encData);
			result = new String(base64Data);
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
		} // try
		
		return result;
	} // sha1()
	
	/*********************************
	 * <pre>
	 * SHA256 해시 생성
	 * </pre>
	 * @param input
	 * @return
	 * @throws Exception
	 *********************************/
	public static String sha256(String input) {
		String result = null;
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] encData = messageDigest.digest(input.getBytes());
			//result = Base64.encodeBase64String(encData);
			byte[] base64Data = Base64.encodeBase64(encData);
			result = new String(base64Data);
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
		} // try
		
		return result;
	} // sha1()
	
	/**********************************
	 * <pre>
	 * 파일에 대한 MD5 해시를 생성한다.
	 * </pre>
	 * @param file
	 * @return
	 * @throws Exception
	 **********************************/
	public static String md5(File file) throws Exception {
		String result = null;
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		int read = 0;
		byte[] buffer = new byte[256];
		while(true) {
			read = in.read(buffer);
			if (read <= 0) break;
			md.update(buffer, 0, read);
		} // while
		
		in.close();
		
		byte[] data = md.digest();
		result = Base64.encodeBase64String(data);
		
		return result;
	} // md5()
	
	public static String createRandomString(int len) {
		char[] array = new char[]{
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'	
		};
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < len; i++) {
			Random r = new Random();
			int d = r.nextInt(array.length);
			builder.append(array[d]);
		} // for
		
		return builder.toString();
	} // createRandomString()
	
	public static void main(String[] args) {
		String str = ARUtil.createRandomString(100);
		System.out.println(str);
	}
} // class ARUtil
