package magServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Mortar {
	//need function for after the mortar leaves
	private int fuze = 0;
	private int ID; 
	private String gps = "AA000000000000";
	private String elev = "00000";
	private String message = "iam "+ ID + "," + fuze + "," + gps + "," + elev;
	private boolean stillThere;
	private MortarSocket mortarListener;
    
	// When instantiating a mortar, an ID is needed
	public Mortar(int newID) { ID = newID; }
	
	// All the setters and getters	
	public void setID(int newID) { this.ID = newID; }
	public int getID() { return this.ID; }
	
	public void setFuze(int newFuze) { this.fuze = newFuze; }
	public int getFuze() { return this.fuze; }
	
	public void setGps(String newGps) { this.gps = newGps; }
	public String getGps() { return this.gps; }
	
	public void setElev(String newElev) { this.elev = newElev; }
	public String getElev() { return this.elev; }
	
	public void sendSelf(String message) { this.mortarListener.sendToSocket(message); }
	
	// if encountering id < 10, add a "0" in the message
	public String makeMessage() {
		String tempStr = "";
		if (this.ID < 10) { tempStr += "0" + this.ID; }
		else tempStr += this.ID;
		return "iam "+ tempStr + ","+ fuze + "," + gps + "," + elev;
	}
  
	public void updateSelf(int newID, int newFuze, String newGps, String newElev) {		//takes info from controller, changes data on mortar
		this.setID(newID);
		this.setFuze(newFuze);
		this.setGps(newGps);
		this.setElev(newElev);
	}
	
	public void receiveData(String message){
		if (message.substring(0,2) == "iam") { receiveIAm(message); }
		else if (message.substring(1,6)==" here"){ this.stillThere = true; };
		//send message back to mortar that is ID+" acknowledge"
	};
    
	public void receiveIAm(String newMessage){	//update self based on message
		updateSelf(Integer.parseInt(newMessage.substring(2,3)),newMessage.substring(4,18),Integer.parseInt(newMessage.substring(0,1)),newMessage.substring(19,23));
		Controller.updateTablet();
	}
	
	public void startSocketListener(){
		this.mortarListener = new MortarSocket();
		mortarListener.start();
	}
}

class MortarSocket implements Runnable {
  private String clHost ="192.168.1.1";
  int clPort = 4445;
  private Socket dSock = new Socket(clHost, clPort);
  OutputStreamWriter os = new OutputStreamWriter(dSock.getOutputStream(), "UTF-8"); //For sending:: Use .write to send data over os
  BufferedReader is = new BufferedReader(new InputStreamReader(dSock.getInputStream())); //For receiving::

  public void sendToSocket(String message){
    os.write(message);
  }
  
  public void run(){
    private String message = "";
    private String read
    while((str = is.readLine()) != null); {
      message = message + read;
    }
    super.receiveData(message);
  }
}

/*
class MortarPing implements Runnable{
  pingHost = "192.168.1.1";
  int pingPort = 4446;
  pingSock = new Socket(pingHost, pingPort);
}
*/