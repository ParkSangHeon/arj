package com.ar.http;

/********************************
 * <pre>
 * 필수 파라미터 누락시 발생하는 오류.
 * </pre>
 * @author Bak Sang Heon
 *
 ********************************/
public class RequiredParameterNotFoundException extends Throwable {
	
	private static final long serialVersionUID = -6314388332831796603L;
	
	private String param;
	
	public RequiredParameterNotFoundException(String param) {
		super();
		this.param = param;
	}
	
	public RequiredParameterNotFoundException(String param, String msg) {
		super(msg);
		this.param = param;
	}
	
	public RequiredParameterNotFoundException(String param, Throwable e) {
		super(e);
		this.param = param;
	}
	
	public RequiredParameterNotFoundException(String param, String msg, Throwable e) {
		super(msg, e);
		this.param = param;
	}
	
	public String getParam() { return this.param; }
} // class RequiredParameterNotFoundException
