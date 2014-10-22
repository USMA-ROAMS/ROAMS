package magServer;

import java.util.*;

public class Magazine {
	int capacity;
	ArrayList<Tube> tubes = new ArrayList<Tube>();
	
	public Magazine(int newCap) {
		this.capacity = newCap;
		for (int i = 0; i < this.capacity; i++) {
			this.tubes.add(new Tube(i));
		}
	}
	
	public ArrayList<Tube> getTubes() { return this.tubes; }
	public void setCapacity(int cap) { this.capacity = cap; }
	public int getCapacity() { return this.capacity; }
	public Tube apply(int num) { return this.tubes.get(num); }
	
	public void rotate() {
		Tube tempTube = this.tubes.get(0);
		tubes.remove(tempTube);
		tubes.add(this.capacity - 1, tempTube);		
	}
	
	public void rotate(int spaces) {
		for (int i = 0; i < spaces; i++) {
			this.rotate();
		}
	}
	
	public void fire() { //TODO
		this.rotate();
	}
	

}