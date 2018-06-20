package com.ar.common.spec;

import java.io.InputStream;
import java.util.HashMap;

import com.ar.common.config.ARConfig;
import com.ar.common.constant.ARConst;
import com.ar.common.logging.ARLog;
import com.ar.common.xml.ARTag;
import com.ar.common.xml.ARXmlParser;

/*************************************************************
 * <pre>
 * 전문 스펙 정의를 읽어 스펙에 생성에 필요한 데이타 인스턴스를 생성한다.
 * </pre>
 * @author Bak Sang Heon
 *
 *************************************************************/
public class SpecLoader {
	private ARLog log = ARLog.getLogger(SpecLoader.class);
	
	/***************************************
	 * <pre>
	 * InputStream을 통해서 전문 파일을 파싱한다.
	 * </pre>
	 * @param path
	 * @return
	 ***************************************/
	public HashMap<String, ARTag> ContextParser(InputStream path) {
		HashMap<String, ARTag> tags = new HashMap<String, ARTag>();
		try {
			ARXmlParser parser = new ARXmlParser();
			ARTag root_tag = parser.parse(path);
			
			String root_name = root_tag.getName();
			if (root_name.equals("Specs")) {
				for (ARTag child : root_tag.getChildren()) {
					String child_name = child.getName();
					if (child_name.equals("Spec")) {
						tags.put(child.getAttribute("code"), child);
					} // if
				} // for
			} // if
		} catch (Exception e) {
			log.error(e);
		} // try
		return tags;
	} // ContextParser()
	
	public HashMap<String, ARTag> ContextParser(String path) {
		HashMap<String, ARTag> tags = new HashMap<String, ARTag>();
		try {
			ARXmlParser parser = new ARXmlParser();
			ARTag root_tag = parser.parse(path);
			
			String root_name = root_tag.getName();
			if (root_name.equals("Specs")) {
				for (ARTag child : root_tag.getChildren()) {
					String child_name = child.getName();
					if (child_name.equals("Spec")) {
						tags.put(child.getAttribute("code"), child);
					} // if
				} // for
			} // if
		} catch (Exception e) {
			log.error(e);
		} // try
		return tags;
	} // ContextParser()
	
} // class SpecLoader
