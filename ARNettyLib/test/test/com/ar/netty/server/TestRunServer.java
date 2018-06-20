package test.com.ar.netty.server;

import com.ar.netty.server.StartServer;

public class TestRunServer {
	public static void main(String[] args) {
		StartServer server = new StartServer();
		server.start();
	}
}
