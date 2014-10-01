class Magazine(int capacity) extends ArrayList {
	ArrayList[] tubes =  new ArrayList(capacity);
	public void function init(){
		for (i = 0;i<capacity;i++){
			void tubes = new Tube() +: tubes;
		};
	}
	public void function rotate(){
		void tubes = tubes.add(tubes(0));
		void tubes = tubes.remove(tubes(0));
		}
	public Tube function apply(int num){
		Tube tubes(num);
		}
	
class Mortar(int ID){ //need function for after the mortar leaves
	String fuze = "0";
	String gps = "AA000000000000";
	String elev = "00000";
	String message = ("iam "+ID+","+fuze+","+gps+","+elev);
	InetAddress clHost;
	int clPot;
	Socket dSock;
	datagramSocket pSock;
	var boolean stillThere;
	public void function sendSelf(){
	//sends message to actual, using whatever sockets it needs
	}
	public void function updateSelf(String newFuze, String newGps, String newElev){		//takes info from controller, changes data on mortar
		void fuze = newFuze;
		void gps = newGps;
		void elev = newElev;
		}
	public void function recieveData(String message){
		if message.substring(0,2) == "iam"{
			receiveIAm(message)
			};
		else if (message.substring(1,6)==" here"){
			stillHere = true //send message back to mortar that is ID+" acknowledge"
			};
		};
	public void function receiveIAm(String newMessage){	//update self based on message
		updateSelf(newMessage.substring(2,3),newMessage.substring(4,18),newMessage.substring(0,1),(19,23));
		super.super.super.updateTablet();
		}; 

class Tube{
	int magPos= function(){super.tubes.indexOf(this)};
	Mortar mortar = new Mortar(magPos);
	};

	
class Tablet{
	String state = 0;
	InetAddress host;
	int port;
	Socket dSock;
	public void function send(String message){
		//send message to tablet (using fairy magic)
		}
	public void receiveData(String message){ //gets data from actual tablet, sends up to controller to update mortar object
		int ID = message.substring(0,1).toInt;
		String fuze = message.substring(2);
		String gps = message.substring(3,16);
		String elev = message.substring(17,21);
		super.updateMortar(ID, fuze, gps, elev)}
	}
	

class Controller{
	Tablet tab = new Tablet;
	Magazine mag = new Magazine(20);
	public void function rotateMagazine(int num){
		for(i = 0;i<num;i++){
		void mag.rotate();
		void updateTablet();}
		};
	public void function fire(){
		//sends fire signal
		void rotateMagazine(1);
		}
	public void function updateTablet(){ //sends data from magazine to tablet to send
		void tablet.send(String "<list>");
		for (i=0;i<mag.capacity;i++){
			String message = (mag(i).mortar.ID +","+mag(i).mortar.fuze+","mag(i).mortar.gps+","+mag(i).mortar.elev+";");
			tablet.send(message);
			}
		void tablet.send(String "</list>\n");
		}
	public void function updateMortar(Int ID, String fuze, String gps, String elev){  //takes message from Tablet to mortar, then updates actual mortar
		for (i=0;i<mag.capacity;i++){
			if (mag(i).mortar.ID==ID){
				mag(i).mortar.updateSelf(fuze, gps, elev);
				mag(i).mortar.sendSelf();
				}
			}
		}
	}