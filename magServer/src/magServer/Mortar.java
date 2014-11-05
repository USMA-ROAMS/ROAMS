package magServer;

import java.net.Socket;
//import java.util.concurrent.locks.ReentrantLock;

class Mortar { //need function for after the mortar leaves
	String								fuze = "0";
	String 								ID;
	String 								gps = "AA000000000000";
	String 								elev = "00000";
	String 								message = "iam "+ ID +","+ fuze + "," + gps + "," + elev;
	boolean 							stillThere;
	ChildSocket 						mortarListener = new ChildSocket();
	//private final ReentrantLock			lock = new ReentrantLock();
	Controller 							cont;

	// When instantiating a mortar, an ID is needed
	public Mortar(String newID) { ID = newID; }
	
	public void init(Controller newCont, Socket clientSocket, String ID){
		mortarListener.setMortar(this);
		try {
			mortarListener.acceptSocket(clientSocket);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.cont = newCont;
		this.ID = ID;
		this.cont.incrementID();
	}
	
	// All the setters and getters of fuze, ID, GPS, elevation

	public void setID(String newID) { 
		try{
			if (Integer.parseInt(newID) >= 0) this.ID = newID;
			else {throw new  IllegalArgumentException("Can't be less than 0");};
		} catch(IllegalArgumentException x) { this.ID = "-1";}
	}
	
	public String getID() { return this.ID; }
	
	public void setFuze(String newFuze) { 
		try {
			if(Integer.parseInt(newFuze)>5) {throw new IllegalArgumentException("No such fuze");}
			else this.fuze = newFuze;
		} catch(IllegalArgumentException x){this.fuze = "-1";}
	}

	public void setGps(String newGps) { 
		try {
			if(newGps.length() != 14) {throw new IllegalArgumentException("Not the correct format");}
			else {this.gps = newGps;}
		} catch(IllegalArgumentException x){/*this.gps = this.gps;*/}
	}
	
	public String getGps() { return this.gps; }
	
	public void setElev(String newElev) { this.elev = newElev; }

	public String getElev() { return this.elev; }
	
	public String getFuze() { return this.fuze; }
	
	public String makeMessage() {
		if (ID == "-1"){ return "Failure";}
		else if(fuze == "-1"){ return "Failure";}
		else{ return "iam "+ ID + ","+ fuze + "," + gps + "," + elev; }  
	}
  
	public void setHere(boolean here) { this.stillThere = here; }
	
	public boolean getHere() { return this.stillThere; }
	
	public ChildSocket getMortarListener() { return this.mortarListener; }
	
	public void setController(Controller newCont) {	this.cont = newCont; }
	
	public void sendSelf(String message) { this.mortarListener.sendToSocket(message); }
	
	// Takes info from controller, changes data on mortar
	public void updateSelf(String newID, String newFuze, String newGps, String newElev) {		
		this.setID(newID);
		this.setFuze(newFuze);
		this.setGps(newGps);
		this.setElev(newElev);
	}
	
	//send message back to mortar that is ID +" acknowledge"
	public void receiveData(String message) {
		System.out.println(message);
		if (message.substring(0,2) == "iam") { 
			receiveIAm(message);
			this.mortarListener.sendToSocket("ack");
		}

		else{ 
			this.setHere(true);
			this.mortarListener.sendToSocket("ack"+ID);
		}
		//send message back to mortar that is ID+" acknowledge"
	};
    
	public void receiveIAm(String newMessage){	// update self based on message
		updateSelf(newMessage.substring(0,1), newMessage.substring(2,3), newMessage.substring(4,18), newMessage.substring(19,23));
		cont.updateTablet(newMessage);
	}
}