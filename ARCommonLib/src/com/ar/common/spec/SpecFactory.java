package com.ar.common.spec;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import com.ar.common.config.ARConfig;
import com.ar.common.constant.ARConst;
import com.ar.common.logging.ARLog;
import com.ar.common.xml.ARTag;


/**********************************************
 * <pre>
 * 전문 구현체를 생성한다.
 * 사용법
 * Spec spec = SpecFactory.getSpec("A001");
 * spec.getXXX(0);
 * spec.setXXX("TEST", "DATA");
 * </pre>
 * @author Bak Sang Heon
 *
 **********************************************/
public class SpecFactory {
	
	private static ARLog log = ARLog.getLogger(SpecFactory.class);
	
	private static HashMap<String, ARTag> specs;
	
	/*********************************************
	 * code에 해당하는 전문 클래스를 생성하여 반환한다.
	 * @param code
	 * @return Spec
	 *********************************************/
	public static Spec getSpec(String code) {
		if (specs == null) {
			// 스펙 목록을 생성한다.
			SpecLoader loader = new SpecLoader();
			ARConfig config = ARConfig.getInstance();
			String path = config.getValue(ARConst.SPEC_PATH);
			log.debug("#### Find Spec in!! : " +  path);
			
			InputStream in = SpecFactory.class.getClassLoader().getResourceAsStream(path);
			if (in == null) {	
				specs = loader.ContextParser(path);
				log.debug("InputStream is null");
			} else {  			
				specs = loader.ContextParser(in);
				log.debug("InputStream is not null");
			} // if
			
			try {
				if (in != null) in.close();
			} catch (Exception e) {
				log.error(e);
			} // try
		} // if
				
		ARTag tag = specs.get(code);
		
		if (tag == null) {
			log.debug("#### Spec Tag is null [CODE:" + code + "]");
			return null;
		}
		
		SpecImpl spec_impl = new SpecImpl(tag);
		return (Spec)spec_impl;
	} // getSpec()
	
	/**
	 * 테스트 코드
	 * @param args
	 */
	public static void main(String[] args) {
		Spec specA1 = SpecFactory.getSpec("A1");
		log.debug("A1 : " + specA1);
		log.debug("Code : " + specA1.getCode());
		log.debug("Length : " + specA1.getLength());
		log.debug("Version : " + specA1.getVersion());
	} // main()
} // class SpecFactory
