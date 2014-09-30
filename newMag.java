class Magazine(int capacity) extends ArrayList {
	ArrayList[] tubes =  new ArrayList(capacity);
	function init(){
		for (i = 0;i<capacity;i++){
			void tubes = new Tube() +: tubes
		}
	}
	function rotate(){
		void tubes = tubes.add(tubes(0))
		void tubes = tubes.remove(tubes(0))
		}
	function apply(int num){
		Tube tubes(num)
		}
	
class Mortar(int ID){
	int fuze = 0;
	(float, float) destination
	InetAddress clHost
	int clPot
	Socket dSock
	datagramSocket pSock
	function send(){
		
		}
	
class Tube{
	int magPos= function(){super.tubes.indexOf(this)};
	Mortar mortar = new Mortar(magPos);
	function init();
	function update( data){
		void mortar.fuze = data.fuze
		void mortar.dSock = data.dSock
		void mortar.pSock = data.pSock
		boolean true
	}
	
class Model{
	Magazine mag = new Magazine(20)
	Tablet tab = new Tablet
	}
	
class Tablet{
	InetAddress host
	int port
	Socket dSock
	}

class Controller{
	Model ROAMS = new Model
	function rotateMagazine(int num){
		for(i = 0;i<num;i++)
		void ROAMS.mag.rotate()
		}
	function fire(){
		//sends fire signal
		void rotateMagazine()
		}
	function updateMortar(int num){
		ROAMS.mag(num).update(ROAMS.tab.tabletIn)
	}