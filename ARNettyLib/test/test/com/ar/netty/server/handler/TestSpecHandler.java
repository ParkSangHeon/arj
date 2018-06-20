package test.com.ar.netty.server.handler;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.ar.common.logging.ARLog;
import com.ar.common.spec.Spec;
import com.ar.common.spec.SpecFactory;

/***************************
 * <pre>
 * 서버 테스트용 핸들러 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 ***************************/
public class TestSpecHandler extends SimpleChannelHandler {
	private static ARLog log = ARLog.getLogger(TestSpecHandler.class);
	
	@Override
	public void messageReceived(ChannelHandlerContext context, MessageEvent event) {
		Object message = event.getMessage();
		Spec spec = null;
		if (message instanceof Spec) {
			spec = (Spec)message;
		} // if
		
		String specCode = spec.getCode();
		log.info("Received Spec Code : {}", specCode);
		
		Channel channel = event.getChannel();
		
		Spec resSpec = SpecFactory.getSpec("T002");
		channel.write(resSpec);
	} // messageReceived()
	
	
} // class TestSpecHandler
