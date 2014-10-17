package magServer;

import java.io.IOException;

class Controller {
	Tablet 				tablet;
	Magazine 			mag;
	WelcomeSocket 		mortarListener;
	WelcomeSocket 		tabletListener;
	
	
	public void rotateMagazine(int num){
		for(int i = 0;i<num;i++){
			mag.rotate();
		}
		
		for(Tube tube : this.mag.tubes){
			tablet.send(tube.mortar.makeMessage());
		}
	}
	
	public void init() throws IOException {
		this.tablet = new Tablet();
		this.mag = new Magazine();
		
		String mortarAddr = "192.168.1.1";
		String tabletAddr = "192.168.0.1";
		
		this.mortarListener = new WelcomeSocket();
		this.mortarListener.init(mortarAddr, 4445);
		
		this.tabletListener = new WelcomeSocket();
		this.tabletListener.init(tabletAddr, 4444);
		
	}
	
	public void fire(){
		//sends fire signal
		rotateMagazine(1);
		}
	
	public void updateTablet(){ //sends data from magazine to tablet to send
		//Following line needs implementation
		//tablet.send(String "<list>");
		for(Tube tube : this.mag.tubes){
			tablet.send(tube.mortar.makeMessage());
			tablet.send("</list>\n");
		}
	}
	
	public void updateMortar(int ID, int fuze, String gps, String elev){  //takes message from Tablet to mortar, then updates actual mortar
		for (int i=0;i<mag.capacity;i++){
			if (mag.tubes.get(i).mortar.ID==ID){
				mag.tubes.get(i).mortar.updateSelf(fuze, gps, ID, elev);
				mag.tubes.get(i).mortar.sendSelf(mag.tubes.get(i).mortar.message);
			}
		}
	}
	
	
}
