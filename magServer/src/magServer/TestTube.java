package magServer;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTube {
	public Mortar makeMortar() {
		Mortar mortar = new Mortar("04");
		try {
			mortar.init(null, null, "04");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mortar;
	}

	Mortar mortar = makeMortar();

	public Tube makeTube() {
		Tube tube = new Tube(4);
		tube.acceptMortar(mortar);
		return tube;
	}

	Tube tube = makeTube();

	@Test
	public void insideMortarTest() {
		Mortar inMortar = tube.getMortar();
		assertEquals(inMortar.makeMessage(), "iam 04,0,AA000000000000,00000");
	}

	@Test
	public void changePosTest() {
		int temp = tube.getPos();
		tube.setPos(temp + 3);
		assertEquals(7, tube.getPos());
	}
}
