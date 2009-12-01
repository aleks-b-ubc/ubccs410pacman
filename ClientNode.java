import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class ClientNode extends Node{

	private PacMan m_pacMan;
	private GameModel m_gameModel;

	public ClientNode(PacMan m_pacMan){
		super();
		this.m_pacMan = m_pacMan;
		this.m_gameModel = m_pacMan.m_gameModel;
		this.coord = false;
	}
	
	public  void connectMultiplayerGame() {
		m_pacMan.netMultiplayer = true;
		//m_pacMan.controller = false;
		//m_pacMan.playerIsGhost = true;
		
		/*try {
			//initialize the socket over which slave receive updates
		//	updateSocket = new MulticastSocket(updateListenPort);
		//	updateSocket.joinGroup(InetAddress.getByName(group));
			//initialize the socket over which slave send updates
			//TODO: MAKE THIS CHANGABLE!!! 
			//tcpSocket = new Socket(InetAddress.getLocalHost(), listenPort);
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//once connections are opened, we start the game!
		m_gameModel.m_state = GameModel.STATE_NEWGAME;
		ClientWorker cw;
		cw = new ClientWorker(m_gameModel);
	    Thread t2 = new Thread(cw);
	    t2.start();	
	}
	
	
}
