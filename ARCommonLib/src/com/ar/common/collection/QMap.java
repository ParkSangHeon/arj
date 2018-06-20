/**
 * 2010.6.11
 * - ���� �߰� <QMap(Map)>
 * - setByte() �޼ҵ� ���� ����
 * - json() �޼ҵ� �߰� 
 */
package com.ar.common.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 2010.6.10
 * HashMap의 래퍼 클래스.
 * @author Bak sang heon
 *
 */
public class QMap extends HashMap<String, Object>{
	private static final long serialVersionUID = -8357599344087438022L;
	
	private static String TYPE_STRING 		= String.class.getName();
	private static String TYPE_INTEGER 		= Integer.class.getName();
	private static String TYPE_SHORT		= Short.class.getName();
	private static String TYPE_LONG			= Long.class.getName();
	private static String TYPE_DOUBLE		= Double.class.getName();
	private static String TYPE_FLOAT		= Float.class.getName();
	private static String TYPE_BOOLEAN		= Boolean.class.getName();
	private static String TYPE_CHARACTER	= Character.class.getName();
	private static String TYPE_BYTE			= Byte.class.getName();
	
	
	public QMap(){
	} // QMap()
	
	/**
	 * 2010.6.11
	 * Map을 파라미터로 받아 새로운 QMap을 생성한다.
	 * @param map
	 */
	public QMap(Map<String, ?> map){
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			Object value = (map.get(key));
			if(value.getClass().getName().equals(TYPE_STRING)){
				this.setString(key, (String)value);
			} else if(value.getClass().getName().equals(TYPE_INTEGER)){
				this.setInt(key, (Integer)value);
			} else if(value.getClass().getName().equals(TYPE_SHORT)){
				this.setShort(key, (Short)value);
			} else if (value.getClass().getName().equals(TYPE_LONG)){
				this.setLong(key, (Long)value);
			} else if (value.getClass().getName().equals(TYPE_DOUBLE)){
				this.setDouble(key, (Double)value);
			} else if (value.getClass().getName().equals(TYPE_FLOAT)){
				this.setFloat(key, (Float)value);
			} else if (value.getClass().getName().equals(TYPE_BOOLEAN)){
				this.setBoolean(key, (Boolean)value);
			} else if (value.getClass().getName().equals(TYPE_CHARACTER)){
				this.setChar(key, (Character)value);
			} else if (value.getClass().getName().equals(TYPE_BYTE)){
				this.setByte(key, (Byte)value);
			} // if
		} // while
	} // QMap()
	
	public void setObject(String key, Object value){ this.put(key, value); 	} // setObject()
	public void setString(String key, String value){ this.put(key, value); } // setString()
	public void setInt(String key, Integer value){ this.put(key, value); } // setInt()
	public void setShort(String key, Short value){ this.put(key, value); } // setShort()
	public void setLong(String key, Long value){ this.put(key, value); } // setLong()
	public void setFloat(String key, Float value){ this.put(key, value); } // setFloat()
	public void setDouble(String key, Double value){ this.put(key, value); } // setDouble()
	public void setBoolean(String key, Boolean value){ this.put(key, value); } // setBoolean()
	public void setChar(String key, Character value){ this.put(key, value); } // setChar()
	public void setByte(String key, Byte value){ this.put(key, value); } // setByte()
	
	/**
	 * 2010.6.10 Bak sang heon
	 * HashMap의 get()
	 * @param key
	 * @return
	 */
	public Object getObject(String key){		
		return this.get(key);
	} // get()
	
	/**
	 * 2010.6.10 Bak sang heon
	 * String 형식의 값을 가져온다.
	 * 해당하는 키값이 없으면 NULL을 반환한다.
	 * @param key
	 * @return
	 */
	public String getString(String key){
		Object obj = this.get(key);
		if ( obj == null ) {
			return "";
		} else if ( obj.getClass().getName().equals(TYPE_STRING)){
			return (String)obj;
		} else if ( obj.getClass().getName().equals(TYPE_INTEGER)){
			return Integer.toString((Integer)obj);
		} else if ( obj.getClass().getName().equals(TYPE_SHORT)){
			return Short.toString((Short)obj);
		} else if ( obj.getClass().getName().equals(TYPE_LONG)){
			return Long.toString((Long)obj);
		} else if ( obj.getClass().getName().equals(TYPE_FLOAT)){
			return Float.toString((Float)obj);
		} else if ( obj.getClass().getName().equals(TYPE_DOUBLE)){
			return Double.toString((Double)obj);
		} else if ( obj.getClass().getName().equals(TYPE_BOOLEAN)){
			return Boolean.toString((Boolean)obj);
		} else if ( obj.getClass().getName().equals(TYPE_CHARACTER)){
			return Character.toString((Character)obj);
		} else if ( obj.getClass().getName().equals(TYPE_BYTE)){
			return Byte.toString((Byte)obj);
		} else {
			return "";
		} // if		
	} // getString()
	
	/**
	 * 2010.6.10
	 * Integer형식의 값을 가져온다.
	 * 해당하는 값이 없으면 0을 반환한다.
	 * @param key
	 * @return
	 */
	public int getInt(String key){
		Object obj = this.get(key);
		if (obj == null){
			return 0;
		} else if (obj instanceof java.lang.Number){
			return ((Number)obj).intValue();
		} else if (obj.getClass().getName().equals(TYPE_STRING)){
			return Integer.parseInt((String)obj);
		} else {
			return 0;
		} // if
	} // getInt()
	
	/**
	 * 2010.6.10
	 * Short형식의 값을 가져온다.
	 * 해당하는 값이 없다면 0을 반환한다.
	 * @param key
	 * @return
	 */
	public short getShort(String key){
		Object obj = this.get(key);
		if (obj == null){
			return 0;
		} else if (obj instanceof java.lang.Number){
			return ((Number)obj).shortValue();
		} else if (obj.getClass().getName().equals(TYPE_STRING)){
			return Short.parseShort((String)obj);
		} else {
			return 0;
		} // if
	} // getShort()
	
	/**
	 * 2010.6.10
	 * Long 형식의 값을 반환다.
	 * 해당하는 값이 없으면 0L을 반환한다.
	 * @param key
	 * @return
	 */
	public long getLong(String key){
		Object obj = this.get(key);
		if (obj == null){
			return 0L;			
		} else if (obj instanceof java.lang.Number){
			return ((Number)obj).longValue();
		} else if (obj.getClass().getName().equals(TYPE_STRING)){
			return Long.parseLong((String)obj);
		} else {
			return 0L;
		} // if
	} // getLong()
	
	/**
	 * 2010.6.10
	 * Float형식의 값을 반환한다.
	 * 해당하는 값이 없으면 0.0f를 반환한다.
	 * @param key
	 * @return
	 */
	public float getFloat(String key){
		Object obj = this.get(key);
		if (obj == null){
			return 0.0f;
		} else if (obj instanceof java.lang.Number){
			return ((Number)obj).floatValue();
		} else if (obj.getClass().getName().equals(TYPE_STRING)){
			return Float.parseFloat((String)obj);
		} else {
			return 0.0f;
		} // if
	} // getFloat();
	
	/**
	 * <pre>
	 * 2010.6.10
	 * Double형식의 값을 반환한다.
	 * 해당하는 값이 없으면 0.0을 반환한다.
	 * </pre>
	 * @param key
	 * @return
	 */
	public double getDouble(String key){
		Object obj = this.get(key);
		if (obj == null){
			return 0.0;
		} else if (obj instanceof java.lang.Number){
			return ((Number)obj).doubleValue();
		} else if (obj.getClass().getName().equals(TYPE_STRING)){
			return Double.parseDouble((String)obj);
		} else {
			return 0.0;
		} // if
	} // getDouble()
	
	/**
	 * 2010.6.10
	 * char형식의 값을 반환한다.
	 * 해당하는 값이 없으면 '\0'을 반환한다.
	 * @param key
	 * @return char
	 */
	public char getChar(String key){
		Object obj = this.get(key);
		if (obj == null){
			return '\0';
		} else if (obj.getClass().getName().equals(TYPE_STRING)){
			return (((String)obj).length() > 0) ? ((String)obj).charAt(0) : 0;
		} else if (obj.getClass().getName().equals(TYPE_CHARACTER)){
			return (Character)obj;
		} else if (obj instanceof java.lang.Number){
			return (char)(((Number)obj).shortValue());
		} else {
			return '\0';
		} // if
	} // getChar()
	
	/**
	 * 2010.6.10
	 * byte 값을 반환한다.
	 * 해당하는 값이 없으면 0을 반환한다.
	 * @param key
	 * @return byte
	 */
	public byte getByte(String key){
		Object obj = this.get(key);
		if (obj == null){
			return 0;
		} else if (obj.getClass().getName().equals(TYPE_BYTE)){
			return (Byte)obj;
		} else {
			return 0;
		} // if
	} // getByte()
	
	/**
	 * 2010.6.10
	 * Boolean 형태의 값을 반환한다.
	 * 해당하는 값이 없으면 NULL을 반환한다.
	 * String Type : "true | TRUE" => true , "false | FALSE" => false
	 * Number Type : (!=0) => true , (==0) => false   
	 * @param key
	 * @return Boolean
	 */
	public Boolean getBoolean(String key){
		Object obj = this.get(key);
		if (obj == null){
			return null;
		} else if (obj.getClass().getName().equals(TYPE_BOOLEAN)){
			return (Boolean)obj;
		} else if (obj.getClass().getName().equals(TYPE_STRING)){
			if (((String)obj).toLowerCase().trim().equals("true")) return true;
			else if (((String)obj).toLowerCase().trim().equals("false")) return false;
			else return null;
		} else if (obj instanceof java.lang.Number){
			if (((Number)obj).intValue() != 0) return true;
			else return false;
		} else {
			return null;
		} // if
	} // getBoolean()
	
	
	
	/**
	 * 2010.6.10
	 * toString() 구현체.
	 */
	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer("");
		
		Iterator<String> iter = this.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			buffer.append("[");
			buffer.append(key);
			buffer.append(":");
			if (get(key) != null)
				buffer.append(get(key).toString());
			else 
				buffer.append("NULL");
			buffer.append("]");
		} // while
		
		return buffer.toString();
	} // toString()
	
	/**
	 * 2010.6.11 �ڻ���
	 * ����Ÿ�� JSON ������� ������ش�.
	 * ��Ǵ� ����Ÿ ���ڿ��� toString()�� �������� �Ѵ�.
	 * @return
	 */
	public String json(){
		StringBuffer buffer = new StringBuffer("");		
		
		// head
		buffer.append("{\n");
		
		// body
		Iterator<String> iterator = this.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			buffer.append("\"");
			buffer.append(key);
			buffer.append("\" : ");
			Object value = getObject(key);
			if (value == null) {
				buffer.append("\"");
				buffer.append("NULL");
				buffer.append("\"");
				buffer.append(",\n");
				continue;
			} // if
			if (value.getClass().getName().equals(TYPE_STRING)){
				// String
				buffer.append("\"");
				buffer.append((String)value);
				buffer.append("\"");
			} else if (value instanceof java.lang.Number){
				// Number
				buffer.append((Number)value);
			} else if (value.getClass().getName().equals(QMap.class.getName())){
				buffer.append(((QMap)value).json());
			} else if (value instanceof java.util.Map){
				QMap innerMap = new QMap((Map)value);
				buffer.append(((QMap)innerMap).json());
			} else {
				buffer.append("\"");
				buffer.append(value.toString());
				buffer.append("\"");
			} // if
			if (iterator.hasNext())
				buffer.append(",\n");
		} // while
		
		// foot
		buffer.append("}\n");
		
		return buffer.toString();
	} // json()
	
	
	
	/**
	 * 2010.8.26 Bak Sang Heon
	 * �ʿ� �ִ� ����Ÿ�� �ش� Bean ��ü�� setter�޼ҵ带 ���� �Ҵ��Ѵ�.
	 * @param <T>
	 * @param bean
	 * @return
	 */
	public void model(Object bean){
		QKeyValue kv = new QKeyValue(bean);		
		Iterator<String> iter = this.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			Object value = this.get(key);
			kv.set(key, value);
		} // while	
	} // model()
	
	
	
} // class QMap