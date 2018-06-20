package com.ar.common.init;

/****************************
 * <pre>
 * 초기화시 발생하는 예외 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ****************************/
public class InitException extends Throwable {
	private static final long serialVersionUID = -521687288409203083L;
	
	public InitException(String msg) {
		super(msg);
	}
	public InitException(Throwable e) {
		super(e);
	}
	public InitException(String msg, Throwable e) {
		super(msg, e);
	}
} // class InitException
