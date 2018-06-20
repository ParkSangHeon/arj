package com.ar.netty.server;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ar.common.config.ARConfig;
import com.ar.common.logging.ARLog;
import com.ar.netty.NetConst;

/*****************************************************************
 * <pre>
 * 서버 설정파일을 불러와 각각의 서버별 ServerSet 인스턴스를 생성, 반환한다.
 * </pre>
 * @author Bak Sang Heon
 *
 *****************************************************************/
public class ServerLoader {
	private static ARLog log = ARLog.getLogger(ServerLoader.class);
	
	/******************************
	 * <pre>
	 * ServerSet 리스트를 반환한다.
	 * </pre>
	 * @return
	 ******************************/
	public List<ServerSet> load() {
		log.debug("Load server configuration");
		ARConfig config = ARConfig.getInstance();
		String filepath = config.getValue(NetConst.SERVER_CONFIG);
		Document doc = this.parse(filepath);
		
		if (doc == null) return new ArrayList<ServerSet>();
		
		List<ServerSet> serverlist = this.getServerList(doc);

		return serverlist;
	} // load()
	
	/***********************
	 * <pre>
	 * 서버 설정파일을 파싱한다.
	 * </pre>
	 * @param path
	 ***********************/
	private Document parse(String path) {
		InputStream in = null;
		Document doc = null;
		try {
			in = ClassLoader.getSystemResourceAsStream(path);
			SAXBuilder builder = new SAXBuilder();
			if (in == null) {
				doc = builder.build(new File(path));
			} else {
				doc = builder.build(in);
			} // if
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (in != null) try { in.close(); } catch (Exception e) { log.error(e); }
		} // try
		return doc;
	} // parse()
	
	/**************************************************
	 * <pre>
	 * Document에서 서버 정보를 읽어 ServerSet을 생성한다.
	 * </pre>
	 * @param doc
	 * @return
	 **************************************************/
	private List<ServerSet> getServerList(Document doc) {
		List<ServerSet> serverList = new ArrayList<ServerSet>();
		
		Element root = doc.getRootElement();
		List<Element> servers = root.getChildren(NetConst.TAG_SERVER);
		for (Element server : servers) {
			log.debug("FIND SERVER TAG");
			ServerSet serverSet = new ServerSet();
			
			String server_name = server.getAttributeValue(NetConst.ATTR_NAME);
			String port_name = server.getAttributeValue(NetConst.ATTR_PORT);
			int port = Integer.parseInt(port_name);
			serverSet.setName(server_name);
			serverSet.setPort(port);
			
			log.debug("    NAME : {}", server_name);
			log.debug("    PORT : {}", port);
			
			/* Get settings */
			log.debug("    OPTIONS");
			Element setting = server.getChild(NetConst.TAG_SETTING);
			List<Element> params = setting.getChildren(NetConst.TAG_PARAM);
			for (Element param : params) {
				String name = param.getAttributeValue(NetConst.ATTR_NAME);
				String value = param.getValue().trim();
				
				log.debug("        {} : {}", name, value);
				
				if (name.equals(NetConst.TCP_NO_DELAY)) {
					// TcpNoDelay
					boolean tcpNoDelay = Boolean.parseBoolean(value);
					serverSet.setTcpNoDelay(tcpNoDelay);
				} else if (name.equals(NetConst.TIME_OUT)) {
					// TimeOut
					long timeout = Long.parseLong(value);
					serverSet.setTimeout(timeout);
				} else if (name.equals(NetConst.KEEP_ALIVE)) {
					// KeepAlive
					boolean keepAlive = Boolean.parseBoolean(value);
					serverSet.setKeepAlive(keepAlive);
				} else if (name.equals(NetConst.USE_THREAD_POOL)) {
					// UseThreadPool
					boolean useThreadPool = Boolean.parseBoolean(value);
					serverSet.setUseThreadPool(useThreadPool);
				} else if (name.equals(NetConst.MAX_THREAD_COUNT)) {
					// MaxThreadCount
					int maxThreadCount = Integer.parseInt(value);
					serverSet.setMaxThreadCount(maxThreadCount);
				} else if (name.equals(NetConst.USE_NIO)) {
					// UseNio
					boolean useNio = Boolean.parseBoolean(value);
					serverSet.setUseNio(useNio);
				} // if
			} // for
			
			/* Get pipeline */
			Element pipeline = server.getChild(NetConst.TAG_PIPELINE);
			List<Element> codecs = pipeline.getChildren(NetConst.TAG_CODEC);
			for(Element codec : codecs) {
				String name = codec.getAttributeValue(NetConst.ATTR_NAME);
				String clazz = codec.getAttributeValue(NetConst.ATTR_CLASS);
				serverSet.addPipeline(name, clazz);
				log.debug("    PIPELINE : {}", name);
				log.debug("               {}", clazz);
			} // for
			
			/* Get handler */
			Element handlerTag = server.getChild(NetConst.TAG_HANDLER);
			String handler = handlerTag.getAttributeValue(NetConst.ATTR_CLASS);
			serverSet.setHandler(handler);
			log.debug("    HANDLER : {}", handler);
			
			serverList.add(serverSet);
			
		} // for
		
		return serverList;
	} // getServerList()
	
} // class ServerLoader
