package magServer;

class Magazine{
	int capacity;
	Tube[] tubes =  new Tube[](capacity);
	
	public void init(){
		for (int i = 0; i<capacity; i++){
			void tubes = new Tube() +: tubes;
		};
	}
	public void rotate(){
		this.tubes = tubes.add(this.tubes[0]);
		this.tubes = tubes.remove(this.tubes[0]);
		}
	
	public void setCapacity(int cap){
		this.capacity = cap;
	}
	
	public Tube apply(int num){
		Tube tubes(num);
		}
}