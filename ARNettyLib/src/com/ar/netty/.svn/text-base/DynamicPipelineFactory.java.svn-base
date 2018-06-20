package com.ar.netty;

import java.util.List;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.ar.netty.server.ServerSet;

/*****************************************
 * <pre>
 * 동적으로 파이프라인을 생성하는 팩토리 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *****************************************/
public class DynamicPipelineFactory implements ChannelPipelineFactory {
	private NetSet _set;
	
	public DynamicPipelineFactory(NetSet set) {
		this._set = set;
	} // Constructor
	
	/********************
	 * <pre>
	 * 파이프라인을 생성한다.
	 * </pre>
	 ********************/
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		 
		// Set codecs
		List<String> keys = this._set.getPipelineNames();
		for (String key : keys) {
			Class<?> clazz = Class.forName(this._set.getPipeline(key));
			Object o = clazz.newInstance();
			if (o instanceof ChannelHandler)
				pipeline.addLast(key, (ChannelHandler)o);
		} // for
		
		// Set handler
		Class<?> clazz = Class.forName(this._set.getHandler());
		Object o = clazz.newInstance();
		if (o instanceof ChannelHandler) {
			pipeline.addLast("handler", (ChannelHandler)o);
		} // if
		
		return pipeline;
	} // getPipeline()
	
	

} // class DynamicPipelineFactory 
