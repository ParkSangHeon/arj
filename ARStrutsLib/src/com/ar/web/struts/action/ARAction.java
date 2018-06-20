package com.ar.web.struts.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.ar.common.collection.QMap;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/******************************************
 * <pre>
 * Struts2의 액션 구성을 위한 최상위 액션 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ******************************************/
public abstract class ARAction implements ModelDriven<Map<String, Object>>, Preparable, SessionAware {
	protected QMap params = null;
	public void setParams(QMap params){
		this.params = params;
	} // setParams();
	public QMap getParams(){
		if(params == null) params = new QMap();
		return this.params;
	} // getParams();

	public QMap getModel(){
		if(params == null) params = new QMap();
		return this.params;
	} // getModel()
	
	public abstract void prepare();

	protected Map<String, Object> sessionMap = null;
	
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}	
} // ARAction
