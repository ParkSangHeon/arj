package com.ar.common.spec;

import java.io.UnsupportedEncodingException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Arrays;

import com.ar.common.logging.ARLog;
import com.ar.common.util.ARUtil;
import com.ar.common.xml.ARTag;

/****************************
 * <pre>
 * Spec 인터페이스에 대한 구현체
 * </pre>
 * @author Bak Sang Heon
 *
 ****************************/
public class SpecImpl implements Spec{
	private static ARLog log = ARLog.getLogger(Spec.class);

	private static enum TYPE {
		AN, // Alpha Number
		NB, // Number
		HN, // HexNumber
		BN,  // Binary
		ST  // Unicode String
	} // enum TYPE
	
	private static enum PADDING {
		LEFT,
		RIGHT
	} // enum PADDING
	
	private static final char PADDING_SPACE = ' ';
	private static final char PADDING_ZERO = '0';
	
	private static final PADDING PADDING_AN = PADDING.LEFT;
	private static final PADDING PADDING_NB = PADDING.RIGHT;
	private static final PADDING PADDING_HN = PADDING.RIGHT;
	private static final PADDING PADDING_BN = PADDING.LEFT;
	private static final PADDING PADDING_ST = PADDING.LEFT;
	
	private static final String NULL_ZERO = "ZERO";
	private static final String NULL_SPACE = "SPACE";
	private static final String NULL_NULL = "NULL";
	
	private static final String ASCII = "US-ASCII";
	
	// Character Encoding
	
	private byte[] spec_data;
	private Field[] fields;
	private String code;
	private int length;
	private int version;
	private String encoding;
	
	/*************************************
	 * <pre>
	 * 생성자
	 * </pre>
	 * @param spec_tag : 전문의 Tag 클래스
	 *************************************/
	public SpecImpl(ARTag spec_tag) {
		String code = spec_tag.getAttribute("code");
		this.code = code;
		
		String ver_str = spec_tag.getAttribute("version");
		this.version = Integer.valueOf(ver_str);
		
		String encoding = spec_tag.getAttribute("encoding");
		if (encoding == null)
			this.encoding = "KSC5601";
		else 
			this.encoding = encoding;
		
		List<ARTag> params = spec_tag.getChildren();
		this.fields = new Field[params.size()];
		for (int i = 0; i < params.size(); i++) {
			int offset = this.length;
			ARTag param_tag = params.get(i);
			String param_name = param_tag.getAttribute("name");
			
			String length_str = param_tag.getAttribute("length");
			int length= Integer.valueOf(length_str).intValue();
			this.length += length;
			
			String type_str = param_tag.getAttribute("type");
			TYPE type = TYPE.AN;
			if (type_str.equals("AN")) type = TYPE.AN;
			else if (type_str.equals("BN")) type = TYPE.BN;
			else if (type_str.equals("NB")) type = TYPE.NB;
			else if (type_str.equals("HN")) type = TYPE.HN;
			else if (type_str.equals("ST")) type = TYPE.ST;
			
			Field field = new Field(param_name, length, type, offset);
			this.fields[i] = field;
			//log.debug("[name:" + param_name + "][length:" + length +"]");
		} // for
		
		//log.debug("### Created Spec Length : " + this.length);
		
		this.spec_data = new byte[this.length];
		String null_field = spec_tag.getAttribute("null_field");
		if (null_field == null) null_field = NULL_SPACE;
		for (int i = 0; i < this.spec_data.length; i++) {
			if (null_field.equals(NULL_ZERO)) {
				this.spec_data[i] = '0';
			} else if (null_field.equals(NULL_SPACE)) {
				this.spec_data[i] = ' ';
			} else if (null_field.equals(NULL_NULL)) {
				this.spec_data[i] = '\0';
			} // if
		}
	} // Constructor()
	
	/*******************************
	 * <pre>
	 * 전문 내부 필드를 정의하는 클래스.
	 * </pre>
	 * @author Bak Sang Heon
	 *
	 *******************************/
	class Field {
		private String name;
		private int length;
		private TYPE type;
		private int offset;
		private PADDING padding;
		private char padding_char;
		
		public Field(String name, int length, TYPE type, int offset) {
			this.name = name;
			this.length = length;
			this.type = type;
			this.offset = offset;
			
			if (type == TYPE.AN) {
				this.padding = PADDING_AN;
				this.padding_char = PADDING_SPACE;
			} else if (type == TYPE.BN) { 
				this.padding = PADDING_BN;
				this.padding_char = PADDING_SPACE;
			} else if (type == TYPE.HN) {
				this.padding = PADDING_HN;
				this.padding_char = PADDING_ZERO;
			} else if (type == TYPE.NB) {
				this.padding = PADDING_NB;
				this.padding_char = PADDING_ZERO;
			} else if (type == TYPE.ST) {
				this.padding = PADDING_ST;
				this.padding_char = PADDING_SPACE;
			}
		} // Constructor
	} // class param

	public void flip(int idx) {
		try {
			Field field = this.fields[idx];
			
			byte[] byte_data = new byte[field.length];
			System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
			
			ByteBuffer buffer = ByteBuffer.allocate(field.length);
			buffer = ByteBuffer.wrap(byte_data);
			if (buffer.order() == ByteOrder.BIG_ENDIAN) {
				buffer.order(ByteOrder.LITTLE_ENDIAN);
			} else if (buffer.order() == ByteOrder.LITTLE_ENDIAN) {
				buffer.order(ByteOrder.BIG_ENDIAN);
			} // if
			
			byte_data = buffer.array();
			System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
		} catch (Exception e) {
			log.error("Spec.flip()", e);
		} // try
	} // flip()
	
	public void flip(String name) {
		int idx = this.getIndexByName(name);
		this.flip(idx);
	} // flip()

	public void setStream(byte[] data){
		try {
			if (data.length != (this.length + 12)) {
				throw new Exception("Invalid data length Spec("+ this.code + ") Length(" + this.length + ") : Data Length(" + data.length + ")");
			} // if
			
			byte[] code_bytes = new byte[4];
			System.arraycopy(data, 0, code_bytes, 0, 4);
			String code_string = new String(code_bytes);
			if (!code_string.equals(this.code))
				throw new Exception("Invalid Spec Code (" + this.code + " : " + code_string + ")");
			
			byte[] hex_bytes = new byte[8];
			System.arraycopy(data, 4, hex_bytes, 0, 8);
			byte[] length_bytes = this.hex2byte(new String(hex_bytes));
			//log.debug("Length Bytes : " + length_bytes.length);
			
			ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
			buffer = ByteBuffer.wrap(length_bytes);
			int data_len = buffer.getInt();
			if (this.length != data_len)
				throw new Exception("Invalid Data Length (" + this.length + " : " + data_len + ")");
			
			System.arraycopy(data, 12, this.spec_data, 0, this.length);
		} catch (Exception e) {
			log.error("Spec.setStream()",e);
		} // try
	} // setStream();
	
	/*******************************************
	 * <pre>
	 * 바이트 배열중 헤더를 제외한 몸체만 넣는다.
	 * </pre>
	 * @param data 헤더12바이트를 제외한 바이트 배열
	 *******************************************/
	public void setStreamBody(byte[] data) {
		System.arraycopy(data, 0, this.spec_data, 0, this.length);
	} // setStreamBody

	public byte[] getStream() {
		try {
			byte[] byte_data = new byte[this.spec_data.length + 12];
			
			String code_string = this.code;
			code_string = this.padding(code_string, 4, PADDING.RIGHT, PADDING_SPACE);
			byte[] code_bytes = code_string.getBytes(ASCII);
			//log.debug("Code Bytes Length : " + code_bytes.length);
			
			String hex_length = Integer.toHexString(this.length);
			hex_length = this.padding(hex_length, 8, PADDING.RIGHT, PADDING_ZERO);
			//log.debug("Length Hex [" + hex_length + "]");
			byte[] length_bytes = hex_length.getBytes(ASCII);
			//log.debug("Length Bytes Length : " + length_bytes.length);
			
			System.arraycopy(code_bytes, 0, byte_data, 0, 4);
			System.arraycopy(length_bytes, 0, byte_data, 4, 8);
			System.arraycopy(this.spec_data, 0, byte_data, 12, this.length);
			return byte_data;
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		} // try
		
		return null;
	} // getStream()
	
	public byte[] getStreamBody() {
		return this.spec_data;
	} // getStreamBody()

	public String getCode() {
		return this.code;
	} // getCode()

	public int getLength() {
		return this.length;
	} // getLength()

	public int getLength(int idx) {
		try {
			Field field = this.fields[idx];
			if (field == null) throw new Exception("해당 필드가 없습니다");
			return field.length;
		} catch (Exception e) {
			log.error("Spec.getLength(int idx)", e);
		} // try
		return -1;
	} // getLength()
	
	public int getLength(String name) {
		int idx = this.getIndexByName(name);
		return this.getLength(idx);
	} // getLength()
	
	public int getVersion() {
		return this.version;
	} // getVersion()

	/**
	 * @param
	 * 		idx : 필드 인덱스
	 * 		data : 바이트 데이타
	 */
	public void setBytes(int idx, byte[] data){
		try {
			Field field = this.fields[idx];
			if (field.length < data.length) {
				throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + data.length);
			} // if
			byte[] byte_data = new byte[field.length];
			System.arraycopy(data, 0, byte_data, 0, data.length);
			System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
		} catch (Exception e) {
			log.error("Spec.setBytes()", e);
		} // try
	} // setBytes()

	public void setBytes(String name, byte[] data) {
		int idx = this.getIndexByName(name);
		this.setBytes(idx, data);
	} // setBytes()

	public void setInt(int idx, int data) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				// Alpha Numeric
				String str_data = Integer.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.length());
				} else if (field.length > str_data.getBytes(ASCII).length) {
					str_data = this.padding(str_data, field.length, field.padding, PADDING_SPACE);
				} // if
				byte[] tmp_data = str_data.getBytes(ASCII);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);

			} else if (field.type == TYPE.BN) {
				// Binary
				byte[] byte_data = this.int2bin(data);
				if (field.length != 4) {
					throw new Exception("Integer binary length should be 4");
				} else if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.HN) {
				// Hex Number
				String hex_data = Integer.toHexString(data);
				if (field.length < hex_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + hex_data.getBytes(this.encoding).length);
				} else if (field.length > hex_data.getBytes(ASCII).length) {
					// Padding
					hex_data = this.padding(hex_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] tmp_data = hex_data.getBytes(ASCII);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.NB) {
				// Number
				String str_data = Integer.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.length());
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] tmp_data = str_data.getBytes(ASCII);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, tmp_data.length - byte_data.length, byte_data, 0, field.length);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
				
			} else if (field.type == TYPE.ST) {
				// String
				String str_data = Integer.toString(data);
				if (field.length < str_data.getBytes(this.encoding).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.length());
				} // if
				byte[] tmp_data = str_data.getBytes(this.encoding);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);

			} // if
		} catch (Exception e) {
			log.error("Spec.setInt()", e);
		} // try
	} // setInt()

	public void setInt(String name, int data) {
		int idx = this.getIndexByName(name);
		this.setInt(idx, data);
	} // setInt

	public void setLong(int idx, long data) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				// Alpha Numeric
				String str_data = Long.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(ASCII).length);
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = str_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.NB) {
				// Numeric
				String str_data = Long.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(ASCII).length);
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = str_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.ST) {
				// String
				String str_data = Long.toString(data);
				if (field.length < str_data.getBytes(this.encoding).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(this.encoding).length);
				} // if
				byte[] tmp_data = str_data.getBytes(this.encoding);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.BN) {
				// Binary
				byte[] byte_data = this.long2bin(data);
				if (field.length != 8) {
					throw new Exception("Long binary length should be 8");
				} else if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.HN) {
				// Hex Number
				String hex_data = Long.toHexString(data);
				if (field.length < hex_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + hex_data.getBytes(ASCII).length);
				} else if (field.length > hex_data.getBytes(ASCII).length) {
					// Padding
					hex_data = this.padding(hex_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = hex_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} // if
		} catch (Exception e) {
			log.error("Spec.setLong()", e);
		} // try
	} // setLong()

	public void setLong(String name, long data) {
		int idx = this.getIndexByName(name);
		this.setLong(idx, data);
	} // setLong()

	public void setShort(int idx, short data) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				// Alpha Numeric
				String str_data = Short.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(ASCII).length);
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = str_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.NB) {
				// Numeric
				String str_data = Short.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(ASCII).length);
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = str_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.ST) {
				String str_data = Short.toString(data);
				if (field.length < str_data.getBytes(this.encoding).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(this.encoding).length);
				} // if
				byte[] tmp_data = str_data.getBytes(this.encoding);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);	
			} else if (field.type == TYPE.BN) {
				// Binary
				byte[] byte_data = this.short2bin(data);
				if (field.length != 2) {
					throw new Exception("Short binary length should be 2");
				} else if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.HN) {
				// Hex Numeric
				String hex_data = Integer.toHexString(data);
				if (field.length < hex_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + hex_data.getBytes(ASCII).length);
				} else if (field.length > hex_data.getBytes(ASCII).length) {
					// Padding
					hex_data = this.padding(hex_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = hex_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} // if
		} catch (Exception e) {
			log.error("Spec.setShort()", e);
		} // try
	} // setShort()

	public void setShort(String name, short data) {
		int idx = this.getIndexByName(name);
		this.setShort(idx, data);
	} // setShort()

	public void setFloat(int idx, float data) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				// Alpha Numeric
				String str_data = Float.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(ASCII).length);
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = str_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.NB) {
				// Numeric
				String str_data = Float.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(ASCII).length);
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = str_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.ST) {
				// String
				String str_data = Float.toString(data);
				if (field.length < str_data.getBytes(this.encoding).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(this.encoding).length);
				} else if (field.length > str_data.getBytes(this.encoding).length) {
					str_data = this.padding(str_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] tmp_data = str_data.getBytes(this.encoding);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.BN) {
				// Binary
				byte[] byte_data = this.float2bin(data);
				if (field.length != 4) {
					throw new Exception("Float binary length should be 4");
				} else if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.HN) {
				// Hex Numeric
				ByteBuffer buffer = ByteBuffer.allocate(Float.SIZE / 8);
				buffer.putFloat(data);
				byte[] float_data = buffer.array();
				String hex_data = this.byte2hex(float_data);
				if (field.length < hex_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + hex_data.getBytes(ASCII).length);
				} else if (field.length > hex_data.getBytes(ASCII).length) {
					// Padding
					hex_data = this.padding(hex_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = hex_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} // if
		} catch (Exception e) {
			log.error("Spec.setFloat()", e);
		} // try
	} // setFloat()

	public void setFloat(String name, float data) {
		int idx = this.getIndexByName(name);
		this.setFloat(idx, data);
	} // setFloat()

	public void setDouble(int idx, double data) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				// Alpha Numeric
				String str_data = Double.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(ASCII).length);
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, PADDING_SPACE);
				} // if
				byte[] byte_data = str_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.NB) {
				// Numeric
				String str_data = Double.toString(data);
				if (field.length < str_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(ASCII).length);
				} else if (field.length > str_data.getBytes(ASCII).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, PADDING_ZERO);
				} // if
				byte[] byte_data = str_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.ST) {
				// String
				String str_data = Double.toString(data);
				if (field.length < str_data.getBytes(this.encoding).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + str_data.getBytes(this.encoding).length);
				} else if (field.length > str_data.getBytes(this.encoding).length) {
					// Padding
					str_data = this.padding(str_data, field.length, field.padding, PADDING_SPACE);
				} // if
				byte[] tmp_data = str_data.getBytes(this.encoding);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.BN) {
				// Binary
				byte[] byte_data = this.double2bin(data);
				if (field.length != 8) {
					throw new Exception("Double binary length should be 8");
				} else if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} else if (field.type == TYPE.HN) {
				// Hex Number
				ByteBuffer buffer = ByteBuffer.allocate(Double.SIZE / 8);
				buffer.putDouble(data);
				byte[] double_data = buffer.array();
				String hex_data = this.byte2hex(double_data);
				if (field.length < hex_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + hex_data.getBytes(ASCII).length);
				} else if (field.length > hex_data.getBytes(ASCII).length) {
					// Padding
					hex_data = this.padding(hex_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = hex_data.getBytes(ASCII);
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
				
			} // if
		} catch (Exception e) {
			log.error("Spec.setDouble()", e);
		} // try
	} // setDouble()

	public void setDouble(String name, double data) {
		int idx = this.getIndexByName(name);
		this.setDouble(idx, data);
	} // setDouble()

	public void setChars(int idx, char[] data) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				String field_data = new String(data);
				if (field.length < field_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + field_data.getBytes(ASCII).length);
				} else if (field.length > field_data.getBytes(ASCII).length){
					field_data = this.padding(field_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] tmp_data = field_data.getBytes(ASCII);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, field.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.BN) {
				String field_data = new String(data);
				field_data = this.padding(field_data, field.length, field.padding, field.padding_char);
				byte[] byte_data = field_data.getBytes();
				if (field.length != byte_data.length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + byte_data.length);
				} // if
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.HN) {
				byte[] hex_data = new String(data).getBytes();
				String hex_string = this.byte2hex(hex_data);
				if (field.length < hex_string.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + hex_data.length);
				} else if (field.length > hex_string.getBytes(ASCII).length) {
					hex_string = this.padding(hex_string, field.length, field.padding, field.padding_char);
				} // if
				byte[] tmp_data = hex_string.getBytes(ASCII);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.NB) {
				String field_data = new String(data);
				if (field.length < field_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + field_data.getBytes(ASCII).length);
				} else if (field.length > field_data.getBytes(ASCII).length) {
					field_data = this.padding(field_data, field.length, field.padding, field.padding_char);
				} // if
				byte[] tmp_data = field_data.getBytes(ASCII);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.ST) {
				String field_data = new String(data);
				if (field.length < field_data.getBytes(this.encoding).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + field_data.getBytes(this.encoding).length);
				} // if
				byte[] tmp_data = field_data.getBytes(this.encoding);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} // if
			
		} catch (Exception e) {
			log.error("Spec.setChars()", e);
		} // try
	} // setChars()

	public void setChars(String name, char[] data) {
		int idx = this.getIndexByName(name);
		this.setChars(idx, data);
	} // setChars()

	public void setString(int idx, String data) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				// Alpha Numeric
				if (field.length < data.getBytes(ASCII).length){
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + data.getBytes(ASCII).length);
				} else if (field.length > data.getBytes(this.encoding).length) {
					data = this.padding(data, field.length, field.padding, field.padding_char);
				} // if
				byte[] byte_data = data.getBytes(ASCII);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.BN) {
				// Binary 
				if (field.length < data.getBytes().length){
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + data.getBytes().length);
				} // if
				byte[] tmp_data = data.getBytes();
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.HN) {
				// Hex Number
				String hex_data = this.byte2hex(data.getBytes());
				if (field.length < hex_data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + hex_data.getBytes(ASCII).length);
				} else if (field.length > hex_data.getBytes(ASCII).length) {
					// Add Padding
					hex_data = this.padding(hex_data, field.length, field.padding, PADDING_ZERO);
				} // if
				byte[] tmp_data = hex_data.getBytes(ASCII);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, tmp_data.length - field.length, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.NB) {
				if (field.length < data.getBytes(ASCII).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + data.getBytes(ASCII).length);
				} else if (field.length > data.getBytes(ASCII).length){
					// Add Padding
					data = this.padding(data, field.length, field.padding, field.padding_char);
				} // if
				byte[] tmp_data = data.getBytes(ASCII);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, tmp_data.length - field.length, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} else if (field.type == TYPE.ST) {
				if (field.length < data.getBytes(this.encoding).length) {
					throw new Exception("Invalid data length at Spec(" + this.code + ") at Index(" + idx + ") : " + field.length + " != " + data.getBytes(this.encoding).length);
				} // if
				byte[] tmp_data = data.getBytes(this.encoding);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(tmp_data, 0, byte_data, 0, tmp_data.length);
				System.arraycopy(byte_data, 0, this.spec_data, field.offset, field.length);
			} // if
		} catch (Exception e) {
			log.error("Spec.setString()", e);
		} // try
	} // setString

	public void setString(String name, String data) {
		int idx = this.getIndexByName(name);
		this.setString(idx, data);
	} // setString()

	public byte[] getBytes(int idx) {
		try {
			Field field = this.fields[idx];
			byte[] byte_data = new byte[field.length];
			System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
			return byte_data;
		} catch (Exception e) {
			log.error("Spec.getBytes()", e);
		} // try
		
		return null;
	} // getBytes()

	public byte[] getBytes(String name) {
		int idx = this.getIndexByName(name);
		return this.getBytes(idx);
	} // getBytes()

	public int getInt(int idx) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				// Alpha Numeric
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					int result = Integer.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Integer : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
				
			} else if (field.type == TYPE.BN) {
				// Binary
				if (field.length != 4) {
					throw new Exception("Integer binary length should be 4");
				} // if
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
				buffer = ByteBuffer.wrap(byte_data);
				int result = buffer.getInt();
				return result;
				
			} else if (field.type == TYPE.HN) {
				// Hex Number
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String hex_string = new String(byte_data, ASCII);
				if (hex_string.length() > 8) {
					hex_string = hex_string.substring(hex_string.length() - 8, hex_string.length());
				}
				byte[] hex_bytes = this.hex2byte(hex_string);
				if (hex_bytes.length != 4) {
					throw new Exception("Hex data cannot convert integer. invalid length(" + hex_bytes.length + ")");
				} // if
				ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
				buffer = ByteBuffer.wrap(hex_bytes);
				int result = buffer.getInt();
				return result;
				
			} else if (field.type == TYPE.NB) {
				// Numeric
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					int result = Integer.valueOf(str_data);
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Integer : Spec(" + this.code + ") At Index(" + idx + ")");
				} // if
				
			} else if (field.type == TYPE.ST) {
				// String
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, this.encoding);
				try {
					int result = Integer.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Integer : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
			} // if
		} catch (Exception e) {
			log.error("Spec.getInt()", e);
		} // try
		
		return -1;
	} // getInt()

	public int getInt(String name) {
		int idx = this.getIndexByName(name);
		return this.getInt(idx);
	} // getInt()

	public long getLong(int idx) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				// Alpha Numeric
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					long result = Long.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Long : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
				
			} else if (field.type == TYPE.BN) {
				// Binary
				if (field.length != 8) {
					throw new Exception("Long binary length should be 8");
				} // if
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / 8);
				buffer = ByteBuffer.wrap(byte_data);
				long result = buffer.getLong();
				return result;
				
			} else if (field.type == TYPE.HN) {
				// Hex Number
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String hex_string = new String(byte_data, ASCII);
				if (hex_string.length() > 16);
					hex_string = hex_string.substring(hex_string.length() - 16, hex_string.length());
				byte[] hex_bytes = this.hex2byte(hex_string);
				if (hex_bytes.length != 8) {
					throw new Exception("Hex data cannot convert Long. invalid length(" + hex_bytes.length + ")");
				} // if
				ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / 8);
				buffer = ByteBuffer.wrap(hex_bytes);
				long result = buffer.getLong();
				return result;
				
			} else if (field.type == TYPE.NB) {
				// Number
				//log.debug("field Length : " + field.length);
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String temp_hex = ARUtil.byte2hex(byte_data);
				//log.debug("TEMP HEX [" + temp_hex + "]");
				String str_data = new String(byte_data, ASCII);
				try {
					//log.debug("Field Data [" + str_data + "]");
					long result = Long.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Long : Spec(" + this.code + ") At Index(" + idx + ")", e);
				} // if
				
			} else if (field.type == TYPE.ST) {
				// String
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, this.encoding);
				try {
					long result = Long.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Long : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
			} // try
		} catch (Exception e) {
			log.error("Spec.getLong()", e);
		} // try
		return -1;
	} // getLong()

	public long getLong(String name) {
		int idx = this.getIndexByName(name);
		return this.getLong(idx);
	} // getLong()

	public short getShort(int idx) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					short result = Short.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Short : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
			} else if (field.type == TYPE.BN) {
				if (field.length != 2) {
					throw new Exception("Short binary length should be 2");
				} // if
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				ByteBuffer buffer = ByteBuffer.allocate(Short.SIZE / 8);
				buffer = ByteBuffer.wrap(byte_data);
				short result = buffer.getShort();
				return result;
			} else if (field.type == TYPE.HN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String hex_string = new String(byte_data, ASCII);
				if (hex_string.length() > 4) {
					hex_string = hex_string.substring(hex_string.length() - 4, hex_string.length());
				}
				byte[] hex_bytes = this.hex2byte(hex_string);
				if (hex_bytes.length != 2) {
					throw new Exception("Hex data cannot convert to Short. invalid length(" + hex_bytes.length + ")");
				} // if
				ByteBuffer buffer = ByteBuffer.allocate(Short.SIZE / 8);
				buffer = ByteBuffer.wrap(hex_bytes);
				short result = buffer.getShort();
				return result;
			} else if (field.type == TYPE.NB) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					short result = Short.valueOf(str_data);
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Short : Spec(" + this.code + ") At Index(" + idx + ")");
				} // if
			} else if (field.type == TYPE.ST) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, this.encoding);
				try {
					short result = Short.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Short : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
			} // try
		} catch (Exception e) {
			log.error("Spec.getShort()", e);
		} // try
		return -1;
	} // getShort()

	public short getShort(String name) {
		int idx = this.getIndexByName(name);
		return this.getShort(idx);
	} // getShort()

	public float getFloat(int idx) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					float result = Float.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Float : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
			} else if (field.type == TYPE.BN) {
				if (field.length != 4) {
					throw new Exception("Float binary length should be 4");
				} // if
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				ByteBuffer buffer = ByteBuffer.allocate(Float.SIZE / 8);
				buffer = ByteBuffer.wrap(byte_data);
				float result = buffer.getFloat();
				return result;
			} else if (field.type == TYPE.HN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String hex_string = new String(byte_data, ASCII);
				if (hex_string.length() > 8) {
					hex_string = hex_string.substring(hex_string.length() - 8, hex_string.length());
				}
				byte[] hex_bytes = this.hex2byte(hex_string);
				if (hex_bytes.length != 4) {
					throw new Exception("Hex data cannot convert to Float. invalid length(" + hex_bytes.length + ")");
				} // if
				ByteBuffer buffer = ByteBuffer.allocate(Float.SIZE / 8);
				buffer = ByteBuffer.wrap(hex_bytes);
				float result = buffer.getFloat();
				return result;
			} else if (field.type == TYPE.NB) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					float result = Float.valueOf(str_data);
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Float : Spec(" + this.code + ") At Index(" + idx + ")");
				} // if
			} else if (field.type == TYPE.ST) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, this.encoding);
				try {
					float result = Float.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Float : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
			} // try
		} catch (Exception e) {
			log.error("Spec.getFloat()", e);
		} // try
		return -1f;
	} // getFloat()

	public float getFloat(String name) {
		int idx = this.getIndexByName(name);
		return this.getFloat(idx);
	} // getFloat()

	public double getDouble(int idx) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					double result = Double.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Double : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
			} else if (field.type == TYPE.BN) {
				if (field.length != 8) {
					throw new Exception("Double binary length should be 8");
				} // if
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				ByteBuffer buffer = ByteBuffer.allocate(Double.SIZE / 8);
				buffer = ByteBuffer.wrap(byte_data);
				double result = buffer.getDouble();
				return result;
			} else if (field.type == TYPE.HN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String hex_string = new String(byte_data, ASCII);
				if (hex_string.length() > (Double.SIZE / 8) * 2) {
					hex_string = hex_string.substring(hex_string.length() - (Double.SIZE / 8 * 2), hex_string.length());
				}
				byte[] hex_bytes = this.hex2byte(hex_string);
				if (hex_bytes.length != 8) {
					throw new Exception("Hex data cannot convert to Double. invalid length(" + hex_bytes.length + ")");
				} // if
				ByteBuffer buffer = ByteBuffer.allocate(Double.SIZE / 8);
				buffer = ByteBuffer.wrap(hex_bytes);
				double result = buffer.getDouble();
				return result;
			} else if (field.type == TYPE.NB) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, ASCII);
				try {
					double result = Double.valueOf(str_data.trim());
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Float : Spec(" + this.code + ") At Index(" + idx + ")");
				} // if
			} else if (field.type == TYPE.ST) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String str_data = new String(byte_data, this.encoding);
				try {
					Double result = Double.valueOf(str_data);
					return result;
				} catch (NumberFormatException e) {
					throw new Exception("Field data cannot convert to Float : Spec(" + this.code + ") At Index(" + idx + ")");
				} // try
			} // try
		} catch (Exception e) {
			log.error("Spec.getDouble()", e);
		} // try
		return -1d;
	} // getDouble()

	public double getDouble(String name) {
		int idx = this.getIndexByName(name);
		return this.getDouble(idx);
	} // getDouble()

	public char[] getChars(int idx) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String string_data = new String(byte_data, this.encoding);
				char[] char_data = new char[string_data.length()];
				string_data.getChars(0, string_data.length(), char_data, 0);
				return char_data;
			} else if (field.type == TYPE.BN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String string_data = new String(byte_data, this.encoding);
				char[] char_data = new char[string_data.length()];
				string_data.getChars(0, string_data.length(), char_data, 0);
				return char_data;
			} else if (field.type == TYPE.HN) {
				byte[] hex_bytes = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, hex_bytes, 0, field.length);
				String hex_string = new String(hex_bytes, this.encoding);
				byte[] byte_data = this.hex2byte(hex_string);
				String string_data = new String(byte_data, this.encoding);
				char[] char_data = new char[string_data.length()];
				string_data.getChars(0, string_data.length(), char_data, 0);
				return char_data;
			} else if (field.type == TYPE.NB) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String string_data = new String(byte_data, this.encoding);
				char[] char_data = new char[string_data.length()];
				string_data.getChars(0, string_data.length(), char_data, 0);
				return char_data;
			} else if (field.type == TYPE.ST) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String string_data = new String(byte_data, this.encoding);
				char[] char_data = new char[string_data.length()];
				string_data.getChars(0, string_data.length(), char_data, 0);
				return char_data;
			} // if
		} catch (Exception e) {
			log.error("Spec.getChars()", e);
		} // try
		return null;
	} // getChars()

	public char[] getChars(String name) {
		int idx = this.getIndexByName(name);
		return this.getChars(idx);
	} // getChars()

	public String getString(int idx) {
		try {
			Field field = this.fields[idx];
			if (field.type == TYPE.AN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String string_data = new String(byte_data, this.encoding);
				return string_data;
			} else if (field.type == TYPE.BN) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String string_data = new String(byte_data, this.encoding);
				return string_data;
			} else if (field.type == TYPE.HN) {
				byte[] hex_bytes = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, hex_bytes, 0, field.length);
				String hex_string = new String(hex_bytes, this.encoding);
				byte[] byte_data = this.hex2byte(hex_string);
				String string_data = new String(byte_data, this.encoding);
				return string_data;
			} else if (field.type == TYPE.NB) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String string_data = new String(byte_data, this.encoding);
				return string_data;
			} else if (field.type == TYPE.ST) {
				byte[] byte_data = new byte[field.length];
				System.arraycopy(this.spec_data, field.offset, byte_data, 0, field.length);
				String string_data = new String(byte_data, this.encoding);
				return string_data;
			} // if
		} catch (Exception e) {
			log.error("Spec.getString()", e);
		}
		return null;
	} // getString()

	public String getString(String name) {
		int idx = this.getIndexByName(name);
		return this.getString(idx);
	} // getString()

	/**
	 * 필드명을 받아서 필드 번호를 반환해준다.
	 * 
	 * @param field_name
	 * @return field_index
	 */
	public int getIndexByName(String name) {
		for (int i = 0; i < this.fields.length; i++) {
			Field field = this.fields[i];
			if (name.equals(field.name))
				return i;
		} // for
		log.error("Field not found : " + name);
		return -1;
	} // getIndexByName()

    public byte[] int2bin(int value) {
		int SIZE = 4;
		byte[] b = new byte[SIZE];
		for (int i = 0; i < SIZE; i++) {
			b[i] = (byte) (value >>> (8 * (SIZE - 1 - i)));
		}
		return b;
	} // int2bin()
    
    public byte[] long2bin(long value) {
		int SIZE = 8;
		byte[] b = new byte[SIZE];
		for (int i = 0; i < SIZE; i++) {
			b[i] = (byte) (value >>> (8 * (SIZE - 1 - i)));
		}
		return b;
	} // long2bin()
    
    public byte[] short2bin(short value) {
		int SIZE = 2;
		byte[] b = new byte[SIZE];
		for (int i = 0; i < SIZE; i++) {
			b[i] = (byte) (value >>> (8 * (SIZE - 1 - i)));
		}
		return b;
	} // short2bin
    
    public byte[] float2bin(float value) {
		return this.int2bin(Float.floatToRawIntBits(value));
	} // float2bin
    
    public byte[] double2bin(double value) {
		return this.long2bin(Double.doubleToRawLongBits(value));
	} // double2bin
    
    public String byte2hex(byte[] value) {
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
	}
    
    public byte[] hex2byte(String hex) {
    	if (hex == null || hex.length() <= 0)
    		return null;
    	
    	byte[] bytes = new byte[hex.length() / 2];
    	for (int i = 0; i < bytes.length; i++) {
    		bytes[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
    	} // for
    	
    	return bytes;
    } // hex2byte()
    
    public String padding(String src, int length, PADDING padding, char padding_char) {
    	String result = src;
    	try {
			byte[] src_bytes = src.getBytes(this.encoding);
			if (src_bytes.length == length) {
	    		result = src;
	    	} else if (src_bytes.length > length) {
	    		if (padding == PADDING.LEFT) {
	    			result = src.substring(0, length);
	    		} else if (padding == PADDING.RIGHT) {
	    			result = src.substring(src_bytes.length - length, src_bytes.length);
	    		} // if
	    	} else if (src_bytes.length < length) {
	    		char[] padding_chars = new char[length - src_bytes.length];
				for (int i = 0; i < padding_chars.length; i++) {
					padding_chars[i] = padding_char;
				} // for
	    		if (padding == PADDING.LEFT) {
	    			StringBuffer buffer = new StringBuffer(src);
	    			buffer.append(padding_chars);
	    			result = buffer.toString();
	    		} else if (padding == PADDING.RIGHT) {
	    			StringBuffer buffer = new StringBuffer();
	    			buffer.append(padding_chars);
	    			buffer.append(src);
	    			result = buffer.toString();
	    		} // if
	    	} // if
		} catch (UnsupportedEncodingException e) {
			log.error("SpecImpl.padding()", e);
		} // try
    	
    	return result;
    } // padding
    
    
    public String toString(){
    	 StringBuffer buffer = new StringBuffer();
    	 
    	 buffer.append("========================\n");
    	 buffer.append("[");
    	 buffer.append(this.code);
    	 buffer.append("]\n");
    	 buffer.append("[");
    	 buffer.append(this.length);
    	 buffer.append("]\n");    	 
    	 
    	 int offset = 0;
    	 for (Field field : this.fields) {
    		 byte[] data = new byte[field.length];
    		 System.arraycopy(this.spec_data, offset, data, 0, field.length);
    		 buffer.append("[");
    		 String value = this.getString(field.name);
    		 buffer.append(value);
    		 buffer.append("]\n");
    	 } // for
    	 
    	 buffer.append("========================");
    	 return buffer.toString();
    } // toString
    
    /*************************
     * 단위테스트용 메소드
     * @param args
     *************************/
    public static void main(String[] args) {
		Spec spec = SpecFactory.getSpec("T001");
		testInteger(spec, 573);
		testLong(spec, 555);
		testShort(spec, (short)100);
		testFloat(spec, 15.0f);
		testDouble(spec, 542.0);
		testBytes(spec, "hello".getBytes());
		testChars(spec, "57300".toCharArray());
		testString(spec, "573");
	} // main()
    
    public static void testInteger(Spec spec, int int_req) {
    	
		spec.setInt("integer_an", int_req);
		spec.setInt("integer_bn", int_req);
		spec.setInt("integer_nb", int_req);
		spec.setInt("integer_hn", int_req);
		spec.setInt("integer_st", int_req);
		
		int result_an = spec.getInt("integer_an");
		int result_bn = spec.getInt("integer_bn");
		int result_nb = spec.getInt("integer_nb");
		int result_hn = spec.getInt("integer_hn");
		int result_st = spec.getInt("integer_st");
		
		if (result_an == int_req) log.debug("Integer AN Success");
		else log.error("#### Integer AN Failed [" + result_an + "]");
		
		if (result_bn == int_req) log.debug("Integer BN Success");
		else log.error("#### Integer BN Failed [" + result_bn + "]");
		
		if (result_nb == int_req) log.debug("Integer NB Success");
		else log.error("#### Integer NB Failed [" + result_nb + "]");
		
		if (result_hn == int_req) log.debug("Integer HN Success");
		else log.error("#### Integer HN Failed [" + result_hn + "]");
		
		if (result_st == int_req) log.debug("Integer ST Success");
		else log.error("#### Integer ST Failed [" + result_st + "]");
    } // testInteger
    
    public static void testLong(Spec spec, long value) {
    	log.debug("========== Long Test [" + spec.getCode() + "][" + spec.getLength() + "] ==========");
    	
		spec.setLong("long_an", value);
		spec.setLong("long_bn", value);
		spec.setLong("long_nb", value);
		spec.setLong("long_hn", value);
		spec.setLong("long_st", value);
		
		long result_an = spec.getLong("long_an");
		long result_bn = spec.getLong("long_bn");
		long result_nb = spec.getLong("long_nb");
		long result_hn = spec.getLong("long_hn");
		long result_st = spec.getLong("long_st");
		
		if (result_an == value) log.debug("Long AN Success");
		else log.error("#### Long AN Failed [" + result_an + "]");
		
		if (result_bn == value) log.debug("Long BN Success");
		else log.error("#### Long BN Failed [" + result_bn + "]");
		
		if (result_nb == value) log.debug("Long NB Success");
		else log.error("#### Long NB Failed [" + result_nb + "]");
		
		if (result_hn == value) log.debug("Long HN Success");
		else log.error("#### Long HN Failed [" + result_hn + "]");
		
		if (result_st == value) log.debug("Long ST Success");
		else log.error("#### Long ST Failed [" + result_st + "]");
    } // testLong()
    
    public static void testShort(Spec spec, short value) {
    	log.debug("========== Short Test [" + spec.getCode() + "][" + spec.getLength() + "] ==========");
    	spec.setShort("short_an", value);
    	spec.setShort("short_bn", value);
    	spec.setShort("short_nb", value);
    	spec.setShort("short_hn", value);
    	spec.setShort("short_st", value);
    	
    	short short_an = spec.getShort("short_an");
    	short short_bn = spec.getShort("short_bn");
    	short short_nb = spec.getShort("short_nb");
    	short short_hn = spec.getShort("short_hn");
    	short short_st = spec.getShort("short_st");
    	
    	if (short_an == value) log.debug("Short AN Success");
    	else log.error("#### Short AN Failed [" + short_an + "]");
    	
    	if (short_bn == value) log.debug("Short BN Success");
    	else log.error("#### Short BN Failed [" + short_bn + "]");
    	
    	if (short_nb == value) log.debug("Short NB Success");
    	else log.error("#### Short NB Failed [" + short_nb + "]");
    	
    	if (short_hn == value) log.debug("Short HN Success");
    	else log.error("#### Short HN Failed [" + short_hn + "]");
    	
    	if (short_st == value) log.debug("Short ST Success");
    	else log.error("#### Short ST Failed [" + short_st + "]");
    	
    } // testShort()
    
    public static void testFloat(Spec spec, float value) {
    	log.debug("========== Float Test [" + spec.getCode() + "][" + spec.getLength() + "] ==========");
    	spec.setFloat("float_an", value);
    	spec.setFloat("float_bn", value);
    	spec.setFloat("float_nb", value);
    	spec.setFloat("float_hn", value);
    	spec.setFloat("float_st", value);
    	
    	float float_an = spec.getFloat("float_an");
    	float float_bn = spec.getFloat("float_bn");
    	float float_nb = spec.getFloat("float_nb");
    	float float_hn = spec.getFloat("float_hn");
    	float float_st = spec.getFloat("float_st");
    	
    	if (float_an == value) log.debug("Float AN Success");
    	else log.error("#### Float AN Failed [" + float_an + "]");
    	
    	if (float_bn == value) log.debug("Float BN Success");
    	else log.error("#### Float BN Failed [" + float_bn + "]");
    	
    	if (float_nb == value) log.debug("Float NB Success");
    	else log.error("#### Float NB Failed [" + float_nb + "]");
    	
    	if (float_hn == value) log.debug("Float HN Success");
    	else log.error("#### Float HN Failed ["+ float_hn + "]");
    	
    	if (float_st == value) log.debug("Float ST Success");
    	else log.error("#### Float ST Failed [" + float_st + "]");
    } // testFloat()
    
    public static void testDouble(Spec spec, double value) {
    	log.debug("========== Double Test [" + spec.getCode() + "][" + spec.getLength() + "] ==========");
    	spec.setDouble("double_an", value);
    	spec.setDouble("double_bn", value);
    	spec.setDouble("double_nb", value);
    	spec.setDouble("double_hn", value);
    	spec.setDouble("double_st", value);
    	
    	double double_an = spec.getDouble("double_an");
    	double double_bn = spec.getDouble("double_bn");
    	double double_nb = spec.getDouble("double_nb");
    	double double_hn = spec.getDouble("double_hn");
    	double double_st = spec.getDouble("double_st");
    	
    	if (double_an == value) log.debug("Double AN Success");
    	else log.error("#### Double AN Failed [" + double_an + "]");
    	
    	if (double_bn == value) log.debug("Double BN Success");
    	else log.error("#### Double BN Failed [" + double_bn + "]");
    	
    	if (double_nb == value) log.debug("Double NB Success");
    	else log.error("#### Double NB Failed [" + double_nb + "]");
    	
    	if (double_hn == value) log.debug("Double HN Success");
    	else log.error("#### Double HN Failed [" + double_hn + "]");
    	
    	if (double_st == value) log.debug("Double ST Success");
    	else log.error("#### Double ST Failed [" + double_st + "]");
    } // testDouble()
    
    public static void testBytes(Spec spec, byte[] value) {
    	log.debug("========== Bytes Test [" + spec.getCode() + "][" + spec.getLength() + "] ==========");
    	spec.setBytes("bytes_an", value);
    	spec.setBytes("bytes_bn", value);
    	spec.setBytes("bytes_nb", value);
    	spec.setBytes("bytes_hn", value);
    	spec.setBytes("bytes_st", value);
    	
    	byte[] bytes_an = spec.getBytes("bytes_an");
    	byte[] bytes_bn = spec.getBytes("bytes_bn");
    	byte[] bytes_nb = spec.getBytes("bytes_nb");
    	byte[] bytes_hn = spec.getBytes("bytes_hn");
    	byte[] bytes_st = spec.getBytes("bytes_st");
    	
    	byte[] value_data = new byte[spec.getLength("bytes_an")];
    	System.arraycopy(value, 0, value_data, 0, value.length);
    	
    	if (Arrays.equals(bytes_an, value_data)) log.debug("Bytes AN Success");
    	else log.error("Bytes AN Failed [" + ARUtil.byte2hex(bytes_an) + "]");
    	
    	if (Arrays.equals(bytes_bn, value_data)) log.debug("Bytes BN Success");
    	else log.error("Bytes BN Failed [" + ARUtil.byte2hex(bytes_bn) + "]");
    	
    	if (Arrays.equals(bytes_nb, value_data)) log.debug("Bytes NB Success");
    	else log.error("Bytes NB Failed [" + ARUtil.byte2hex(bytes_nb) + "]");
    	
    	if (Arrays.equals(bytes_hn, value_data)) log.debug("Bytes HN Success");
    	else log.error("Bytes HN Failed [" + ARUtil.byte2hex(bytes_hn) + "]");
    	
    	if (Arrays.equals(bytes_st, value_data)) log.debug("Bytes ST Success");
    	else log.error("Bytes ST Failed [" + ARUtil.byte2hex(bytes_st) + "]");
    } // testBytes()
    
   public static void testChars(Spec spec, char[] value) {
	   log.debug("========== Chars Test [" + spec.getCode() + "][" + spec.getLength() + "] ==========");
	   log.debug("=== Test Value [" + new String(value) + "]");
	   spec.setChars("chars_an", value);
	   spec.setChars("chars_bn", value);
	   spec.setChars("chars_nb", value);
	   spec.setChars("chars_hn", value);
	   spec.setChars("chars_st", value);
	   
	   char[] chars_an = spec.getChars("chars_an");
	   char[] chars_bn = spec.getChars("chars_bn");
	   char[] chars_nb = spec.getChars("chars_nb");
	   char[] chars_hn = spec.getChars("chars_hn");
	   char[] chars_st = spec.getChars("chars_st");
	   
	   if (Arrays.equals(chars_an, value)) log.debug("Chars AN Success");
	   else log.error("#### Chars AN Failed [" + new String(chars_an) + "]");
	   
	   if (Arrays.equals(chars_bn, value)) log.debug("Chars BN Success");
	   else log.error("#### Chars BN Failed [" + new String(chars_bn) + "]");
	   
	   if (Arrays.equals(chars_nb, value)) log.debug("Chars NB Success");
	   else log.error("#### Chars NB Failed [" + new String(chars_nb) + "]");
	   
	   if (Arrays.equals(chars_hn, value)) log.debug("Chars HN Success");
	   else log.error("#### Chars HN Failed [" + new String(chars_hn) + "]");
	   
	   if (Arrays.equals(chars_st, value)) log.debug("Chars ST Success");
	   else log.error("#### Chars ST Failed [" + new String(chars_st) + "]");
   } // testChars()
    
    public static void testString(Spec spec, String value) {
    	log.debug("========== String Test [" + spec.getCode() + "][" + spec.getLength() + "] ==========");
    	spec.setString("string_an", value);
    	spec.setString("string_bn", value);
    	spec.setString("string_nb", value);
    	spec.setString("string_hn", value);
    	spec.setString("string_st", value);
    	
    	
    	String result_an = spec.getString("string_an");
    	String result_bn = spec.getString("string_bn");
    	String result_nb = spec.getString("string_nb");
    	String result_hn = spec.getString("string_hn");
    	String result_st = spec.getString("string_st");
    	
    	
    	if (result_an.trim().equals(value)) log.debug("String AN Success");
    	else log.error("String AN Failed [" + result_an + "]");
    	
    	if (result_bn.trim().equals(value)) log.debug("String BN Success");
    	else log.error("String BN Failed [" + result_bn + "]");
    	
    	if (result_nb.trim().equals(value)) log.debug("String NB Success");
    	else log.error("String NB Failed [" + result_nb + "]");
    	
    	if (result_hn.trim().equals(value)) log.debug("String HN Success");
    	else log.error("String HN Failed [" + result_hn + "]");
    	
    	if (result_st.trim().equals(value)) log.debug("String ST Success");
    	else log.error("String ST Failed [" + result_st + "]");
    	/*
    	*/
    } // testString()
} // class SpecImpl
