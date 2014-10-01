class Magazine(int capacity) extends ArrayList {
	ArrayList[] tubes =  new ArrayList(capacity);
	public void function init(){
		for (i = 0;i<capacity;i++){
			void tubes = new Tube() +: tubes
		}
	}
	public void function advanceTubes(){
		void tubes = tubes.add(tubes(0))
		void tubes = tubes.remove(tubes(0))
		}
	public void function update(Tablet tab){
		for (i = 0; i<capacity: i++){
			tubes(i).update(tab)
			}
		}
	function apply(int num){
		Tube tubes(num)
		}
	
class Mortar(int ID){
	int fuze = 0;
	String gps = "AA000000000000"
	String elev = "00000"
	String message = ("iam "+ID+","+fuze+","+gps+","+elev)
	InetAddress clHost
	int clPot
	Socket dSock
	datagramSocket pSock
	var boolean stillThere
	public void function notify(){
	//sends message to actual, using whatever sockets it needs
	}
	public boolean function getPing(){
		//listens for ping?
		}
		
		}
	
class Tube{
	int magPos= function(){super.tubes.indexOf(this)};
	Mortar mortar = new Mortar(magPos);
	function init();
	function update(Tablet tab){
		void mortar.fuze = tab.fuze
		void mortar.dSock = tab.dSock
		void mortar.pSock = tab.pSock
		boolean true
	}

	
class Tablet{
	String state = 0
	InetAddress host
	int port
	Socket dSock
	public void function send(String message){
		//send message to tablet (using fairy magic)
		}
	public void function notify(){}
	public void receiveData(String message){
		int ID = message.substring(0,1).toInt;
		String fuze = message.substring(2);
		String gps = message.substring(3,16);
		String elev = message.substring(17,21);
		super.updateMortar(ID, fuze, gps, elev)}
	}
	

class Controller{
	Tablet tab = new Tablet
	Magazine mag = new Magazine(20);
	public void function rotateMagazine(int num){
		for(i = 0;i<num;i++)
		void mag.rotate()
		}
	public void function fire(){
		//sends fire signal
		void rotateMagazine(1)
		}
	public void function updateTablet(){
		void tablet.send(String "<list>")
		for (i=0;i<mag.capacity;i++){
			String message = mag(i).mortar.ID +","+mag(i).mortar.fuze+","mag(i).mortar.gps+","+mag(i).mortar.elev+";"
			tablet.send(message)
			}
		void tablet.send(String "</list>\n")
		}
	public void function updateMortar(Int ID, String fuze, String gps, String elev){
		for (i=0;i<mag.capacity;i++){
			if (mag(i).mortar.ID==ID)
				mag(i).mortar.updateSelf(fuze, gps, elev)
		
		
	}