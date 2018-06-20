package com.ar.netty.server;

import java.util.List;

import com.ar.common.logging.ARLog;

/***************************
 * <pre>
 * Netty 서버를 구동시킨다.
 * </pre>
 * @author Bak Sang Heon
 *
 ***************************/
public class StartServer {
	private static ARLog log = ARLog.getLogger(StartServer.class);
	
	/*******************
	 * <pre>
	 * 서버를 구동한다.
	 * </pre>
	 *******************/
	public void start() {
		log.info("### Server start");
		
		try {
			ServerLoader loader = new ServerLoader();
			List<ServerSet> servers = loader.load();
			log.debug("SERVER COUNT : {}", servers.size());
			for (ServerSet server : servers) {
				try {
					String name = server.getName();
					int port = server.getPort();
					
					boolean tcpNoDelay = server.getTcpNoDelay();
					long timeout = server.getTimeout();
					boolean useThreadPool = server.getUseThreadPool();
					boolean useNio = server.getUseNio();
					int maxThreadCount = server.getMaxThreadCount();
					
					String handler = server.getHandler();
					List<String> pipelines = server.getPipelineNames();
					
					log.info("    NAME [{}]", name);
					log.info("    PORT [{}]", port);
					log.info("    OPTIONS");
					log.info("        TCP NO DELAY [{}]", tcpNoDelay);
					log.info("        TIMEOUT [{}]", timeout);
					log.info("        USE NIO [{}]", useNio);
					log.info("        USE THREAD POOL [{}]", useThreadPool);
					log.info("        MAX THREAD COUNT [{}]", maxThreadCount);
					log.info("    HANDLER [{}]", handler);
					for (String pipeline : pipelines) {
						String key = pipeline.toUpperCase();
						String value = server.getPipeline(pipeline);
						log.info("    {} [{}]", key, value);
					} // for
					
					server.start();
				} catch (Exception e) {
					log.error("서버 구동 실패", e);
				} // try
			} // for
		} catch (Exception e) {
			log.error(e);
		}
		log.info("### All server start completed");
	} // start()
	
} // class StartServer
