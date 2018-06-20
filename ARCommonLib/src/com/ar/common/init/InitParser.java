package com.ar.common.init;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ar.common.config.ARConfig;
import com.ar.common.logging.ARLog;

/******************************
 * <pre>
 * 초기화 설정파일을 파싱하는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ******************************/
class InitParser {
	private static ARLog log = ARLog.getLogger(InitParser.class);
	
	/*********************************
	 * <pre>
	 * 초기화 설정 파일을 파싱한다.
	 * 결과로 초기화 클래스 목록을 반환한다.
	 * </pre>
	 * @return
	 *********************************/
	static List<String> parse() {
		List<String> initList = new ArrayList<String>();
		
		String initConfig = ARConfig.getInstance().getValue(InitConst.INIT_CONFIG);
		
		InputStream in = null;
		try {
			Document document = null;
			in = InitParser.class.getClassLoader().getResourceAsStream(initConfig);
			if (in == null) {
				SAXBuilder builder = new SAXBuilder(true);
				document = builder.build(new File(initConfig));
			} else {
				SAXBuilder builder = new SAXBuilder(true);
				document = builder.build(in);
			} // if
			
			if (document == null) throw new Exception ("Can not parse init config file");
			
			Element root = document.getRootElement();
			List<Element> children = root.getChildren("init");
			for (Element child : children) {
				String className = child.getAttributeValue("class");
				initList.add(className);
			} // for
			
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (in != null) try { in.close(); } catch (Exception e) { log.error(e); }
		} // try
		
		return initList;
	} // parse()
} // class InitParser
