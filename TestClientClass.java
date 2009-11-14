import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;



public class TestClientClass {

	JFrame f = new JFrame("Receiver Client");
	JTextArea text = new JTextArea();
	JPanel mainPanel = new JPanel();
	
	
	public TestClientClass() {

	    //Frame
	    f.addWindowListener(new WindowAdapter() {
	       public void windowClosing(WindowEvent e) {
		 System.exit(0);
	       }
	    });
   
	    //frame layout
	    mainPanel.setLayout(null);
	    mainPanel.add(text);

	    f.getContentPane().add(mainPanel, BorderLayout.CENTER);
	    f.setSize(new Dimension(390,370));
	    f.setVisible(true);

	  }
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		//TestClientClass theClient = new TestClientClass();
		int listenPort = 4444;
		//int sendPort = 5555;
		
		byte[] buffer = new byte[1024];

		//This is here to show that communication is working!
		PacmanDataPacket received;	
		PacmanDataPacket toSend = new PacmanDataPacket(11);
		
		//Scanner kbd = new Scanner(System.in);
		//System.out.println("To send press 1: ");
		//int choice = kbd.nextInt();
		
			
		
		//THIS HAS WORKING CODE TO SEND THE PACKMANPACKET!
		/**
		if(choice == 1){
			DatagramSocket sendSocket = new DatagramSocket(sendPort);
			
			ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outBuffer);
			out.writeObject(toSend);
			out.close();
			
			
			DatagramPacket packet = new DatagramPacket(outBuffer.toByteArray(), outBuffer.toByteArray().length,
					InetAddress.getLocalHost(), listenPort);
			sendSocket.send(packet);
			
			
		}
		else{
			*/
		DatagramSocket listenSocket = new DatagramSocket(listenPort);
		//WORKING CODE FOR RECEIVING PACKMANPACKET
		System.out.println("Ready to receive.");
		while(true){

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			listenSocket.receive(packet);
			
			ByteArrayInputStream inBuffer = new ByteArrayInputStream(packet.getData());
			ObjectInputStream in = new ObjectInputStream(inBuffer);
			received = (PacmanDataPacket) in.readObject();
			
			System.out.println(received.toString());
		}
	
	}

}
