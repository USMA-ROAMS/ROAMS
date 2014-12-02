package magServer;

import java.net.Socket;
//import java.util.concurrent.locks.ReentrantLock;

class Tablet {
	
	int 							state = 0;
	TabletChildSocket				tabletListener;
	//private final ReentrantLock		lock = new ReentrantLock();
	private Controller 				cont;
	
	public void init(Controller newCont, Socket tabletSocket) throws Exception{
		tabletListener = new TabletChildSocket();
		tabletListener.acceptSocket(tabletSocket, this);
		this.cont = newCont;
		this.cont.setTablet(this);
	}
	
	public void send(String message){
		this.tabletListener.sendToSocket(message);
	}
	
	public void receiveData(String message){ //gets data from actual tablet, sends up to controller to update mortar object
		if(message.substring(0,1).equals(1)){
			String ID = message.substring(1,3);
			String fuze = message.substring(3,4);
			String gps = message.substring(4,18);
			String elev = message.substring(18,23);
			cont.updateMortar(ID, fuze, gps, elev);
		}
		else System.out.println("Don't know what to do with this message");
	}
}
	
