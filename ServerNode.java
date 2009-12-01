import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.SocketAddress;

public class ServerNode extends Node {
	public static int LISTENPORT = 4444;
	
	private PacMan m_pacMan;
	private GameUI m_gameUI;
	private GameModel m_gameModel;
	private ServerSocket serverSocket;
	private MulticastSocket updateSocket;
	private SocketAddress updateSendPort;
	byte[] ghostNewDirection = new byte[4];
	private int numOfClients = 0;
	
	public ServerNode (PacMan m_pacMan){
		super(); // init node
		//reference to all game models
		this.m_pacMan = m_pacMan;
		this.m_gameUI = m_pacMan.m_gameUI;
		this.m_gameModel = m_pacMan.m_gameModel;
		this.coord = true; //set this node as a coordinator
	}
	
	public void updateGhostPlayers (){
		for (int i=0; i<numOfClients; i++){
			m_gameModel.m_ghosts[i].m_requestedDirection = ghostNewDirection[i];
		}
	}
	
	public void connectToClients(int numOfClients){
			this.numOfClients = numOfClients;
			ServerWorker[] clients = new ServerWorker[numOfClients];
			for (int i=0; i<numOfClients; i++){
			acceptConnection(clients[i], i);
		}
	}

	private  void acceptConnection(ServerWorker sw, int ghostID) {
		//tcpSocket = serverSocket.accept();
		  try{
			  //serverSocket.accept();
		    sw = new ServerWorker(serverSocket.accept(), this, ghostID);
		    Thread t = new Thread(sw);
		    //t.setPriority(Thread.MIN_PRIORITY);
		    t.start();
		  } catch (IOException e) {
		    System.out.println("Accept failed: 4444");
		    //System.exit(-1);
		  }
		m_pacMan.multiplayerActive = true;

		// start new game?
		m_gameModel.m_state = GameModel.STATE_NEWGAME;

	}
	
	public void setUpHosting() {
		m_pacMan.netMultiplayer = true;
		//m_pacMan.controller = true;
		//m_pacMan.playerIsGhost = false;

		
		try {
			//create a new server socket for accepting connections from slaves
			serverSocket = new ServerSocket(LISTENPORT);
			
			//Also initialize the update socket for SENDING
		//	updateSocket = new MulticastSocket(updateSendPort);

			// Oh. Here we do necessary UI changes
			// Then we accept the connection?
			m_gameUI.m_bShowHostingGame = true;

			m_gameUI.hostingIP = ip;
			m_gameUI.portNumber = Integer.toString(serverSocket.getLocalPort());
			m_gameUI.m_bRedrawAll = true;

			m_gameModel.m_state = GameModel.STATE_HOSTING;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
