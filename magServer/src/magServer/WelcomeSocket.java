package magServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

public class WelcomeSocket extends Controller implements Runnable {
	String host;
	int port;
	ServerSocket sock;
	InetSocketAddress addr;

	public void init(String newHost, int newPort) throws IOException {
		this.host = newHost;
		this.port = newPort;
		this.addr = new InetSocketAddress(host, port);
		this.sock = new ServerSocket();
		this.sock.bind(addr);
		run();
	}

	public void run() {
		try {
			Socket clientSocket = this.sock.accept();
			mag.tubes.get(mag.getCapacity() - 1).mortar.mortarListener.acceptSocket(clientSocket);
			mag.tubes.get(mag.getCapacity() - 1).mortar.mortarListener.run();
			super.rotateMagazine(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
