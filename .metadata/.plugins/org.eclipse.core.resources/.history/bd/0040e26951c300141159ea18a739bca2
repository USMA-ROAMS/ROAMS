package magServer;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestMagazine {
	public Mortar makeMortar(String str) {
		Mortar mortar = new Mortar(str);
		try {
			mortar.init(null, null, str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mortar;
	}

	public Magazine makeMagazine() {
		Magazine magazine = new Magazine();
		magazine.init(20);
		for (int i = 0; i < magazine.capacity; i++) {
			if (i < 10) {
				magazine.tubes.get(i).acceptMortar(
						makeMortar("0" + Integer.toString(i)));
			} else {
				magazine.tubes.get(i).acceptMortar(
						makeMortar(Integer.toString(i)));
			}
		}
		return magazine;
	}

	Magazine magazine = makeMagazine();

	public void testFire() {
	} // TODO

	@Test
	public void testTubes() {
		Magazine magazine = new Magazine();
		magazine.init(20);

		assertEquals(20, magazine.tubes.size());
	}

	@Test
	public void testEmptyTube() {
		Magazine magazine = new Magazine();
		magazine.init(20);

		assertEquals(null, magazine.apply(7).getMortar());
	}

	@Test
	public void testRotate1() { // test rotate 1 tube
		Magazine magazine = new Magazine();
		magazine.init(20);

		ArrayList<Tube> expectedTubes = magazine.getTubes();
		Tube firstTube = expectedTubes.get(0);
		expectedTubes.remove(0);
		expectedTubes.add(firstTube);

		// actual result
		// magazine.rotate();

		// compare expected and actual results
		assertEquals(expectedTubes, magazine.getTubes());
	}

	@Test
	public void rotateTest() { // mortars change position in magazine on rotate
		// magazine.rotate();
		assertEquals(magazine.apply(0).getMortar().getID(), "01");
		assertEquals(magazine.apply(19).getMortar().getID(), "00");
	}

	@Test
	public void testRotate7() { // test rotate 7 tubes
		Magazine magazine = new Magazine();
		magazine.init(20);

		ArrayList<Tube> expectedTubes = magazine.getTubes();
		for (int i = 0; i < 7; i++) {
			Tube firstTube = expectedTubes.get(0);
			expectedTubes.remove(0);
			expectedTubes.add(firstTube);
		}

		// actual result
		// for (int i = 0; i < 7; i++) { magazine.rotate(); }

		// compare expected and actual results
		assertEquals(expectedTubes, magazine.getTubes());
	}
}
