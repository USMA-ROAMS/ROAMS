package magServer;

import java.io.IOException;
//import java.util.concurrent.locks.ReentrantLock;

class Controller {
	Tablet 							tablet; 
	private Magazine 				mag;
	WelcomeSocket 					mortarListener;
	TabletWelcomeSocket 			tabletListener;
	public int 						ThreadCount;
	//private final ReentrantLock		lock = new ReentrantLock();
	public String					nextID = "00";
	String 							mortarAddr = "127.0.0.1";
	String 							tabletAddr = "127.0.0.1";
	
	//Initialize all elements required for controller operation
	public void init() throws IOException {
		
		//Instantiate and initialize new Magazine object
		System.out.println("Initializing Magazine Object");
		this.mag = new Magazine();
		mag.init(20); //Integer passed as parameter here dictates magazine size.
		System.out.println("Magazine Object Initialized");
		
		//Begin process to create welcome socket and bind it to desired host and port
		int timeoutCounter = 0; //initialize a counter to track number of attempted binds.
		while (timeoutCounter < 3){ //start trying to bind
			try {
				System.out.println("Attempting to bind welcome socket to " + mortarAddr + " on port " + "4445");
				//Create and initialize new instance of WelcomeSocket
				this.mortarListener = new WelcomeSocket();
				this.mortarListener.init(mortarAddr, 4445, this);
				System.out.println("Bound successfully!");
				Thread mortarWelcome = new Thread(this.mortarListener, "MortarWelcomeSocket"); //serverSocket.accept() function is blocking. Spawn a new thread and continue execution.
				mortarWelcome.start(); //Start the new thread
				this.ThreadCount++;
				
				//If successful Set the timeout counter to 0 for the Tablet binding process
				timeoutCounter = 0;
				break; 
			} catch (java.net.BindException e) {
				timeoutCounter ++;
				System.out.println("Couldn't bind to " + mortarAddr);
				System.out.println("Failed " + Integer.toString(timeoutCounter) + " times.");
				
				//TODO Figure out why Sleep statement requires Interrupted Exception blocks
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		if (timeoutCounter >= 3) {
			//TODO Implement code to kill the server neatly if the attempted bind fails 3 times
		}
		
		while (timeoutCounter < 3){
			try {
				System.out.println("Attempting to bind welcome socket to " + tabletAddr + " on port " + "4444");
				this.tabletListener = new TabletWelcomeSocket();
				this.tabletListener.init(tabletAddr, 4446, this);
				System.out.println("Bound successfully!");
				Thread tabletWelcome = new Thread(this.tabletListener, "TabletWelcomeSocket");
				tabletWelcome.start();
				break;
			} catch (java.net.BindException e) {
				timeoutCounter ++;
				System.out.println("Couldn't bind to " + tabletAddr);
				System.out.println("Failed " + Integer.toString(timeoutCounter) + " times.");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		if (timeoutCounter >= 3) {
			//TODO Implement code to kill the server neatly if the attempted bind times out 3 times
		}
		
	}
	
	public void rotateMagazine(int num){
		for(int i = 0;i<num;i++){
			mag.rotate();
		}
		
//		for(Tube tube : this.mag.tubes){
//			tablet.send(tube.mortar.makeMessage());
//		}
	}
	
	public Magazine getMag(){
		return this.mag;
	}
	
	public void fire(){
		//sends fire signal
		rotateMagazine(1);
		}
	
	public void updateTablet(String newMessage){ //sends data from magazine to tablet to send
		//TODO Following line needs implementation
		tablet.send(newMessage);
	}
	
	
	public void closeGracefully(){
		//TODO Implement code that will kill all of the open connections and running threads gracefully
		if(ThreadCount == 0) System.exit(0);
		
		//this.tabletListener.close();
		//this.ThreadCount --;
		this.mortarListener.close();
		this.ThreadCount --;
		
		if(ThreadCount == 0) System.exit(0);
		
		System.out.println("Sending kill command to all clients");
		for(Tube tube : this.mag.tubes){
			tube.mortar.mortarListener.sendToSocket("killthread");
			this.ThreadCount --;
		}
		
		if(ThreadCount > 0){
			System.out.println("Failed to close " + Integer.toString(ThreadCount) + " threads...");
			System.exit(1);
		}
		
		System.out.println("Server closed successfully");
		System.exit(0);
	}
	
	public void incrementID(){
		int oldIntID = Integer.parseInt(this.nextID);
		int newIntID = oldIntID + 1;
		this.nextID = Integer.toString(newIntID);
	}
	
	public void setTablet(Tablet newTablet){
		this.tablet = newTablet;
	}
	
	public void startThread(Mortar mortarToStart){
		String threadName = "mortarThread" + mortarToStart.ID;
		new Thread(mortarToStart.mortarListener, threadName).start();
	}
	
	public void updateMortar(String ID, String fuze, String gps, String elev){  //takes message from Tablet to mortar, then updates actual mortar
		//TODO This updates all mortars with one set of info; Incorrect, needs to change.
		for (int i=0;i<mag.capacity;i++){
			if (mag.tubes.get(i).mortar.ID == ID) {
				mag.tubes.get(i).mortar.updateSelf(ID, fuze, gps, elev);
				mag.tubes.get(i).mortar.sendSelf(mag.tubes.get(i).mortar.message);
			}
		}
	}
}
