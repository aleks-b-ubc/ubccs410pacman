import java.io.Serializable;


//creating my own class for encapsulation of necessary data
//that will go between the nodes
@SuppressWarnings("serial")
public class PacmanDataPacket implements Serializable{
	
	/**
	static final int STATE_START = 0; // Not used.
	static final int STATE_DEADPACMAN = 1; // Pacman's dead. Use next life or
											// GameOver?
	static final int STATE_GAMEOVER = 2; // All lives gone. Game Over
	static final int STATE_LEVELCOMPLETE = 3; // Flash the board and advance the
												// level
	static final int STATE_PLAYING = 4; // Normal playing
	static final int STATE_BEGIN_PLAY = 5; // Animation during start of play
											// "Ready !"
	static final int STATE_DEAD_PLAY = 6; // Animation of Pacman dying
	static final int STATE_NEWGAME = 7; // Initialization of a new game
	static final int STATE_PAUSED = 8; // Used to pause the game
	static final int STATE_INTRO = 9; // Intro to game with nice JPEG banner
	static final int STATE_MULTIPLAYER_WAITROOM = 10; // Used to go go the Multiplayer waiting page
	static final int STATE_COLOR = 11; // Color selection page
	static final int STATE_HIGHSCORE = 12; //High score selectino page
	static final int STATE_HOST = 13;		//Hosting a multiplayer game
	static final int STATE_CONNECT = 14;	//Connecting to a multiplayer game
	*/
	
	int currentGameState;
	GameModel gameModel;
	
	public PacmanDataPacket(int gameState){
		currentGameState = gameState;
	}
	
	public PacmanDataPacket(GameModel model){
		gameModel = model;
		currentGameState = gameModel.m_state;
	}

	
	public String stateToString(){
		switch(currentGameState){
		case GameModel.STATE_HOSTING:
			return "STATE_HOSTING";
		case GameModel.STATE_SET_UP_CONNECTION:
			return "STATE_SET_UP_CONNECTION";
		case GameModel.STATE_CONNECT:
			return "STATE_CONNECT";
	    case GameModel.STATE_MULTIPLAYER_WAITROOM:
	    	  return "STATE_MULTIPLAYER_WAITROOM";
	    case GameModel.STATE_INTRO:
	    	  return "STATE_INTRO";
	    case GameModel.STATE_PAUSED:
	    	  return "STATE_PAUSED";
	    case GameModel.STATE_NEWGAME:
	    	  return "STATE_NEWGAME";
	    case GameModel.STATE_GAMEOVER:
	    	  return "STATE_GAMEOVER";
	    case GameModel.STATE_LEVELCOMPLETE:
	    	  return "STATE_LEVELCOMPLETE";
        case GameModel.STATE_DEADPACMAN:
	    	  return "STATE_DEADPACMAN";
	    case GameModel.STATE_BEGIN_PLAY:
	    	  return "STATE_BEGIN_PLAY";
        case GameModel.STATE_PLAYING:
	    	  return "STATE_PLAYING";
	    case GameModel.STATE_DEAD_PLAY:
	    	  return "STATE_DEAD_PLAY";
	    case GameModel.STATE_COLOR:
	    	  return "STATE_COLOR";
	    case GameModel.STATE_HIGHSCORE:
	    	  return "STATE_HIGHSCORE";
		default:
	    	  return "Well, you're screwd. Something broke!";
	      }
	}
}
