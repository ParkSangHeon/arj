package com.ar.netty.codec.spec;

import java.nio.ByteBuffer;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.ar.common.logging.ARLog;
import com.ar.common.spec.Spec;
import com.ar.common.spec.SpecFactory;
import com.ar.common.util.ARUtil;

/***************************************************
 * <pre>
 * 스트림 데이타를 Spec형식으로 변환하여 Handler로 전달한다.
 * </pre>
 * @author Bak Sang Heon
 *
 ***************************************************/
public class SpecDecoder extends FrameDecoder {
	private static ARLog log = ARLog.getLogger(SpecDecoder.class);
	
	private Spec spec;
	
	@Override
	protected Object decode(ChannelHandlerContext context, Channel channel, ChannelBuffer buffer) throws Exception {
		log.debug("SpecDecoder.decode()");
		
		// 읽은 수 있는 데이타가 4바이트 미만이면 처리하지 않는다.
		if (buffer.readableBytes() < 4) {
			return null;
		} // if
		
		// 코드가 왔다면 마크!
		buffer.markReaderIndex();
		
		byte[] code_bytes = new byte[4];
		buffer.readBytes(code_bytes, 0, 4);
		String code = new String(code_bytes, "US-ASCII");
		
		// 코드에 해당하는 Spec클래스 생성.
		this.spec = SpecFactory.getSpec(code.trim());
		if (this.spec == null) {
			log.error("Spec Code Invalid");
			channel.close();
			return null;
		} // if
		
		log.info("Spec Code [" + code +"]");
		
		if (buffer.readableBytes() < 8) {
			buffer.resetReaderIndex();
			return null;
		} // if
		
		byte[] tmp_bytes = new byte[8];
		buffer.readBytes(tmp_bytes, 0, 8);
		
		String hex_length = new String(tmp_bytes, "US-ASCII");
		log.info("Spec Hex Length : " + hex_length);
		
		byte[] length_bytes = ARUtil.hex2byte(hex_length);
		log.info("Hex bytes Length : " + length_bytes.length);
		
		ByteBuffer int_buffer = ByteBuffer.allocate(Integer.SIZE / 8);
		int_buffer = ByteBuffer.wrap(length_bytes);
		int length = int_buffer.getInt();
		
		log.info("Spec Length : " + length);
		if (length != this.spec.getLength()) {
			log.error("Spec Length invalid " + this.spec.getLength() + " : " + length);
			channel.close();
			return null;
		} // if
		
		if (buffer.readableBytes() < (spec.getLength() - 12)) {
			log.debug("waiting packet!!!");
			buffer.resetReaderIndex();
			return null;
		}
		
		byte[] stream = new byte[spec.getLength()];
		buffer.readBytes(stream, 0, spec.getLength());
		
		this.spec.setStreamBody(stream);
		//buffer.clear();
		
		log.debug("Send Spec to Handler");
		return this.spec;
	} // decode

} // class SpecDecoder
