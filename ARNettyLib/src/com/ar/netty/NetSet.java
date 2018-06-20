package com.ar.netty;

import java.util.List;

/*************************************
 * <pre>
 * 네트워크 연결 설정을 담당하는 인터페이스.
 * </pre>
 * @author Bak Sang Heon
 *
 *************************************/
public interface NetSet {
	public List<String> getPipelineNames();
	public String getPipeline(String key);
	public String getHandler();
} // class NetSet
