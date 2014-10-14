package magServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

class Tablet{
	int state = 0;
	public void send(String message){
		this.tabletListener.sendToSocket(message);
	}
	
	public void receiveData(String message){ //gets data from actual tablet, sends up to controller to update mortar object
		int ID = Integer.parseInt(message.substring(0,1));
		String fuze = message.substring(2);
		String gps = message.substring(3,16);
		String elev = message.substring(17,21);
		controller.updateMortar(ID, fuze, gps, elev);
	}
	TabletSocket tabletListener = new TabletSocket();
}
	
class TabletSocket{
	String host = "192.168.0.1";
	int port = 4444;
	Socket dSock = new Socket(host, port);
	OutputStreamWriter os = new OutputStreamWriter(dSock.getOutputStream(), "UTF-8"); //For sending:: Use .write to send data over os
	BufferedReader is = new BufferedReader(new InputStreamReader(dSock.getInputStream())); //For receiving::

	public void sendToSocket(String message) {
	   os.write(message);
	}
	  
	public void run(){
		String message = "";
	    while((str = is.readLine()) != null) {
	    	message = message + str
	    }
	    super.receiveData(message)
	}
}