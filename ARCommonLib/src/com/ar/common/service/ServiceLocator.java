package com.ar.common.service;

import java.lang.reflect.Method;

import java.util.ArrayList;

import com.ar.common.logging.ARLog;
import com.ar.common.proxy.ARInterceptor;
import com.ar.common.proxy.ARProxy;
import com.ar.common.service.annotation.Transaction;

public class ServiceLocator {
	private static ARLog log = ARLog.getLogger(ServiceLocator.class);
	/**
	 * 2010.7.21 Bak SangHeon
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends CommonService> T getService(Class<? extends CommonService> clazz){
		// 트랜잭션 처리를 위한 서비스 인터셉터를 이용해 서비스 클래스의 프록시를 생성하고 반환한다.
		String[] methods = getTxMethod(clazz);
		TxServiceInterceptor interceptor = new TxServiceInterceptor(methods);
		T service = (T)ARProxy.createProxy(clazz, interceptor);
		
		return service;
	} // getService()
	
	
	/**
	 * 2010.7.21 Bak SangHeon
	 * @param clazz
	 * @return
	 */
	private static String[] getTxMethod(Class<?> clazz){
		ArrayList<String> list = new ArrayList<String>();
		Method[] methods = clazz.getMethods();
		
		for(Method method : methods){
			Transaction tx = method.getAnnotation(Transaction.class);
			if(tx != null){
				Transaction.scope scope = tx.value();
				if (scope == Transaction.scope.METHOD){
					list.add(method.getName());
				} // if
			} // if
		} // for
		
		String[] result = new String[list.size()];
		result = list.toArray(result);
		return result;
	} // getTxMethods

	
	@SuppressWarnings("unchecked")
	public static <T extends CommonService> T getProxyService(ARInterceptor interceptor, Class<? extends CommonService> clazz){
		// 트랜잭션 처리를 위한 서비스 인터셉터를 이용해 서비스 클래스의 프록시를 생성하고 반환한다.
		T service = (T)ARProxy.createProxy(clazz, interceptor);
		return service;
	} // getProxyService()
	
	
	
} // class ServiceLocator
