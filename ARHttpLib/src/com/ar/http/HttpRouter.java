package com.ar.http;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;

import com.ar.common.logging.ARLog;
import com.ar.http.service.HttpService;

/*******************************************
 * <pre>
 * Http 요청을 파싱하여 매핑된 서비스를 호출해준다.
 * </pre>
 * @author Bak Sang Heon
 *
 *******************************************/
public class HttpRouter {
	private static ARLog log = ARLog.getLogger(HttpRouter.class);
	
	private static HttpRouter _router;
	
	/**********************************
	 * <pre>
	 * 생성자. HTTP 라우트 정보를 초기화한다.
	 * </pre>
	 **********************************/
	private HttpRouter() {
		// 라우트 정보 초기화.
		try {
			HttpRouteParser parser = new HttpRouteParser();
			this._routes = parser.load();
		} catch (Exception e) {
			log.error(e);
		} // try
	} // Constructor
	
	
	/****************************
	 * <pre>
	 * HttpRouter 인스턴스를 얻는다.
	 * </pre>
	 * @return
	 ****************************/
	public static HttpRouter getRouter() {
		if (_router == null) _router = new HttpRouter();
		return _router;
	} // getRouter()
	
	private List<Route> _routes = new ArrayList<Route>();
	
	
	/*******************************
	 * <pre>
	 * Http 요청에 대한 라우팅을 호출한다.
	 * </pre>
	 * @param uri
	 *******************************/
	public Object route(HttpRequest request, HttpResponse response) throws Exception, RequiredParameterNotFoundException, BadRequestException  {		
		// URI 확인하기.
		String url = request.getUri();
		String[] uris = url.split("[?]");
		for (String uri : uris) {
			log.debug("URI [" + uri + "]");
		} // for
		String uri = uris[0];
		
		// URI에 해당하는 Route를 가져온다.
		Route route = findRoute(uri);
		if (route == null)
			throw new BadRequestException();
			
		log.debug("Find Route : " + route.toString());
		
		Map<String, String> params = null;
		
		HttpMethod method = request.getMethod();
		Route.Method routeMethod = route.getMethod();
		if (routeMethod == Route.Method.JSON) {
			// TODO : Method의 형식이 JSON일 경우. Body의 JSON 데이타를 가져온다.
			
		} else {
			// JSON 형식이 아닐경우 파라미터 목록을 가져온다.
			if (method.equals(HttpMethod.POST)) {
				// POST Request				
				params = getPostParameters(request);
			} else {
				// Other Request
				params = getParameters(request);
			} // if
			
			// 필수 파라미터가 들어있는지 확인한다.
			for (String requiredKey : route.requiredKeys()) {
				if (!params.containsKey(requiredKey)) {
					// 필수값 오류 발생.
					throw new RequiredParameterNotFoundException(requiredKey);
				} // if
			} // for
		} // if
		
		try {
			// Class 인스턴스 생성
			String serviceClassName = route.getService();
			Class<?> clazz = Class.forName(serviceClassName);
			Constructor<?> constructor = clazz.getConstructor(HttpRequest.class, HttpResponse.class);
			Object obj = constructor.newInstance(request, response);
			if (obj instanceof HttpService) {
				HttpService service = (HttpService)obj;
				service.setParams(params);
			} // if
			
			//메소드 생성.
			Method callMethod = obj.getClass().getMethod(route.getCallMethod());
			
			// 메소드 호출.
			Object result = callMethod.invoke(obj);
			if (result != null) {
				// Return Value 처리.
				return result;
			} // if
		} catch (Exception e) {
			log.error(e);
			throw e;
		} // try
		
		return null;
	} // route()
	
	/****************************
	 * <pre>
	 * URI에 해당하는 라우팅을 찾는다.
	 * </pre>
	 * @param uri
	 * @return
	 ****************************/
	private Route findRoute(String uri) {
		for (Route route : _routes){
			if (route.getUri().equals(uri)) {
				return route;
			} // if
		} // for
		return null;
	} // findRoute()
	
	/*********************
	 * <pre>
	 * Route 목록을 가져온다.
	 * </pre>
	 * @return
	 *********************/
	public List<Route> getRoutes() {
		return _routes;
	} // getRoutes()

	
	/******************************
	 * <pre>
	 * Routes 에 대한 정보 문자열 반환.
	 * </pre>
	 * @return
	 ******************************/
	public String getRoutesInfo() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n=== HTTP URL Routes ===\n");
		for(Route route : _routes) {
			builder.append(route.toString());
		} // for
		return builder.toString();
	} // getRoutesInfo();
	
	
	/*******************************
	 * <pre>
	 * POST로 들어온 파라미터를 조회한다.
	 * </pre>
	 * @param request
	 * @return
	 *******************************/
	public Map<String, String> getPostParameters(HttpRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		
		/********************
		 *  is POST request
		 ********************/				
		try {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
			
			try {
				while(decoder.hasNext()) {
					InterfaceHttpData data = decoder.next();
					Attribute attr = (Attribute)data;
					String key = data.getName();
					String value = attr.getValue();
					log.debug("KEY : " + key);
					log.debug("VALUE : " + value);
					params.put(key, value);
				} // while
			} catch (EndOfDataDecoderException e) {
				log.debug("No more parameter");
			} // try
			
		} catch (Exception e) {
			log.error(e);
		} // try
		
		return params;
	} // getPostParameters()
	
	
	/********************************************
	 * POST 이외의 방식으로 들어온 파라미터를 조회한다.
	 * @param request
	 * @return
	 ********************************************/
	public Map<String, String> getParameters(HttpRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		
		// is GET Parameter
		QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
		Map<String, List<String>> paramList = decoder.getParameters();
		Iterator<String> keys = paramList.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			List<String> values = paramList.get(key);
			String value = values.get(0);
			params.put(key, value);
		} // while
		
		return params;
	} // getParameters()
	
} // class HttpRouter

