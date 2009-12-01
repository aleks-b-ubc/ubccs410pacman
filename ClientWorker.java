import java.io.*;
import java.net.*;


public class ClientWorker implements Runnable{
	Socket tcpSocket;
	GameModel m_gameModel;
	
	public ClientWorker(GameModel m_gameModel){
		this.m_gameModel = m_gameModel;
	}
	
	
	
	public void run() {
		try{
			byte lastDirection = -1;
			byte newDirection;
		tcpSocket = new Socket("localhost", 4444);
		   PrintWriter out = new PrintWriter(tcpSocket.getOutputStream(), true);
		   while (true)
		   {
			   Thread.sleep(1000/35);
			   synchronized(m_gameModel){newDirection = m_gameModel.m_player.m_requestedDirection;}
			   if (lastDirection != newDirection){
				   lastDirection = newDirection;
			   		System.out.println("sending: "+newDirection);
				    out.write(newDirection);
				    out.flush();  
			   }
		   }
	 
		}catch(Exception e) {
	         System.out.print("TCPClient:Whoops! It didn't work!\n");
	      }
	
	}
}