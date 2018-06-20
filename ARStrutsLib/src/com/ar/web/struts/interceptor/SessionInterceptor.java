package com.ar.web.struts.interceptor;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.ar.common.logging.ARLog;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**********************************
 * <pre>
 * 세션을 Action에 전달하는 인터셉터.
 * </pre>
 * @author Bak Sang Heon
 *
 **********************************/
public class SessionInterceptor implements Interceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7877098693363621255L;
	private static ARLog log = ARLog.getLogger(SessionInterceptor.class);
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		String result = null;
		Object obj = invocation.getAction();
		
		if (obj instanceof SessionAware) {
			Map<String, Object> map = null;
			log.debug("SessionAware Implementated");
			SessionAware sessionAction = (SessionAware)obj;
			map = ActionContext.getContext().getSession();
			log.debug("SESSION MAP : " + map.toString());
			if (map != null){
				sessionAction.setSession(map);
			} // if
		} // if
		
		return invocation.invoke();
	} // intercept

} // class SessionInterceptor
