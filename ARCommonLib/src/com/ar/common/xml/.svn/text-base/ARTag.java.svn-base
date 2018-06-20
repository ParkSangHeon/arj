package com.ar.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**************************
 * <pre>
 * 태그 정보를 담고있는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 **************************/
public class ARTag {
	private String name;
	private String body;
	private HashMap<String, String> attributes;
	private ArrayList<ARTag> children;
	
	public void setName(String name) {
		this.name = name; 
	} // setName()
	
	public String getName() {
		return this.name;
	} // getName()
	
	public void setBody(String body) {
		this.body = body;
	} // setBody()
	
	public String getBody() {
		return this.body;
	} // getBody()
	
	public void addAttribute(String name, String value) {
		if (this.attributes == null)
			this.attributes = new HashMap<String, String>();
		this.attributes.put(name, value);
	} // addAttribute()
	
	public void addChild(ARTag child) {
		if (this.children == null)
			this.children = new ArrayList<ARTag>();
		this.children.add(child);
	} // addChild()
	
	public Map<String, String> getAttributes(){
		return this.attributes;
	} // getAttributes()
	
	public String getAttribute(String name) {
		return this.attributes.get(name);
	} // getAttribute()
	
	public List<ARTag> getChildren() {
		return this.children;
	} // getChildren()
	
	public ARTag getChild(String name) {
		ARTag result = null;
		for (ARTag tag : this.children) {
			String tagName = tag.getName();
			if (name.equals(tagName)) {
				result = tag;
			} // for
		} // for
		return result;
	} // getChild
	
	public String toString() {
		String result = this.toString(0);
		return result;
	} // toString()
	
	public String toString(int tab_idx) {
		StringBuilder buffer = new StringBuilder();
		
		StringBuffer tab = new StringBuffer();
		for (int i = 0; i < tab_idx; i++) {
			tab.append("   ");
		} // for
		
		// Tag Name
		buffer.append(tab).append("<");
		buffer.append(this.name);
		
		// Attributes
		if (this.attributes != null && this.attributes.size() > 0) {
			Iterator<String> iter = this.attributes.keySet().iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				String value = this.getAttribute(key);
				buffer.append(" ").append(key).append("=").append(value);
			} // while
		} // if
		
		buffer.append(">");
		
		// Body
		if (this.body != null) {
			buffer.append(body);
		} // if
		
		buffer.append("\n");
		
		// Children
		if (this.children != null && this.children.size() > 0) {
			for (ARTag child : this.children) {
				buffer.append(child.toString(tab_idx+1));
			} // for
		} // if
		
		buffer.append(tab).append("</").append(this.name).append(">\n");
		
		return buffer.toString(); 
	} // toString()
	
} // class ARTag
