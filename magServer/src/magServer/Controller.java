package magServer;

class Controller{
	Tablet tablet = new Tablet();
	Magazine mag = new Magazine();
	public void rotateMagazine(int num){
		for(int i = 0;i<num;i++){
		mag.rotate();
		tablet.updateTablet();
		}
	};
	
	public void fire(){
		//sends fire signal
		rotateMagazine(1);
		}
	
	public void updateTablet(){ //sends data from magazine to tablet to send
		//Following line needs implementation
		//tablet.send(String "<list>");
		for (int i=0;i<mag.capacity;i++){
			String message = (mag.tubes[i].mortar.ID +","+mag.tubes[i].mortar.fuze+","mag(i).mortar.gps+","+mag(i).mortar.elev+";");
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
