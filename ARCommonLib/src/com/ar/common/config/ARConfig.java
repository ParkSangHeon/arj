package com.ar.common.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import com.ar.common.logging.ARLog;

/**************************
 * <pre>
 * 설정파일을 읽어오는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 **************************/
public class ARConfig {
	private static ARLog log = ARLog.getLogger(ARConfig.class);
	
	private HashMap<String, String> properties = new HashMap<String, String>();
	
	private static ARConfig instance;
	public static ARConfig getInstance() {
		if (instance == null) instance = new ARConfig();
		return instance;
	} // getInstance()
	
	private ARConfig() {
		this.load();
	} // Constructor

	/******************
	 * <pre>
	 * 설정파일을 읽어온다.
	 * </pre>
	 ******************/
	private void load() {
		log.info("Loading ar.properties");
		InputStream in = null;
		try {
			in = ARConfig.class.getResourceAsStream("/ar.properties");
			Properties prop = new Properties();
			prop.load(in);
			
			Iterator<?> keyIt = prop.keySet().iterator();
			while(keyIt.hasNext()) {
				Object obj = keyIt.next();
				String key = (String)obj;
				String value = (String) prop.get(key);
				properties.put(key, value);
			} // while
			
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (in != null) try { in.close(); } catch (Exception e) { log.error(e); }
		} // try
	} // load()
	
	/*************************
	 * <pre>
	 * 설정파일을 다시 읽어온다.
	 * </pre>
	 *************************/
	public void reload() {
		this.load();
	} // reload()

	/**********************************
	 * <pre>
	 * 키에 해당하는 프로퍼티 값을 가져온다.
	 * </pre>
	 **********************************/
	public String getValue(String key) {
		return this.properties.get(key);
	} // getValue()
	
	/**********************************************
	 * <pre>
	 * 키에 해당하는 값을 Integer형으로 변환하여 가져온다.
	 * </pre>
	 * @param key
	 * @return
	 **********************************************/
	public int getInteger(String key) {
		String str = this.properties.get(key);
		return Integer.parseInt(str);
	} // getInteger()
	
	/*********************************************
	 * <pre>
	 * 키에 해당하는 값을 Boolean형으로 변환하여 가져온다.
	 * </pre>
	 * @param key
	 * @return
	 *********************************************/
	public boolean getBoolean(String key) {
		String str = this.properties.get(key);
		return Boolean.parseBoolean(str);
	} // getBoolean()
	
} // class ARConfig

