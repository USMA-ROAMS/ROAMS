package magServer;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestTube {
	Tube tube = new Tube(4);

	@Test
	public void insideMortarTest() {
		Mortar inMortar = tube.getMortar();
		assertEquals(inMortar.makeMessage(),
					 "iam 04,0,AA000000000000,00000");
	}
	@Test
	public void changePosTest() {
		int temp = tube.getMagPos();
		tube.setMagPos(temp + 3);
		assertEquals(7,tube.getMagPos());
	}
}
