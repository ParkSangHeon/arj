package com.ar.web.struts.interceptor;

import java.util.Iterator;
import java.util.Map;

import com.ar.common.logging.ARLog;
import com.ar.web.struts.action.ARAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/*****************************************
 * <pre>
 * 요청 파라미터를 액션에 전달하기 위한 인터셉터.
 * </pre>
 * @author Bak Sang Heon
 *
 *****************************************/
public class ParameterInterceptor implements Interceptor {
	private static ARLog log = ARLog.getLogger(ParameterInterceptor.class);
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	} // destroy()

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	} // init()

	/****************************************
	 * <pre>
	 * 요청에서 파라미터를 꺼내 Action으로 전달한다.
	 * </pre>
	 ****************************************/
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Object obj = invocation.getAction();
		if (obj instanceof ARAction) {
			ARAction action = (ARAction)obj;
			Map<String, Object> model = action.getModel();
			if (model == null) log.error("Model is null");
			
			Map<String, Object> params = ActionContext.getContext().getParameters();
			if (params != null && params.size() > 0) {
				Iterator<String> iter = params.keySet().iterator();
				while(iter.hasNext()) {
					String key = iter.next();
					Object value = params.get(key);
					log.debug("Find Parameter Key : {}", key);
					log.debug("Find Parameter Value : {}", value);
					log.debug("Find Parameter Type : {}", value.getClass().getName());
					if (value instanceof String[]) {
						log.debug("Value is Array");
						
						// 파라미터가 배열일 경우.
						String[] paramArray = (String[])value;
						log.debug("Array Length : {}", paramArray.length);
						if (paramArray.length == 1) {
							log.debug("PARAM 0 : {}", paramArray[0]);
							log.debug("PARAM 0 Type : {}", paramArray[0].getClass().getName());
							model.put(key, paramArray[0]);
						} else {
							log.debug("PARAM IS NOT ARRAY");
							model.put(key,  paramArray);
						} // if
					} else {
						log.debug("Value is String");
						// 단일 파라미터일 경우.
						model.put(key, value);
					} // if
				} // while (iter has next)
			} // if
		} // if
		
		return invocation.invoke();
	} // intercept()

} // class ParameterInterceptor
