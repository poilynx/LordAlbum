package cc.jmpesp.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(8722);
		Socket socket = null;
		System.out.println("服务器已经启动!");
		while (true) {
			socket = ss.accept();
			ServerThread st = new ServerThread(socket);
			st.start();
		}
	}

}
