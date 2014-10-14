package magServer;

class Controller{
	Tablet tablet = new Tablet();
	Magazine mag = new Magazine();
	public void rotateMagazine(int num){
		for(int i = 0;i<num;i++){
			mag.rotate();
		}
		
		for(Tube tube : this.mag.tubes){
			tablet.send(tube.mortar.makeMessage());
		}
	}
	
	public void init() {
		//Magazine mag = new Magazine();
		//Tablet tab = new Tablet();
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
