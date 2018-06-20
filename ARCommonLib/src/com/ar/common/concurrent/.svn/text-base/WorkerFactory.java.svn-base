package com.ar.common.concurrent;

import com.ar.common.logging.ARLog;
import com.ar.common.proxy.ARProxy;

/**********************************
 * <pre>
 * Worker의 프록시를 생성하여 반환한다.
 * </pre>
 * @author Bak Sang Heon
 *
 **********************************/
public class WorkerFactory {
	private static ARLog log = ARLog.getLogger(WorkerFactory.class);
	
	/******************************************
	 * <pre>
	 * 해당 Worker 클래스의 프록시를 생성하여 반환한다.
	 * </pre>
	 * @param clazz
	 * @param workerName
	 * @return
	 ******************************************/
	@SuppressWarnings("unchecked")
	public static <T extends Worker> T getWorker(Class<? extends Worker> clazz, String workerName) {
		// Worker는 run() 메소드만을 프록시로 가로챈다.
		String[] methods = {"run"};
		WorkerInterceptor interceptor = new WorkerInterceptor(methods);
		Class[] argTypes = new Class[]{ String.class };
		Object[] params = new Object[]{ workerName };
		T worker = (T)ARProxy.createProxy(clazz, argTypes, params, interceptor);
		return worker;
	} // getWorker()
	
} // class WorkerFactory
