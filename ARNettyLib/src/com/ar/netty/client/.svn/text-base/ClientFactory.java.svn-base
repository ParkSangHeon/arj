package com.ar.netty.client;

import java.util.List;

import com.ar.common.logging.ARLog;

/*****************************************
 * <pre>
 * 서버 연결을 위한 클라이언트를 생성하는 클래스.
 * </pre>
 * @author Bak Sang Heon
 *
 *****************************************/
public class ClientFactory {
	private static ARLog log = ARLog.getLogger(ClientFactory.class);
	
	private static List<ClientSet> clientList;
	
	/***************************
	 * <pre>
	 * 해당 클라이언트를 검색한다.
	 * </pre>
	 * @param name
	 * @return
	 ***************************/
	public static ClientSet getClient(String name) {
		if (clientList == null) {
			ClientLoader loader = new ClientLoader();
			clientList = loader.load();
		} // if
		
		for (ClientSet client : clientList) {
			log.debug("Client Name : " + client.getName());
			String client_name = client.getName();
			if (name.equals(client_name)) 
				return client;
		} // for
		
		return null;
	} // getClient()
	
	/******************************************
	 * <pre>
	 * 호스트와 포트 정보로 클라이언ㅌ 연결을 생성한다.
	 * </pre>
	 * @param host
	 * @param port
	 * @return
	 ******************************************/
	public static ClientSet getClient(String host, int port) {
		ClientSet client = new ClientSet();
		client.setHost(host);
		client.setPort(port);
		return client;
	} // getClient()
} // class ClientFactory
