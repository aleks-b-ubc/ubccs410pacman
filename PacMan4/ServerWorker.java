import java.io.*;
import java.net.*;

class ServerWorker implements Runnable {
  private Socket client;
  ServerNode serverNode;
  private int ghostID;

  
  ServerWorker(Socket client, ServerNode serverNode, int ghostID) {
	  //System.out.println(client.getInetAddress().getHostAddress());
   this.client = client;
   this.serverNode = serverNode;
   this.ghostID = ghostID;
  }

  public void run(){
    byte b;
    BufferedReader in = null;
    try{
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    } catch (IOException e) {
      System.out.println("in or out failed");
      System.exit(-1);
    }

    while(true){  
      try{
    	  //client.setKeepAlive(true);
			client.setSoTimeout(1000);
    	  if (in.ready()){
    		  b = (byte)in.read();
    		  System.out.println("RECEIVING: " + b);
    		  serverNode.ghostNewDirection[ghostID]=b;   
    		  Thread.sleep(1000/35);
    	  }
    	  
      	} catch (SocketException e1) {
			// TODO Auto-generated catch block
			System.out.println("TCP: client is not alive");
      	} catch (IOException e) {
      		System.out.println("Read failed");
      		System.exit(-1);
      	} catch (InterruptedException e) {
      		// TODO Auto-generated catch block
      		e.printStackTrace();
      	} 
    
    }
  }
}