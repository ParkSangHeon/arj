package com.ar.common.collection;

import java.lang.reflect.Method;

import java.util.HashMap;

import com.ar.common.logging.ARLog;

/**
 * 2010.7.21
 * Key - Value 형식으로 Bean객체를 관리하는 클래스.
 * @author SangHeon Bak
 *
 */
public class QKeyValue {
	private ARLog log = ARLog.getLogger(QKeyValue.class);
	private Object obj;
	private HashMap<String, Method> getter = new HashMap<String, Method>();
	private HashMap<String, Method> setter = new HashMap<String, Method>();
	public QKeyValue(Object obj){
		this.obj = obj;
		analyse(obj);
	} // QKeyValue()
	
	/**
	 * 2010.8.26 Bak Sang Heon
	 * 객체의 setter/getter를 검색한다. 
	 * @param obj
	 */
	private void analyse(Object obj){
		Method[] methods = obj.getClass().getMethods();
		for(Method method : methods){
			String name = method.getName();
			if (!name.equals("getClass")){
				if (name.startsWith("get")){
					String key = String.valueOf(name.charAt(3)).toLowerCase() + name.substring(4);
					getter.put(key, method);
				} // if
			} // if
			
			if (name.startsWith("set")){
				String key = String.valueOf(name.charAt(3)).toLowerCase() + name.substring(4);
				setter.put(key, method);
			} // if
		} // for
	} // searchGetter()

	/**
	 * 2010.8.26 Bak Sang Heon
	 * setter를 검색하여 데이타를 넣는다.
	 * @param id
	 * @param value
	 */
	public void set(String id, Object value){
		Method method = setter.get(id);
		if ( method == null) return;
		try { 
			Class[] parameter = method.getParameterTypes();
			String parameterType = parameter[0].getName();
			String valueType = value.getClass().getName();			
			if(!parameterType.equals(valueType)){
				if(valueType.equals("java.lang.String")){
					if(parameterType.equals(Integer.class.getName())){
						value = Integer.parseInt((String)value);
					} else if(parameterType.equals(Long.class.getName())){
						value = Long.parseLong((String)value);
					} else if(parameterType.equals(Short.class.getName())){
						value = Short.parseShort((String)value);
					} else if(parameterType.equals(Double.class.getName())){
						value = Double.parseDouble((String)value);
					} else if(parameterType.equals(Float.class.getName())){
						value = Float.parseFloat((String)value);
					} else if(parameterType.equals(Boolean.class.getName())){
						value = Boolean.parseBoolean((String)value);
					} else if(parameterType.equals(Byte.class.getName())){
						value = Byte.valueOf((String)value);
					} // if
				} // if
			} // if			
			
			method.invoke(obj, value);
		} catch(Exception e){
			log.error(e);
		}
	} // set()
	
	public <T> T get(String id){
		// TODO
		return null;
	} // get()
} // class QKeyValue
