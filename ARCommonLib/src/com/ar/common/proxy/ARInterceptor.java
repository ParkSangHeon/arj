package com.ar.common.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/*************************************************
 * <pre>
 * Proxy에서 이벤트를 가로챌 인터셉터를 구현하기 위한 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *************************************************/
public abstract class ARInterceptor implements MethodInterceptor {

	private String[] _methods;
	
	public ARInterceptor(String[] methods) {
		this._methods = methods;
	} // Constructor
	
	// 타겟 메소드 호출 전 호출퇴는 메소드.
	protected abstract void before();
	protected void before(Object obj){}
	
	// 타겟 메소드 호출 후 호출되는 메소드.
	protected abstract void after();
	protected void after(Object obj){}
	
	/***********************************************
	 * <pre>
	 * 메소드 호출을 가로채는 인터셉터.
	 * 미리 선언한 특정 메소드를 호출할 경우,
	 * 이를 가로채 before(), after() 메소드를 호출한다.
	 * </pre>
	 ***********************************************/
	@Override
	public Object intercept(Object obj1, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
		Object result = null;
		
		for(String methodName : this._methods){
			if (methodName.equals(method.getName())){
				this.before();
				this.before(obj1);
				try {
					result = methodProxy.invokeSuper(obj1, params);
				} finally {
					this.after();
					this.after(obj1);
				} // try
				return result;
			} // if
		} // for
		
		return methodProxy.invokeSuper(obj1, params);
	} // intercept()

} // ARInterceptor
