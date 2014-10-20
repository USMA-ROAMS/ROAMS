package magServer;

public class Tube {
	private int magPos;
	public Mortar mortar;
	
	public Tube(int magPos) { mortar = new Mortar(magPos); }
	
	public void setMagPos(int newPos) { this.magPos = newPos; }
	public int getMagPos() { return this.magPos; }
}
