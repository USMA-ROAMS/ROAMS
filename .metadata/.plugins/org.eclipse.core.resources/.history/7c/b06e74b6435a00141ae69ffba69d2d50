package magServer;

import java.util.concurrent.locks.ReentrantLock;

class Tube extends Magazine {
	private final ReentrantLock			lock = new ReentrantLock();
	private int 						magPos = super.tubes.indexOf(this);
	Mortar 								mortar;
	
	public void acceptMortar(Mortar newMortar) {
		this.mortar = newMortar;
	}
	
	public int getPos() {
		return this.magPos;
	}
}
