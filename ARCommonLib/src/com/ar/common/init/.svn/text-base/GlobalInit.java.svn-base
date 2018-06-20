package com.ar.common.init;

import java.util.List;

import com.ar.common.logging.ARLog;

/********************************************
 * <pre>
 * 초기화 클래스의 검색 및 구동처리를 진행한다.
 * </pre>
 * @author Bak Sang Heon
 *
 ********************************************/
public class GlobalInit {
	private static ARLog log = ARLog.getLogger(GlobalInit.class);	
	private GlobalInit() {
		// Don't create this class's instance
	} // Constructor

	public static void init() {
		log.info("### Global Initializer start");
		
		// Parsing XML
		List<String> initList = InitParser.parse();
		
		// Run Initializer
		for (String initClassName : initList) {
			try {
				Class<?> clazz = Class.forName(initClassName);
				Object obj = clazz.newInstance();
				if (obj instanceof Initializer) {
					((Initializer)obj).init();
				} // if
			} catch (InitException e) {
				log.error(e);
			} catch (Exception e) {
				log.error(e);
			} // try
		} // for
	} // init()
} // class InitManager
