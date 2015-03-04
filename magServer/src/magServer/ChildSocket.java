package magServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//import java.util.concurrent.locks.ReentrantLock;

class ChildSocket implements Runnable {
	Mortar mortar;
	Socket dSock;
	PrintWriter os;
	BufferedReader is;

	// private final ReentrantLock lock = new ReentrantLock();

	public void acceptSocket(Socket newSock) throws Exception {
		this.dSock = newSock;
		create();
	}

	public void setMortar(Mortar newMortar) {
		this.mortar = newMortar;
	}

	public void create() throws Exception {
		// this.os = new OutputStreamWriter(dSock.getOutputStream(), "UTF-8");
		//For sending:: Use .write to send data over os
		this.os = new PrintWriter(dSock.getOutputStream(), true);
		this.is = new BufferedReader(new InputStreamReader(dSock.getInputStream())); // For receiving::
	}

	public void sendToSocket(String message){
		System.out.println("Sending through Socket...");
		os.println(message);
		os.flush();
		System.out.println("Message Sent");
	}

	public void run() {
		System.out.println("Sending Mortar " + this.mortar.ID + " it's Identity");
		sendToSocket("0" + this.mortar.ID + System.getProperty("line.separator")); // Send just initialized mortar it's ID
		while (true) {
			String read = "";
			try {
				read = is.readLine();
				if (read == null) {
					// This block is a dead end statement to keep the
					// server from interpreting an empty buffered reader as
					// constant null messages
					Thread.sleep(500);
				} else if (read.equals("closeme")) {
					System.out.println("Client closed connection");
					break;
				} else if (read == "") {
					System.out
							.println("Receieved an empty string; Tossing it.");
				} else {
					System.out.println("Received Message!");
					System.out.println("Updating Mortar " + this.mortar.ID);
					mortar.receiveData(read);
				}
			} catch (IOException | InterruptedException e) {
				System.out.println("Failed to read.");
				e.printStackTrace();
			}
		}

		try {
			//TODO Close Print Writer
			this.dSock.close();
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
