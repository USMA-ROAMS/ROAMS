package magServer;

class Tablet{
	String state = 0;
	public void function send(String message){
		//send message to tablet (using fairy magic)
		}
	public void receiveData(String message){ //gets data from actual tablet, sends up to controller to update mortar object
		int ID = message.substring(0,1).toInt;
		String fuze = message.substring(2);
		String gps = message.substring(3,16);
		String elev = message.substring(17,21);
		super.updateMortar(ID, fuze, gps, elev)}
	}
	
class TabletSocket{
  InetAddress host = "192.168.0.1";
	int port = 4444;
	dSock = new Socket(clHost, clPort);
	  OutputStreamWriter os = new OutputStreamWriter(dSock.getOutputStream(), "UTF-8"); //For sending:: Use .write to send data over os
	  BufferedReader is = new BufferedReader(new InputStreamReader(dSock.getInputStream())); //For receiving::

	  public void sendToSocket(String message){
	    os.write(message)
	  }
	  
	  public void run(){
	    String message = "";
	    while((str = is.readLine()) != null) {
	      message = message + str
	    }
	    super.receiveData(message)
	  }
}
