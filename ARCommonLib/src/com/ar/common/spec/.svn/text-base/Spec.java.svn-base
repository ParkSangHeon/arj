package com.ar.common.spec;

/*******************************
 * <pre>
 * 전문 구현을 위한 최상의 인터페이스
 * </pre>
 * @author Bak Sang Heon
 *
 *******************************/
public interface Spec {
	
	/**
	 * 해당 필드의 바이트 배열순서를 변경한다. BIG_ENDIAN <-> LITTLE_ENDIAN
	 * @param idx 필드번호
	 */
	public void flip(int idx);
	/**
	 * 해당 필드의 바이트 배열순서를 변경한다. BIG_ENDIAN <-> LITTLE_ENDIAN
	 * @param name 필드명 
	 */
	public void flip(String name);
	
	/**
	 * 바이트 배열을 이용한 전문 생성
	 * @param data 바이트배열
	 */
	public void setStream(byte[] data);
	
	/**
	 * 바이트 배열중 헤더를 제외한 몸체만 넣는다.
	 * @param data 헤더12바이트를 제외한 바이트 배열
	 */
	public void setStreamBody(byte[] data);
	
	/**
	 * 전문에 대한 바이트배열을 가져온다
	 * @return
	 */
	public byte[] getStream();
	
	/**
	 * 전문 Body에 대한 바이트배열을 가져온다.
	 * @return
	 */
	public byte[] getStreamBody();
	
	/**
	 * 전문 코드를 가져온다.
	 * @return
	 */
	public String getCode(); 
	/**
	 * 전문 길이를 가져온다.
	 * @return
	 */
	public int getLength(); 
	/**
	 * 해당 필드 길이를 가져온다.
	 * @param idx
	 * @return
	 */
	public int getLength(int idx); 
	/**
	 * 해당 필드 길이를 가져온다.
	 * @param name
	 * @return
	 */
	public int getLength(String name);
	/**
	 * 전문 버전을 가져온다.
	 * @return
	 */
	public int getVersion(); 
	
	public void setBytes(int idx, byte[] data);
	public void setBytes(String name, byte[] data);
	public void setInt(int idx, int data);
	public void setInt(String name, int data);
	public void setLong(int idx, long data);
	public void setLong(String name, long data);
	public void setShort(int idx, short data);
	public void setShort(String name, short data);
	public void setFloat(int idx, float data);
	public void setFloat(String name, float data);
	public void setDouble(int idx, double data);
	public void setDouble(String name, double data);
	public void setChars(int idx, char[] data);
	public void setChars(String name, char[] data);
	public void setString(int idx, String data);
	public void setString(String name, String data);
	
	public byte[] getBytes(int idx);
	public byte[] getBytes(String name);
	public int getInt(int idx);
	public int getInt(String name);
	public long getLong(int idx);
	public long getLong(String name);
	public short getShort(int idx);
	public short getShort(String name);
	public float getFloat(int idx);
	public float getFloat(String name);
	public double getDouble(int idx);
	public double getDouble(String name);
	public char[] getChars(int idx);
	public char[] getChars(String name);
	public String getString(int idx);
	public String getString(String name);
} // interface Spec
