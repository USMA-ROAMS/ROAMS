package magServer;

class Mortar extends Controller { //need function for after the mortar leaves
	int 			fuze = 0;
	int 			ID; 
	String 			gps = "AA000000000000";
	String 			elev = "00000";
	String 			message = ("iam "+ ID +","+ fuze + "," + gps + "," + elev);
	boolean 		stillThere;
	ChildSocket 	mortarListener = new ChildSocket();
  
	public void setFuze(int ID){
		this.fuze = ID;
	}
	
	public void sendSelf(String message){
		this.mortarListener.sendToSocket(message);
	}
	
	public String makeMessage(){
		return message = ("iam "+ ID +","+ fuze + "," + gps + "," + elev);
	}
  
	public void updateSelf(int newFuze, String newGps, int newID, String newElev){		//takes info from controller, changes data on mortar
		fuze = newFuze;
		ID = newID; 
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
		updateSelf(Integer.parseInt(newMessage.substring(2,3)),newMessage.substring(4,18),Integer.parseInt(newMessage.substring(0,1)),newMessage.substring(19,23));
		super.updateTablet();
	}
}
