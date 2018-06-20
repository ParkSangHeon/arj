package com.ar.netty.client;

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
import com.ar.netty.server.ServerSet;

/******************************
 * <pre>
 * Client 설정 파일을 로드한다.
 * </pre>
 * @author Bak Sang Heon
 *
 ******************************/
public class ClientLoader {
	private static ARLog log = ARLog.getLogger(ClientLoader.class);
	
	public List<ClientSet> load() {
		log.debug("Load server configuration");
		ARConfig config = ARConfig.getInstance();
		String filepath = config.getValue(NetConst.CLIENT_CONFIG);
		Document doc = this.parse(filepath);
		
		if (doc == null) return new ArrayList<ClientSet>();
		
		List<ClientSet> clientList = this.getClientList(doc);

		return clientList ;
	} // load()
	
	/*******************
	 * <pre>
	 * 설정 파일을 파싱한다.
	 * </pre>
	 * @param filepath
	 * @return
	 *******************/
	public Document parse(String path) {
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
	} // parse();
	
	/**************************************************
	 * <pre>
	 * XML 파일을 파싱하여 ClientSet 목록을 생성, 반환한다.
	 * </pre>
	 * @param doc
	 * @return
	 **************************************************/
	private List<ClientSet> getClientList(Document doc) {
		List<ClientSet> clientList = new ArrayList<ClientSet>();
		
		Element root = doc.getRootElement();
		List<Element> clients = root.getChildren(NetConst.TAG_CLIENT);
		for (Element client : clients) {
			log.debug("FIND CLIENT TAG");
			ClientSet clientSet = new ClientSet();
			
			String client_name = client.getAttributeValue(NetConst.ATTR_NAME);
			String host = client.getAttributeValue(NetConst.ATTR_HOST);
			String portStr = client.getAttributeValue(NetConst.ATTR_PORT);
			int port = Integer.parseInt(portStr);
			
			clientSet.setName(client_name);
			clientSet.setHost(host);
			clientSet.setPort(port);
			
			log.debug("    NAME : {}", client_name);
			log.debug("    PORT : {}", port);
			
			log.debug("    OPTIONS");
			Element setting = client.getChild(NetConst.TAG_SETTING);
			List<Element> params = setting.getChildren(NetConst.TAG_PARAM);
			for (Element param : params) {
				String name = param.getAttributeValue(NetConst.ATTR_NAME);
				String value = param.getValue().trim();
				
				log.debug("        {} : {}", name, value);
				
				if (name.equals(NetConst.TCP_NO_DELAY)) {
					// TcpNoDelay
					boolean tcpNoDelay = Boolean.parseBoolean(value);
					clientSet.setTcpNoDelay(tcpNoDelay);
				} else if (name.equals(NetConst.TIME_OUT)) {
					// TimeOut
					long timeout = Long.parseLong(value);
					clientSet.setTimeout(timeout);
				} else if (name.equals(NetConst.KEEP_ALIVE)) {
					// KeepAlive
					boolean keepAlive = Boolean.parseBoolean(value);
					clientSet.setKeepAlive(keepAlive);
				} else if (name.equals(NetConst.USE_THREAD_POOL)) {
					// UseThreadPool
					boolean useThreadPool = Boolean.parseBoolean(value);
					clientSet.setUseThreadPool(useThreadPool);
				} else if (name.equals(NetConst.MAX_THREAD_COUNT)) {
					// MaxThreadCount
					int maxThreadCount = Integer.parseInt(value);
					clientSet.setMaxThreadCount(maxThreadCount);
				} else if (name.equals(NetConst.USE_NIO)) {
					// UseNio
					boolean useNio = Boolean.parseBoolean(value);
					clientSet.setUseNio(useNio);
				} // if
			} // for
			
			/* Get pipeline */
			Element pipeline = client.getChild(NetConst.TAG_PIPELINE);
			List<Element> codecs = pipeline.getChildren(NetConst.TAG_CODEC);
			for(Element codec : codecs) {
				String name = codec.getAttributeValue(NetConst.ATTR_NAME);
				String clazz = codec.getAttributeValue(NetConst.ATTR_CLASS);
				clientSet.addPipeline(name, clazz);
				log.debug("    PIPELINE : {}", name);
				log.debug("               {}", clazz);
			} // for
			
			/* Get handler */
			Element handlerTag = client.getChild(NetConst.TAG_HANDLER);
			String handler = handlerTag.getAttributeValue(NetConst.ATTR_CLASS);
			clientSet.setHandler(handler);
			log.debug("    HANDLER : {}", handler);
			
			clientList.add(clientSet);
		} // for
		
		return clientList;
	} // getClientList()
	
	
} // class ClientLoader


