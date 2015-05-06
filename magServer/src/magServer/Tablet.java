package magServer;

import java.net.Socket;

//import java.util.concurrent.locks.ReentrantLock;

class Tablet {

	int state = 0;
	TabletChildSocket tabletListener;
	// private final ReentrantLock lock = new ReentrantLock();
	private Controller cont;

	public void init(Controller newCont, Socket tabletSocket) throws Exception {
		tabletListener = new TabletChildSocket();
		tabletListener.acceptSocket(tabletSocket, this);
		this.cont = newCont;
		this.cont.setTablet(this);
		/*if(this.cont.dataForTablet = true) {
			System.out.println("Mortars already exists... sending them to tablet");
			this.cont.sendExistingMortars();
			System.out.println("Mortars sent to tablet");
		}*/
	}

	public void send(String message) {
		this.tabletListener.sendToSocket(message);
	}
	
	public TabletChildSocket getListener() {
		return this.tabletListener;
	}

	public void receiveData(String message) { // gets data from actual tablet,
												// sends up to controller to
												// update mortar object
		if (message.substring(0, 1).equals("1")) {
			System.out.println(message);
			String ID = message.substring(1, 3);
			String fuze = message.substring(3, 4);
			String gps = message.substring(4, 18);
			String elev = message.substring(18, 22);
			cont.updateMortar(ID, fuze, gps, elev);
		}
		else if (message.substring(0, 1).equals("3")) {
			cont.rotateMagazine(message);
			cont.rotatePhysicalMagazine(message.substring(1,2), message.substring(2,3));
		} else if (message.substring(0,1).equals("!")){
		} else if (message.substring(0,1).equals("4")){
			System.out.println(message);
			cont.fire(message.substring(1,3));
		} else
			System.out.println("Don't know what to do with this message");
	}
}
