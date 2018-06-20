package com.ar.common.proxy;

import net.sf.cglib.proxy.Enhancer;

/**************************************
 * <pre>
 * 특정 클래스의 Proxy를 생성해주는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 **************************************/
public class ARProxy {
	
	/************************************************************************
	 * <pre>
	 * 프록시를 생성할 클래스와, 이벤트를 가로챌 인터셉터를 통해 프록시 클래스를 생성한다.
	 * </pre>
	 * @param clazz
	 * @param interceptor
	 * @return
	 ************************************************************************/
	public static Object createProxy(Class<?> clazz, ARInterceptor interceptor) {
		Enhancer en = new Enhancer();
		en.setSuperclass(clazz);
		en.setCallback(interceptor);
		Object obj = en.create();
		return obj;
	} // createProxy()
	
	/************************************************************************
	 * <pre>
	 * 프록시를 생성할 클래스와, 이벤트를 가로챌 인터셉터를 통해 프록시 클래스를 생성한다.
	 * 생성자에 파라미터를 전달한다.
	 * </pre>
	 * @param clazz
	 * @param interceptor
	 * @return
	 ************************************************************************/
	public static Object createProxy(Class<?> clazz, Class[] argTypes, Object[] args, ARInterceptor interceptor) {
		Enhancer en = new Enhancer();
		en.setSuperclass(clazz);
		en.setCallback(interceptor);
		Object obj = en.create(argTypes, args);
		return obj;
	} // createProxy()
	
} // class ARProxy()
