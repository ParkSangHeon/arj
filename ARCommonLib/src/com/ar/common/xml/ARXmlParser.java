package com.ar.common.xml;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ar.common.logging.ARLog;


/*************************************
 * <pre>
 * XML 데이타를 파싱하여 맵 형태로 반환한다.
 * </pre>
 * @author Bak Sang Heon
 *
 *************************************/
public class ARXmlParser {
	private ARLog log = ARLog.getLogger(ARXmlParser.class);
	
	public ARTag parse(String path) throws Exception {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new File(path));
			
			Element root = document.getRootElement();
			ARTag rootTag = this.getTag(root);
			return rootTag;
		} catch(Exception e) {
			log.error(e);
		} // try
		return null;
	} // parse()
	
	/*****************************************
	 * <pre>
	 * InputStream으로 부터 XML데이타를 읽어온다.
	 * </pre>
	 * @param in
	 * @return
	 * @throws Exception
	 *****************************************/
	public ARTag parse(InputStream in) throws Exception {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(in);
			
			Element root = document.getRootElement();
			ARTag rootTag = this.getTag(root);
			return rootTag;
		} catch (Exception e) {
			log.error(e);
		} // try
		return null;
	} // parse()
	
	public ARTag getTag(Element element) {
		ARTag tag = new ARTag();
		
		tag.setName(element.getName());
		
		List<Element> children = element.getChildren();
		if (children.size() > 0) {
			for (Element child : children) {
				ARTag childTag = this.getTag(child);
				tag.addChild(childTag);
			} // for
		} // if
		
		List<Attribute> attributes = element.getAttributes();
		if (attributes.size() > 0) {
			for (Attribute attribute : attributes) {
				String name = attribute.getName();
				String value = attribute.getValue();
				tag.addAttribute(name, value);
			} // for
		} // for
		
		String body = element.getValue().trim();
		if (!body.equals("")) {
			tag.setBody(body);
		} // if
		
		return tag;
	} // getTag()
} // class ARXmlParser
