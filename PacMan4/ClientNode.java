import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;


public class ClientNode extends Node{

	PacMan m_pacMan;
	GameModel m_gameModel;

	public ClientNode(PacMan m_pacMan){
		super();
		this.m_pacMan = m_pacMan;
		this.m_gameModel = m_pacMan.m_gameModel;
		this.coord = false;
	}
	
	public  void connectMultiplayerGame() {
		m_pacMan.netMultiplayer = true;
		m_pacMan.controller = false;
		m_pacMan.playerIsGhost = true;
		m_pacMan.multiplayerActive = true;
		
		try {
			//initialize the socket over which slave receive updates
		m_pacMan.updateSocket = new MulticastSocket(m_pacMan.updateListenPort);
		m_pacMan.updateSocket.joinGroup(InetAddress.getByName(m_pacMan.group));
			//initialize the socket over which slave send updates
			//TODO: MAKE THIS CHANGABLE!!! 
			//tcpSocket = new Socket(InetAddress.getLocalHost(), listenPort);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//once connections are opened, we start the game!
		m_gameModel.m_state = GameModel.STATE_NEWGAME;
		ClientWorker cw;
		  String hostIP = JOptionPane.showInputDialog(null, "Enter host's ip : ", 
				  "", 1);
		cw = new ClientWorker(m_gameModel, hostIP);
		try {
			cw.tcpSocket = new Socket(hostIP, m_pacMan.serverlistenPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    Thread t2 = new Thread(cw);
	    t2.start();	
	}
	
	
}
