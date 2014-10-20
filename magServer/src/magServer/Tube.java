package magServer;

public class Tube {
	private int magPos;		// The tube's position in the magazine
	private Mortar mortar;	// The mortar inside this tube
	
	public Tube(int magPos) {
		mortar = new Mortar(magPos);
		this.magPos = magPos;
	}
	
	// Setters and getters
	public void setMagPos(int newPos) { this.magPos = newPos; }
	public int getMagPos() { return this.magPos; }
	public Mortar getMortar() { return this.mortar; }
}
