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
	}

	public void send(String message) {
		this.tabletListener.sendToSocket(message);
	}

	public void receiveData(String message) { // gets data from actual tablet,
												// sends up to controller to
												// update mortar object
		System.out.println(message);
		if (message.substring(0, 1).equals("1")) {
			String ID = message.substring(1, 2);
			String fuze = message.substring(2, 3);
			String gps = message.substring(3, 17);
			String elev = message.substring(17, 21);
			cont.updateMortar(ID, fuze, gps, elev);
		}
		if (message.substring(0, 1).equals("3")) {
			cont.rotateMagazine(message);
		} else
			System.out.println("Don't know what to do with this message");
	}
}
