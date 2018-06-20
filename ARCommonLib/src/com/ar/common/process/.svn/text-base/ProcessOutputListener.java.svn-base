package com.ar.common.process;

/***********************************************************
 * <pre>
 * ProcessOutputMonitor에서 가져온 메세지를 수신하기 위한 인터페이스.
 * </pre>
 * @author Bak Sang Heon
 *
 ***********************************************************/
public interface ProcessOutputListener {
	public enum Type {
		IN,
		OUT,
		ERR
	};
	public void messageReceived(Type type, char[] buffer, int offset, int len);
} // interface ProcessOutputListener
