package magServer;

class Mortar { //need function for after the mortar leaves
	int 			fuze = 0;
	int 			ID; 
	String 			gps = "AA000000000000";
	String 			elev = "00000";
	String 			message = ("iam "+ ID +","+ fuze + "," + gps + "," + elev);
	boolean 		stillThere;
	MortarSocket 	mortarListener;
  
	public void setFuze(int ID){
		this.fuze = ID;
	}
	
	public void sendSelf(String message){
		this.mortarListener.sendToSocket(message)
	}
  
	public void updateSelf(int newFuze, String newGps, String newElev){		//takes info from controller, changes data on mortar
		fuze = newFuze;
		gps = newGps;
		elev = newElev;
		}
	public void receiveData(String message){
		if (message.substring(0,2) == "iam"){
			receiveIAm(message);
			}
		else if (message.substring(1,6)==" here"){
			this.stillThere = true; //send message back to mortar that is ID+" acknowledge"
		 	};
		};
    
	public void receiveIAm(String newMessage){	//update self based on message
		updateSelf(newMessage.substring(2,3),newMessage.substring(4,18),newMessage.substring(0,1),(19,23));
		Controller.updateTablet();
	}
	
	public void startSocketListener(){
		this.mortarListener = new MortarSocket();
		mortarListener.start();
	}
}

class MortarSocket implements Runnable {
  clHost ="192.168.1.1";
  int clPort = 4445;
  dSock = new Socket(clHost, clPort);
  OutputStreamWriter os = new OutputStreamWriter(dSock.getOutputStream(), "UTF-8"); //For sending:: Use .write to send data over os
  BufferedReader is = new BufferedReader(new InputStreamReader(dSock.getInputStream())); //For receiving::

  public void sendToSocket(String message){
    os.write(message)
  }
  
  public void run(){
    String message = "";
    while((String str = is.readLine()) != null) {
      message = message + str;
    }
    super.receiveData(message);
  }
}

class MortarPing implements Runnable{
  pingHost = "192.168.1.1";
  int pingPort = 4446;
  pingSock = new Socket(pingHost, pingPort);
}
