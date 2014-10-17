package magServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class MortarSocket implements Runnable {
	
	  Socket dSock;
	  OutputStreamWriter os;
	  BufferedReader is;

  
  
  public void acceptSocket(Socket newSock) throws Exception {
	  this.dSock = newSock;
	  create();
  }
  
  public void create() throws Exception {
	  this.os = new OutputStreamWriter(dSock.getOutputStream(), "UTF-8"); //For sending:: Use .write to send data over os
	  this.is = new BufferedReader(new InputStreamReader(dSock.getInputStream())); //For receiving::
  }
  
  public void sendToSocket(String message){
	  try {
    os.write(message);
	  }
	  catch (IOException e) {System.out.println("error");}
  }
  
  public void run(){
    String message = "";
    String read;

    while((read = is.readLine()) != null); {
      message = message + read;
    }
    super.receiveData(message);
  }
}

