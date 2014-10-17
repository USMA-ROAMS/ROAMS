package magServer;

import java.util.*;

class Magazine{
	public int capacity;
	ArrayList<Tube> tubes =  new ArrayList<Tube>();
	
	public void init(){
		for (int i = 0; i<capacity; i++){
			tubes.add(new Tube());
		};
	}
	
	public int getCapacity(){
		return this.capacity;
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