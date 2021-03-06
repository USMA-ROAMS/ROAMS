package magServer;

import java.io.IOException;
//import java.util.concurrent.locks.ReentrantLock;

class Controller {
	Tablet 					tablet;
	magMotor 				magMotor;
	private Magazine 		mag;
	WelcomeSocket 			mortarListener;
	TabletWelcomeSocket 	tabletListener;
	public int 				ThreadCount;
	// private final ReentrantLock lock = new ReentrantLock();
	public String 			nextID = "00";
	String 					mortarAddr =  "192.168.42.1";
	String 					tabletAddr =  "192.168.42.1";
	public boolean			dataForTablet = false;
	RGBStatusLight			statusLED; 

	// Initialize all elements required for controller operation
	public void init() throws IOException {

		// Instantiate and initialize new Magazine object
		System.out.println("Intializing status indicator...");
		this.statusLED = new RGBStatusLight();
		statusLED.init();
		System.out.println("Status light initialized");
		this.statusLED.changeLight("110"); //change light to yellow
		System.out.println("Initializing Magazine Object");
		this.mag = new Magazine();
		mag.init(5); // Integer passed as parameter here dictates magazine size.
		System.out.println("Magazine Object Initialized");
		System.out.println("Intializing Magazine Motor");
		this.magMotor = new magMotor();
		magMotor.init();
		System.out.println("Magazine Motor Intitialized.");
		// Begin process to create welcome socket and bind it to desired host
		// and port
		int timeoutCounter = 0; // initialize a counter to track number of
								// attempted binds.
		while (timeoutCounter < 3) { // start trying to bind
			try {
				System.out.println("Attempting to bind welcome socket to "
						+ mortarAddr + " on port " + "4445");
				// Create and initialize new instance of WelcomeSocket
				this.mortarListener = new WelcomeSocket();
				this.mortarListener.init(mortarAddr, 4445, this);
				System.out.println("Bound successfully!");
				Thread mortarWelcome = new Thread(this.mortarListener,
						"MortarWelcomeSocket"); // serverSocket.accept()
												// function is blocking. Spawn a
												// new thread and continue
												// execution.
				mortarWelcome.start(); // Start the new thread
				this.ThreadCount++;

				// If successful Set the timeout counter to 0 for the Tablet
				// binding process
				timeoutCounter = 0;
				break;
			} catch (java.net.BindException e) {
				timeoutCounter++;
				System.out.println("Couldn't bind to " + mortarAddr);
				System.out.println("Failed " + Integer.toString(timeoutCounter) + " times.");
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		while (timeoutCounter < 3) {
			try {
				System.out.println("Attempting to bind welcome socket to "
						+ tabletAddr + " on port " + "4444");
				this.tabletListener = new TabletWelcomeSocket();
				this.tabletListener.init(tabletAddr, 4444, this);
				System.out.println("Bound successfully!");
				Thread tabletWelcome = new Thread(this.tabletListener,
						"TabletWelcomeSocket");
				tabletWelcome.start();
				break;
			} catch (java.net.BindException e) {
				timeoutCounter++;
				System.out.println("Couldn't bind to " + tabletAddr);
				System.out.println("Failed " + Integer.toString(timeoutCounter)
						+ " times.");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void rotateMagazine(String rotMessage) {
		int dist = Integer.parseInt(rotMessage.substring(2, 3));
		int dir = Integer.parseInt(rotMessage.substring(1, 2));

		if (dir == 0) {
			System.out.println("Rotating Magainze " + Integer.toString(dist) + " tubes Clockwise...");
			for (int i = 0; i < dist; i++) {
				mag.CWRotate();
			}
		} else if (dir == 1) {
			System.out.println("Rotating Magainze " + Integer.toString(dist) + " tubes Counter-Clockwise...");
			for (int i = 0; i < dist; i++) {
				mag.CCWRotate();
			}
		} else {
			System.out.println("Invalid Rotation Direction!");
		}
	}

	public Magazine getMag() {
		return this.mag;
	}
	
	public Tablet getTablet() {
		return this.tablet;
	}

	public void fire(String ID) {
		// sends fire signal
		System.out.println("Recieved signal to fire round " + ID);
		Tube tube = this.mag.getFirstTube();
		if (tube != null){
			if (tube.getMortar().getID().equals(ID)){
				tube.setMortarNull();
				tube.setMIDNull();
				this.statusLED.blinkRed();
			}
			else{System.out.println("Mortar ID requested to be fired did not match motar ID in firing tube. Abandoning Fire.");}		
		}
	}

	public void updateTablet(String newMessage) { // sends data from magazine to
												  // tablet to send
		tablet.send(newMessage);
	}

	public void closeGracefully() {
		// TODO Implement code that will kill all of the open connections and
		// running threads gracefully
		if (ThreadCount == 0)
			System.exit(0);

		// this.tabletListener.close();
		// this.ThreadCount --;
		this.mortarListener.close();
		this.ThreadCount--;

		if (ThreadCount == 0)
			System.exit(0);

		System.out.println("Sending kill command to all clients");
		for (Tube tube : this.mag.tubes) {
			tube.mortar.mortarListener.sendToSocket("killthread");
			this.ThreadCount--;
		}

		if (ThreadCount > 0) {
			System.out.println("Failed to close "
					+ Integer.toString(ThreadCount) + " threads...");
			System.exit(1);
		}

		System.out.println("Server closed successfully");
		System.exit(0);
	}

	public void incrementID() {
		int oldIntID = Integer.parseInt(this.nextID);
		int newIntID = oldIntID + 1;
		if (newIntID < 10) this.nextID = "0" + Integer.toString(newIntID);
		else this.nextID = Integer.toString(newIntID);
	}

	public void setTablet(Tablet newTablet) {
		this.tablet = newTablet;
	}

	public void startThread(Mortar mortarToStart) {
		String threadName = "mortarThread" + mortarToStart.ID;
		new Thread(mortarToStart.mortarListener, threadName).start();
	}

	public void updateMortar(String ID, String fuze, String gps, String elev) { // takes message from Tablet to mortar, then updates actual mortar
		boolean success = false;
		System.out.println("Retrieving mortar object for mortar ID: " + ID + "...");
		for (int i = 0; i < this.mag.capacity; i++) {
			Tube currTube = this.getMag().getTubes().get(i);
			System.out.println("ID: " + ID);
			System.out.println("MID: " + currTube.MID);
			if (currTube != null){
				if(currTube.MID.equals(ID)) {
					System.out.println("... Got object for Mortar ID: " + ID + ".");
					currTube.getMortar().updateSelf(ID, fuze, gps, elev);
					System.out.println("updateSelf called successfully!");
					//currTube.getMortar().sendSelf(currTube.getMortar().makeMessage() + System.getProperty("line.separator"));
					currTube.getMortar().setData(true);
					System.out.println("Sent info to mortar!");
					success = true;
					break;
				}

			}
		}
		
		if (!success) System.out.println("Couldn't retrieve mortar object for Mortar ID: " + ID + ".");
	}
	
	public void rotatePhysicalMagazine(String dir, String rots){
		this.magMotor.rotatePhysicalMagazine(dir, rots);
	}
	
	public void sendExistingMortars(){
		for (Tube tube : this.mag.tubes) {
			Mortar currMor = tube.getMortar();
			if(currMor != null) {updateTablet("5"+Integer.toString(tube.getPos())+currMor.makeMessage());}
		}
	}

	public void changeStatusLight(String light){
		if(light.equals("666")) this.statusLED.danceParty();
		else this.statusLED.changeLight(light);
	}
	
	public void clearMagazine(){
		System.out.println("Rebuilding empty magazine...");
		this.mag = new Magazine();
		mag.init(5);
		System.out.println("Magazine is now empty.");
	}
}