package magServer;

//import java.util.concurrent.locks.ReentrantLock;

class Tube extends Magazine {
	// private final ReentrantLock lock = new ReentrantLock();
	private int magPos = super.tubes.indexOf(this);
	Mortar mortar;
	String MID = "--";

	public Tube(int magPos) {
		// mortar = new Mortar(Integer.toString(magPos));
		this.magPos = magPos;
	}

	// Setters and getters
	public void setPos(int newPos) {
		this.magPos = newPos;
	}
	
	public void setMIDNull(){
		this.MID = "--";
	}
	
	public void setMortarNull(){
		this.mortar = null;
	}

	public int getPos() {
		return this.magPos;
	}

	public Mortar getMortar() {
		return this.mortar;
	}

	public void acceptMortar(Mortar newMortar) {
		this.mortar = newMortar;
		this.MID = this.mortar.getID();
	}
}
