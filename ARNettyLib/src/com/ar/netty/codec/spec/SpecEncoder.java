package com.ar.netty.codec.spec;


import org.jboss.netty.buffer.ChannelBuffer;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import org.jboss.netty.buffer.ChannelBuffers;

import com.ar.common.logging.ARLog;
import com.ar.common.spec.Spec;

/*****************************************************
 * <pre>
 * Handler로 부터 전달받은 Spec 클래스를 Stream으로 전달한다.
 * </pre>
 * @author Bak Sang Heon
 *
 *****************************************************/
public class SpecEncoder extends OneToOneEncoder {
	private static ARLog log = ARLog.getLogger(SpecEncoder.class);
	
	@Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if (!(msg instanceof Spec)) {
			return msg;
		} // if
		
		Spec spec = (Spec)msg;
		
		byte[] spec_data = spec.getStream();
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(spec_data.length);
		buffer.writeBytes(spec_data);
		
		log.debug("Encode Data [" + new String(spec_data) + "]");
		return buffer;
	} // encode()
		
} // class SpecEncoder
