package com.ar.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ar.common.logging.ARLog;

/*****************************
 * <pre>
 * URI 라우팅 정보를 담은 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *****************************/
public class Route {
	private static ARLog log = ARLog.getLogger(Route.class);

	public enum Method {
		GET,
		POST,
		PUT,
		DELETE,
		JSON
	}
	private String _uri;
	private Method _method;
	private String _service;
	private String _callMethod;
	private Map<String, Object> _params = new HashMap<String, Object>();
	private Map<String, Class<?>> _paramsType = new HashMap<String, Class<?>>();
	private List<String> _requiredParams = new ArrayList<String>();
	
	public void setUri(String uri) { this._uri = uri; }
	public void setMethod(Method method) { this._method = method; }
	public void setService(String service) { this._service = service; }
	public void setCallMethod(String callMethod) { this._callMethod = callMethod; }
	public void addParam(String key, Object value) { this._params.put(key, value); }
	public void addParamType(String key, Class<?> clazz) { this._paramsType.put(key, clazz); }
	public void addRequired(String key) { this._requiredParams.add(key); }
	
	public String getUri() { return this._uri; }
	public Method getMethod() { return this._method; }
	public String getService() { return this._service; }
	public String getCallMethod() { return this._callMethod; }
	public Object getParam(String key) { return this._params.get(key); }
	public Class<?> getType(String key) { return this._paramsType.get(key); }
	public boolean isRequired(String key) {
		return this._requiredParams.contains(key);
	} // isRequired()
	public List<String> requiredKeys() { return this._requiredParams; }

	/************
	 * to string
	 ************/
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("URI : ").append(this._uri).append("\n");
		builder.append("METHOD : ").append(this._method.toString()).append("\n");
		builder.append("SERVICE : ").append(this._service).append("\n");
		builder.append("CALL METHOD : ").append(this._callMethod).append("\n");
		builder.append("PARAMS ").append("\n");
		Iterator<String> iter = this._paramsType.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			builder.append("   ").append(key);
			Class<?> clazz = this._paramsType.get(key);
			builder.append("[").append(clazz.getName()).append("]");
			if (this._requiredParams.contains(key)) builder.append(" REQUIRED");
			builder.append("\n");
		} // while
		
		return builder.toString();
	} // toString();
	
	@Override
	public void finalize() {
		log.debug(this.getClass().getName() + "finalize");
		try { super.finalize(); } catch (Throwable e) { log.error(e); }
	} // finalize()
} // class Route


