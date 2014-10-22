package magServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Mortar {

	private int fuze = 0;						// Fuze type: NEED TO BE INCLUDED
	private int ID;								// ID Form: 2 digits
	private String gps = "AA000000000000";		// GPS Form: 2 letters and 12 digits
	private String elev = "00000";				// Elevation Form: 5 digits
	private boolean stillThere;					// Whether the mortar is fired or not
    
	// When instantiating a mortar, an ID is needed
	public Mortar(int newID) { ID = newID; }
	
	// All the setters and getters of fuze, ID, GPS, elevation
	public void setID(int newID) { if (newID >= 0) this.ID = newID;
									else this.ID = -1;}
	public int getID() { return this.ID; }
	
	public void setFuze(int newFuze) { if (newFuze < 5) this.fuze = newFuze;
										else this.fuze = -1;}
	public int getFuze() { return this.fuze; }
	
	public void setGps(String newGps) { this.gps = newGps; }
	public String getGps() { return this.gps; }
	
	public void setElev(String newElev) { this.elev = newElev; }
	public String getElev() { return this.elev; }
	
	public void setHere(boolean here) { this.stillThere = here; }
	public boolean getHere() { return this.stillThere; }
	
	
	//public void sendSelf(String message) { this.mortarListener.sendToSocket(message); }
	
	// if encountering id < 10, add a "0" in the message
	public String makeMessage() {
		if (fuze == -1){ return "Failure";}
		else if (ID == -1){ return "Failure";};
		String tempStr = "";
		if (this.ID < 10) { tempStr += "0" + this.ID; }
		else tempStr += this.ID;
		return "iam "+ tempStr + ","+ fuze + "," + gps + "," + elev;
	}
	
	// Takes info from controller, changes data on mortar
	public void updateSelf(int newID, int newFuze, String newGps, String newElev) {		
		this.setID(newID);
		this.setFuze(newFuze);
		this.setGps(newGps);
		this.setElev(newElev);
	}
	
	public void receiveData(String message){
		if (message.substring(0,2) == "iam") { receiveIAm(message); }
		else{ this.setHere(true); };
		//send message back to mortar that is ID+" acknowledge"
	};
    
	public void receiveIAm(String newMessage){	//update self based on message
		//updateSelf(Integer.parseInt(newMessage.substring(2,3)),newMessage.substring(4,18),Integer.parseInt(newMessage.substring(0,1)),newMessage.substring(19,23));
		//Controller.updateTablet();
	}
	
	public void startSocketListener(){
		//this.mortarListener = new MortarSocket();
		//mortarListener.start();
	}
}
/*
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
*/

/*
class MortarPing implements Runnable{
  pingHost = "192.168.1.1";
  int pingPort = 4446;
  pingSock = new Socket(pingHost, pingPort);
}
*/