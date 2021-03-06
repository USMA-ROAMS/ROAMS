package magServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Roams {

	public static void main(String[] args) throws IOException {
		System.out.println("Making Controller");
		Controller controller = new Controller();
		System.out.println("Controller made");
		System.out.println("Initializing Controller");
		controller.init();
		System.out.println("Controller Initialized");
		
		Scanner reader = new Scanner(System.in);
		List<String> commands = Arrays.asList("QQ (QUIT)", "HP (HELP)", "TS (TEST)", "CF (CHANGE FUZE)", "RT (ROTATE)", "CL <RGB> (CHANGE LIGHT)","RS (RESET MAGAZINE)");
		while (true) {
			String rawCommand = reader.nextLine();
			if (rawCommand.equals("") || rawCommand == null){
			}
			else if (rawCommand.substring(0, 1).equals("/")) {
				String command = rawCommand.substring(1, rawCommand.length());
				if (command.equals("QQ")) {
					System.out.println("Operator initiated server shutdown...");
					reader.close();
					break;
				} else if (command.substring(0, 2).equals("TS")) {
					System.out.println("This is the test text!!!");
				} else if (command.substring(0, 2).equals("HP")) {
					System.out
							.println("The following commands are available for use:");
					for (String l : commands) {
						System.out.println(l);
					}
				} else if (command.substring(0, 2).equals("CF")){
					String mor = command.substring(3, 5);
					String set = command.substring(6);
						
					if (Integer.parseInt(set) < 0 || Integer.parseInt(set) > 4){
						System.out.println("Invalid Fuze setting!");
					}
					else{
						System.out.println("Sending mortar " + mor + " a fuze setting of " + set + ".");
						controller.updateMortar(mor, set, "AA000000000000", "00000");
					}
				} else if (command.substring(0,2).equals("RT")){
					controller.rotatePhysicalMagazine("0","1");
				} else if (command.substring(0,2).equals("CL")){
					controller.changeStatusLight(command.substring(3,6));
				} else if (command.substring(0,2).equals("RS")){
					controller.clearMagazine();
				}
				} 
				else {
					System.out.println(rawCommand + " is not a recognized command or server function.");
				}
			}
		}
	}
