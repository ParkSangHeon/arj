package com.ar.netty.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.ar.common.logging.ARLog;
import com.ar.netty.DynamicPipelineFactory;
import com.ar.netty.NetSet;

/***********************************
 * <pre>
 * 클라이언트 연결 정보를 담고있는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ***********************************/
public class ClientSet implements NetSet {
	private static ARLog log = ARLog.getLogger(ClientSet.class);
	
	public String 		_name = "default client";
	public String 		_host;
	public int 			_port;
	private boolean 	_tcpNoDelay = false;
	private boolean 	_keepAlive = false;
	private long 		_timeout = 10000;
	private boolean		_useThreadPool = true;
	private int 		_maxThreadCount = 1;
	private boolean 	_useNio = true;
	
	private Map<String, String> _pipeline = new HashMap<String, String>();
	private String _handler;

	/* Setter */
	public void setName(String name) { this._name = name; }
	public void setHost(String host) { this._host = host; }
	public void setPort(int port) { this._port = port; }
	public void setTcpNoDelay(boolean tcpNoDelay) { this._tcpNoDelay = tcpNoDelay; }
	public void setKeepAlive(boolean keepAlive) { this._keepAlive = keepAlive; }
	public void setTimeout(long timeout) { this._timeout = timeout; }
	public void setUseThreadPool(boolean useThreadPool) { this._useThreadPool = useThreadPool; }
	public void setMaxThreadCount(int maxThreadCount) { this._maxThreadCount = maxThreadCount; }
	public void setUseNio(boolean useNio) { this._useNio = useNio; }
	
	public void setHandler(String handler) { this._handler = handler; }
	public void addPipeline(String key, String value) {
		this._pipeline.put(key, value);
	} // if
	
	/* Getter */
	public String getName() { return this._name; }
	public String getHost() { return this._host; }
	public int getPort() { return this._port; }
	public boolean isTcpNoDelay() { return this._tcpNoDelay; }
	public boolean isKeepAlive() { return this._keepAlive; }
	public long getTimeout() { return this._timeout; }
	public boolean isUseThreadPool() { return this._useThreadPool; }
	public int getMaxThreadCount() { return this._maxThreadCount; }
	public boolean isUseNio() { return this._useNio; }
	
	
	/***********************
	 * <pre>
	 * 해당 서버에 연결한다.
	 * </pre>
	 ***********************/
	public void connect() {
		try {
			log.info("#### Connect to peer");
			log.info("     HOST [{}]", this._host);
			log.info("     PORT [{}]", this._port);
			
			ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool()
					)
			);
			
			bootstrap.setPipelineFactory(new DynamicPipelineFactory(this));
			
			bootstrap.connect(new InetSocketAddress(this._host, this._port));
		} catch (Exception e) {
			log.error(e);
		} // try
	} // connect()
	
	/**************************
	 * <pre>
	 * 파이프라인 목록을 반환한다.
	 * </pre>
	 **************************/
	@Override
	public List<String> getPipelineNames() {
		List<String> keys = new ArrayList<String>();
		
		Iterator<String> iter = this._pipeline.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			keys.add(key);
		} // while
		
		return keys;
	} // getPipelineNames()
	
	/**********************
	 * <pre>
	 * 특정 파이프라인을 가져온다.
	 * </pre>
	 **********************/
	@Override
	public String getPipeline(String key) {
		return this._pipeline.get(key);
	} // getPipeline()
	
	/********************
	 * <pre>
	 * 핸들러명을 가져온다.
	 * </pre>
	 ********************/
	@Override
	public String getHandler() {
		return this._handler;
	} // getHandler()
	
	
} // class ClientSet
