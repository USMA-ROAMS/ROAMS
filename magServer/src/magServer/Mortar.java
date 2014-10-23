package magServer;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.ReentrantLock;

class Mortar { //need function for after the mortar leaves
	int 								fuze = 0;
	int 								ID;
	String 								strID;
	String 								gps = "AA000000000000";
	String 								elev = "00000";
	String 								message = ("iam "+ Integer.toString(ID) +","+ Integer.toString(fuze) + "," + gps + "," + elev);
	boolean 							stillThere;
	ChildSocket 						mortarListener = new ChildSocket();
	private final ReentrantLock			lock = new ReentrantLock();
	Controller 							cont;

	// When instantiating a mortar, an ID is needed
	public Mortar(int newID) { ID = newID; }
	
	// All the setters and getters of fuze, ID, GPS, elevation
	public void setID(int newID) { this.ID = newID; }
	public int getID() { return this.ID; }
	
	public void setFuze(int newFuze) { this.fuze = newFuze; }
	public int getFuze() { return this.fuze; }
	
	public void setGps(String newGps) { this.gps = newGps; }
	public String getGps() { return this.gps; }
	
	public void setElev(String newElev) { this.elev = newElev; }
	public String getElev() { return this.elev; }
	
	//public void sendSelf(String message) { this.mortarListener.sendToSocket(message); }
	
	// if encountering id < 10, add a "0" in the message
	public String makeMessage() {
		String tempStr = "";
		if (this.ID < 10) { tempStr += "0" + this.ID; }
		else tempStr += this.ID;
		return "iam "+ tempStr + ","+ fuze + "," + gps + "," + elev;
  }  
  
	public void init(Controller newCont, Socket clientSocket, String ID) throws Exception {
		mortarListener.setMortar(this);
		mortarListener.acceptSocket(clientSocket);
		this.cont = newCont;
		this.strID = ID;
		this.cont.incrementID();
	}
	
	public ChildSocket getMortarListener(){
		return this.mortarListener;
	}
	
	public void setFuze(int ID){
		this.fuze = ID;
	}
	
	public void setController(Controller newCont){
		this.cont = newCont;
	}
	
	public void sendSelf(String message){
		this.mortarListener.sendToSocket(message);
	}
	
	// Takes info from controller, changes data on mortar
	public void updateSelf(int newID, int newFuze, String newGps, String newElev) {		
		this.setID(newID);
		this.setFuze(newFuze);
		this.setGps(newGps);
		this.setElev(newElev);
	}

	public void updateSelf(int newFuze, String newGps, int newID, String newElev){		//takes info from controller, changes data on mortar
		fuze = newFuze;
		ID = newID; 
		gps = newGps;
		elev = newElev;
		}
	
	public void receiveData(String message){
		System.out.println(message);
		if (message.substring(0,2) == "iam"){
			receiveIAm(message);
			}
		else if (message.substring(1,6)==" here"){
			this.stillThere = true; //send message back to mortar that is ID+" acknowledge"
		 	};
		};
    
	public void receiveIAm(String newMessage){	//update self based on message
		updateSelf(Integer.parseInt(newMessage.substring(2,3)),newMessage.substring(4,18),Integer.parseInt(newMessage.substring(0,1)),newMessage.substring(19,23));
		cont.updateTablet();
	}
}
