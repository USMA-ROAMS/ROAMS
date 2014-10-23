package magServer;

import java.util.concurrent.locks.ReentrantLock;

class Tube extends Magazine {
	private final ReentrantLock			lock = new ReentrantLock();
	private int 						magPos = super.tubes.indexOf(this);
	Mortar 								mortar;
  
  public Tube(int magPos) {
    mortar = new Mortar(magPos);
    this.magPos = magPos;
	}
	
	// Setters and getters
	public void setMagPos(int newPos) { this.magPos = newPos; }
	public int getMagPos() { return this.magPos; }
	public Mortar getMortar() { return this.mortar; }  
  
	public void acceptMortar(Mortar newMortar) {
		this.mortar = newMortar;
	}
	
	public int getPos() {
		return this.magPos;
	}
}
