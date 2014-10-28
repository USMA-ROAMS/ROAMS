package magServer;

import static org.junit.Assert.*;

//import java.io.BufferedReader;

import org.junit.Test;

public class TestMortar {
	// Test the mortar that has an ID of 7
	public Mortar makeMortar(){
		Mortar mortar = new Mortar("07");
		try {
			mortar.init(null, null, "07");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mortar;}
	Mortar mortar = makeMortar();
	// Check if default settings are correct
	// Also, check if the getter functions are working
	@Test
	public void defaultTest() {
		assertEquals(mortar.getID(), "07");
		assertEquals(mortar.getFuze(), "0");
		assertEquals(mortar.getGps(), "AA000000000000");
		assertEquals(mortar.getElev(), "00000");
	}
	
	@Test	// Check if ID setting method is functional
	public void idTest() {
		mortar.setID("19");
		assertEquals("19", mortar.getID());
	}
	@Test
	public void idTestBorder(){
		mortar.setID("0");
		assertEquals("0", mortar.getID());
	}
	@Test 
	public void idTestFail(){
		mortar.setID("-40");
		assertEquals(mortar.getID(), "-1");
	}
	
	@Test	// Check if fuze setting method is functional
	public void fuzeTest() {
		mortar.setFuze("2");
		assertEquals(mortar.getFuze(), "2");
	}
	@Test
	public void fuzeTestBorder() {
		mortar.setFuze("6");
		assertEquals(mortar.getFuze(), "-1");
	}
	@Test 
	public void fuzeTestFail(){
		mortar.setFuze("18");
		assertEquals(mortar.getFuze(),"-1");
	}
	
	@Test	// Check if GPS setting method is functional
	public void gpsTest() {
		mortar.setGps("WL123412341234");
		assertEquals(mortar.getGps(), "WL123412341234");
	}
	@Test
	public void gpsFail(){
		mortar.setGps("Hello!");
		assertEquals(mortar.getGps(), "AA000000000000");
	}
	
	@Test	// Check if elevation setting method is functional
	public void elevTest() {
		mortar.setElev("83019");
		assertEquals(mortar.getElev(), "83019");
	}
	@Test //test is stillThere setting method is functional
	public void thereTest() {
		mortar.setHere(true);
		assertEquals(mortar.getHere(),true);
	}
	
	// Check if update method is functional
	// Also, check if returned message is correct
	@Test
	public void msgTest() {
		mortar.updateSelf("02", "1", "DC567856785678", "98765");
		assertEquals(mortar.makeMessage(),
					 "iam 02,1,DC567856785678,98765");
	}
	@Test
	public void msgTestFail() {
		mortar.updateSelf("02", "15", "DC567856785678", "98765");
		assertEquals(mortar.makeMessage(),
					 "Failure");
	}
	@Test
	public void dataTest(){
		mortar.setHere(false);
		mortar.receiveData("7 here");
		assertEquals(mortar.getHere(),true);
	}
}
