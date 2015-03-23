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
	boolean hasData = false;

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
		//os.flush();
		System.out.println("Message Sent");
	}

	public void run() {
		System.out.println("Sending Mortar " + this.mortar.ID + " it's Identity");
		// Send just initialized mortar it's ID
		sendToSocket("0" + this.mortar.ID+System.getProperty("line.separator")); 
		//Send mortar population message to tablet. Use tube position instead of mortar ID.
		this.mortar.cont.getTablet().tabletListener.sendToSocket(this.mortar.getID());
		//this.mortar.cont.getTablet().tabletListener.sendToSocket("0" + this.mortar.getPos() + System.getProperty("line.separator"));
		String read = "";
		while (true) {
			System.out.println("loop");
			try {
				
				if (hasData == true){
					sendToSocket(this.mortar.makeMessage() + System.getProperty("line.separator"));
					hasData = false;
				} 
				else { 
					sendToSocket("?"+this.mortar.getID()+System.getProperty("line.separator"));
				}
				
				read = is.readLine();
				if (read == null) {
					// This block is a dead end statement to keep the
					// server from interpreting an empty buffered reader as
					// constant null messages
				} else if (read.equals("closeme")) {
					System.out.println("Client closed connection");
					break;
				} else if (read == "") {
					System.out.println("Receieved an empty string; Tossing it.");
				} else {
					System.out.println("Received Message!");
					System.out.println("Updating Mortar " + this.mortar.ID);
					mortar.receiveData(read);
				}
				
				Thread.sleep(20);
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
