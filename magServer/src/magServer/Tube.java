package magServer;

public class Tube {
	private int magPos;
	public Mortar mortar;
	
	public Tube(int magPos) {
		mortar = new Mortar(magPos);
		this.magPos = magPos;
	}
	
	public void setMagPos(int newPos) { this.magPos = newPos; }
	public int getMagPos() { return this.magPos; }
	public Mortar getMortar() { return this.mortar; }
}
