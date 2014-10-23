package magServer;

import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

class Tablet extends Controller{
	
	int 							state = 0;
	TabletChildSocket				tabletListener;
	private final ReentrantLock		lock = new ReentrantLock();
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
		int ID = Integer.parseInt(message.substring(0,1));
		int fuze = Integer.parseInt(message.substring(2));
		String gps = message.substring(3,16);
		String elev = message.substring(17,21);
		super.updateMortar(ID, fuze, gps, elev);
	}
}
	