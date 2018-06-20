package com.ar.http;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ar.common.config.ARConfig;
import com.ar.common.logging.ARLog;

/*********************************
 * <pre>
 * HTTP 요청을 정의한 파일을 파싱한다.
 * </pre>
 * @author Bak Sang Heon
 *
 *********************************/
public class HttpRouteParser {
	private static ARLog log = ARLog.getLogger(HttpRouteParser.class);
	
	/*******************
	 * <pre>
	 * 설정 파일을 로드한다.
	 * </pre>
	 * @throws Exception
	 *******************/
	public List<Route> load() throws Exception {
		log.info("Load http route configuration");
		InputStream in = null;
		List<Route> routeList = new ArrayList<Route>();
		try {
			String file = ARConfig.getInstance().getValue(HttpConst.HTTP_CONFIG);
			
			Document document = null;
			in = HttpRouteParser.class.getClassLoader().getResourceAsStream(file);
			if (in == null) {
				SAXBuilder builder = new SAXBuilder(true);
				document = builder.build(new File(file));
			} else {
				SAXBuilder builder = new SAXBuilder(true);
				document = builder.build(in);
			} // if
			
			Element root = document.getRootElement();
			List<Element> mappingList = root.getChildren("mapping");
			
			for (Element mapping : mappingList) {
				Route route = new Route();
				
				// Set uri
				String uri = mapping.getAttributeValue("uri");
				route.setUri(uri);
				
				// Set method
				String method = mapping.getAttributeValue("method");
				if (method.equals("GET")) route.setMethod(Route.Method.GET);
				else if (method.equals("POST")) route.setMethod(Route.Method.POST);
				else if (method.equals("PUT")) route.setMethod(Route.Method.PUT);
				else if (method.equals("DELETE")) route.setMethod(Route.Method.DELETE);
				else if (method.equals("JSON")) route.setMethod(Route.Method.JSON);
				
				Element service = mapping.getChild("service");
				
				// Set clazz
				String clazz = service.getAttributeValue("class");
				route.setService(clazz);
				
				// Set service class
				String callMethod = service.getAttributeValue("method");
				route.setCallMethod(callMethod);
				
				Element parameters = mapping.getChild("parameters");
				List<Element> params = parameters.getChildren("param");
				for (Element param : params) {
					String name = param.getAttributeValue("name");
					String type = param.getAttributeValue("type");
					String required = param.getAttributeValue("required");
					
					// Set type
					Class<?> typeClass = null;
					if (type.equals("String")) typeClass = String.class;
					else if (type.equals("Integer")) typeClass = Integer.class;
					else if (type.equals("Double")) typeClass = Double.class;
					else if (type.equals("Boolean")) typeClass = Boolean.class;
					route.addParamType(name, typeClass);
					
					// Set required
					if (required.equals("true")) route.addRequired(name);
				} // for
				
				routeList.add(route);
			} // for
			
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (in != null) try { in.close(); } catch (Exception e) { log.error(e); }
		} // try
		
		return routeList;
	} // load()

} // class HttpRouteParser
