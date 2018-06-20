package com.ar.http.service;

import java.util.Map;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import com.ar.common.logging.ARLog;

/******************************************************
 * <pre>
 * HTTP 요청을 처리하는 서비스 클래스를 구현하기 위한 상위 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ******************************************************/
public class HttpService {
	private static ARLog log = ARLog.getLogger(HttpService.class);
	
	protected HttpRequest _request;
	protected HttpResponse _response;
	protected Map<String, String> _params;
	
	public HttpService(HttpRequest request, HttpResponse response) {
		this._request = request;
		this._response = response;
	} // Constructor
	
	/**
	 * <pre>
	 * HTTP 요청 파라미터를 세팅한다.
	 * </pre>
	 * @param params
	 */
	public void setParams(Map<String, String> params) {
		this._params = params;
	} // setParams()
	
} // class HttpService
