package magServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//import java.util.concurrent.locks.ReentrantLock;

class TabletChildSocket implements Runnable {
	Socket dSock;
	PrintWriter os;
	BufferedReader is;
	// private final ReentrantLock lock = new ReentrantLock();
	private Tablet tablet;

	public void acceptSocket(Socket newSock, Tablet newTablet) throws Exception {
		this.dSock = newSock;
		this.tablet = newTablet;
		create();
	}

	public void create() throws Exception {
		this.os = new PrintWriter(dSock.getOutputStream(), true);
		this.is = new BufferedReader(new InputStreamReader(
				dSock.getInputStream())); // For receiving::
	}

	public void sendToSocket(String message) {
		os.println(message);
	}

	public void run() {
		String read = "";
		while (true) {
			try {
				sendToSocket("?");
				
				read = is.readLine();
				if (read == null) {
					// System.out.println("Null string.");
				} else if (read.equals("closeme")) {
					System.out.println("Tablet closed connection");
					break;
				} else if (read == "") {
					System.out.println("Empty String??");
				} else {					
					tablet.receiveData(read);
				}
				Thread.sleep(200);
			} catch (IOException | InterruptedException e) {
				System.out.println("Failed to read.");
				e.printStackTrace();
			}
		}

		try {
			this.dSock.close();
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
