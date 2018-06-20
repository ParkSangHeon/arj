package com.ar.netty.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.ChannelGroupFutureListener;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.oio.OioServerSocketChannelFactory;

import com.ar.common.logging.ARLog;
import com.ar.netty.DynamicPipelineFactory;
import com.ar.netty.NetSet;

/*********************************************************
 * <pre>
 * 설정한 개별 서버를 정의한다.
 * 서버에 대한 정보를 담고 있으면, 서버의 구동 및 정지기능을 수행한다.
 * </pre>
 * @author Bak Sang Heon
 *
 *********************************************************/
public class ServerSet implements NetSet {
	private static ARLog log = ARLog.getLogger(ServerSet.class);
	
	private String 		_name;
	private int 		_port;
	private boolean 	_tcpNoDelay;
	private boolean 	_keepAlive;
	private long 		_timeout;
	private boolean		_useThreadPool;
	private int 		_maxThreadCount;
	private boolean 	_useNio = true;
	
	private Map<String, String> _pipeline = new HashMap<String, String>();
	private String _handler;
	
	private ServerBootstrap _serverBootstrap;
	private Channel _serverChannel;
	
	private ChannelGroup _allChannels = new DefaultChannelGroup();
	
	/* Setter */
	public void setName(String name) { this._name = name; }
	public void setPort(int port) { this._port = port; }
	public void setTcpNoDelay(boolean tcpNoDelay) { this._tcpNoDelay = tcpNoDelay; }
	public void setKeepAlive(boolean keepAlive) { this._keepAlive = keepAlive; }
	public void setTimeout(long timeout) { this._timeout = timeout; }
	public void setUseThreadPool(boolean useThreadPool) { this._useThreadPool = useThreadPool; }
	public void setMaxThreadCount(int maxThreadCount) { this._maxThreadCount = maxThreadCount; }
	public void setUseNio(boolean useNio) { this._useNio = useNio; }
	public void addPipeline(String key, String value) {
		this._pipeline.put(key, value);
	} 
	
	public void setHandler(String handler) { this._handler = handler; }
	
	/* Getter */
	public String getName() { return this._name; }
	public int getPort() { return this._port; }
	public boolean getTcpNoDelay() { return this._tcpNoDelay; }
	public boolean getKeepAlive() { return this._keepAlive; }
	public long getTimeout() { return this._timeout; }
	public boolean getUseThreadPool() { return this._useThreadPool; }
	public int getMaxThreadCount() { return this._maxThreadCount; }
	public boolean getUseNio() { return this._useNio; }
	
	@Override
	public String getPipeline(String key) { return this._pipeline.get(key); }
	@Override
	public String getHandler() { return this._handler; }

	public Map<String, String> getPipeline() { return this._pipeline; }
	
	/*****************************************
	 * <pre>
	 * 파이프라인에 넣을 ChannelHandler 목록 반환.
	 * </pre>
	 * @return
	 *****************************************/
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
	
	/*********************
	 * <pre>
	 * 서버를 구동한다.
	 * </pre>
	 *********************/
	public void start() throws Exception {
		if (this._serverChannel != null && this._serverChannel.isBound()) {
			throw new Exception("Server was bound");
		} // if
		
		ServerSocketChannelFactory socketChannelFactory = null;
		if (this._useNio) {
			// Use thread pool
			if (this._useThreadPool || !(this._maxThreadCount <= 0)) {
				socketChannelFactory = new NioServerSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool(),
					this._maxThreadCount
				);
			} else {
				socketChannelFactory = new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()
					);
			} // if
		} else {
			// No thread pool
			socketChannelFactory = new OioServerSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool()
				);
		} // if
		
		this._serverBootstrap = new ServerBootstrap(socketChannelFactory);
		
		// Set Options
		this._serverBootstrap.setOption("child.tcpNoDelay", this._tcpNoDelay);
		this._serverBootstrap.setOption("child.keepAlive", this._keepAlive);
		this._serverBootstrap.setOption("child.connectTimeoutMillis", this._timeout);
		
		// Set pipeline factory
		DynamicPipelineFactory pipelineFactory = new DynamicPipelineFactory(this);
		this._serverBootstrap.setPipelineFactory(pipelineFactory);
		
		// Set socket bind
		this._serverChannel = _serverBootstrap.bind(new InetSocketAddress(this._port));

		log.info("Server start completed");
	} // start()
	
	/******************
	 * <pre>
	 * 서버를 정지한다.
	 * </pre>
	 ******************/
	public void stop() {
		// Child channel close
		ChannelGroupFuture closeChildChannel = this._allChannels.close();
		closeChildChannel.addListener(new ChannelGroupFutureListener(){
			@Override
			public void operationComplete(ChannelGroupFuture future) throws Exception {
				log.info("All child channel closed");
			} // operationComplete()
		});
		
		// Server channel close
		ChannelFuture closeFuture = this._serverChannel.close();
		closeFuture.addListener(new ChannelFutureListener(){
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				log.info("Server channel closed");
			} // operationCompleted
		});
		closeFuture.awaitUninterruptibly();
		
		this._serverBootstrap.releaseExternalResources();
		
		this._serverChannel = null;
		this._serverBootstrap = null;
	} // stop()
	
} // class ServerSet
