package magServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
//import java.util.concurrent.locks.ReentrantLock;

class TabletChildSocket implements Runnable {
	  Socket 							dSock;
	  OutputStreamWriter 				os;
	  BufferedReader 					is;
	  //private final ReentrantLock		lock = new ReentrantLock();
	  private Tablet					tablet;
  
  public void acceptSocket(Socket newSock, Tablet newTablet) throws Exception {
	  this.dSock = newSock;
	  this.tablet = newTablet;
	  create();
  }
  
  public void create() throws Exception {
	  this.os = new OutputStreamWriter(dSock.getOutputStream(), "UTF-8"); //For sending:: Use .write to send data over os
	  this.is = new BufferedReader(new InputStreamReader(dSock.getInputStream())); //For receiving::
  }
  
  public void sendToSocket(String message){
	  try {
		  os.write(message + '\n');
	  } catch (IOException e) {System.out.println("error");}
  }
  
  public void run(){
		while(true){
			System.out.println("Tablet here!");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		    String read = "";
		    try {
				//while((read = is.readLine()) != null){
		    		read = is.readLine();
					System.out.println(read);
				    if(read.equals("closeme")){
				    	System.out.println("Tablet closed connection");
				    	break;
				    }
				    else if(read == ""){
				    	System.out.println("Empty String??");
				    }
				    //else if(read == null){}
				    else{
					    System.out.println("Received Message!");
					    System.out.println("Updating Tablet ");
					    tablet.receiveData(read);
				    }
				//}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Failed to read.");
				e.printStackTrace();
			}
		 }
		
		try {
			this.dSock.close();
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	  }
	}


