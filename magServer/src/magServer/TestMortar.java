package magServer;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestMortar {
	// Test the mortar that has an ID of 7
	Mortar mortar = new Mortar(7);
	
	// Check if default settings are correct
	// Also, check if the getter functions are working
	@Test
	public void defaultTest() {
		assertEquals(mortar.getID(), 7);
		assertEquals(mortar.getFuze(), 0);
		assertEquals(mortar.getGps(), "AA000000000000");
		assertEquals(mortar.getElev(), "00000");
	}
	
	@Test	// Check if ID setting method is functional
	public void idTest() {
		mortar.setID(19);
		assertEquals(19, mortar.getID());
	}
	
	@Test	// Check if fuze setting method is functional
	public void fuzeTest() {
		mortar.setFuze(2);
		assertEquals(mortar.getFuze(), 2);
	}
	
	@Test	// Check if GPS setting method is functional
	public void gpsTest() {
		mortar.setGps("WL123412341234");
		assertEquals(mortar.getGps(), "WL123412341234");
	}
	
	@Test	// Check if elevation setting method is functional
	public void elevTest() {
		mortar.setElev("83019");
		assertEquals(mortar.getElev(), "83019");
	}
	
	// Check if update method is functional
	// Also, check if returned message is correct
	@Test
	public void msgTest() {
		mortar.updateSelf(2, 1, "DC567856785678", "98765");
		assertEquals(mortar.makeMessage(),
					 "iam 02,1,DC567856785678,98765");
	}
}
