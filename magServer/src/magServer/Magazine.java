package magServer;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class Magazine{
	public int 							capacity;
	public ArrayList<Tube> 				tubes = new ArrayList<Tube>();
	private final ReentrantLock			lock = new ReentrantLock();
	
	public void init(int cap){
		this.capacity = cap;
		for (int i = 0; i<capacity; i++){
			tubes.add(new Tube());
			System.out.println(Integer.toString(i+1) + " tubes initialized");
		}
	}
	
	public int getCapacity(){
		return this.capacity;
	}
	
	public Tube getLastTube(){
		return this.tubes.get(capacity - 1);
	}
	
	public void rotate(){
		tubes.add(this.capacity-1,this.tubes.get(0));
		tubes.remove(this.tubes.get(0));
		}
	
	public void setCapacity(int cap){
		this.capacity = cap;
	}
	
	public Tube apply(int num){
		return this.tubes.get(num);
	}
}