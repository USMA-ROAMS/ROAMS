package roamsTest;
// *** Need the package for the actual code ***

import static org.junit.Assert.*;
import magServer.*;


public class Test {

	@org.junit.Test
	public void test() {
		String message = "iam 17,0,WL123456123456,12345";
		/* update the mortar
		 *		id = 17
		 *		fuze = impact
		 *		GPS = WL123456123456
		 *		elev = 12345
		 */
		String updatedInfo = "iam 17,0,WL123456123456,12345";
		assertEquals(message, updatedInfo);
		// fail("Not yet implemented");
	}
	
	public void testUpdate() {
		
//		for (i=0; i<10; i++) {
//			
//			
//			
//			Mortar mortar = new Mortar(10); // mortar id = 10
//			Tube tube = new Tube
//			Controller controller = new Controller
//		 
//		}
		
		
		
	}
	
	
	public void testFire() {}
	public void testRotate() {}

}








/* message = 'iam ' + id + ',' + str(setting) + ',' + gps + ',' + elev
 *            0123    45    6           7        8    9-22   23   24-28
 *
 *
 */

/*
message = 'close' -> close the system
message[0:3] == '111' -> 'sending GPA data to all rounds'
message[0:3] != '111' -> 'sending' message 'to all rounds'
message[0:3] == 'iam' -> 'updating the fuze'
	message[3:4] == id
	message[]

message[0:6] == mortar.id + ' here'
	message[7:8]
			 */