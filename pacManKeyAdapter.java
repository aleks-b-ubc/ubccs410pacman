import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Key event handlers
class pacManKeyAdapter extends KeyAdapter {
	PacMan m_pacMan;

	pacManKeyAdapter(PacMan pacMan) {
		super();
		m_pacMan = pacMan;
	}

	public void keyPressed(KeyEvent event) {
		
		
		switch (event.getKeyCode()) {
		case KeyEvent.VK_A:
			m_pacMan.m_gameModel.m_ghostPlayer.m_requestedDirection = Thing.LEFT;
			break;

		case KeyEvent.VK_D:
			m_pacMan.m_gameModel.m_ghostPlayer.m_requestedDirection = Thing.RIGHT;
			break;

		case KeyEvent.VK_W:
			m_pacMan.m_gameModel.m_ghostPlayer.m_requestedDirection = Thing.UP;
			break;

		case KeyEvent.VK_S:
			m_pacMan.m_gameModel.m_ghostPlayer.m_requestedDirection = Thing.DOWN;
			break;
		case KeyEvent.VK_LEFT:
			m_pacMan.m_gameModel.m_player.m_requestedDirection = Thing.LEFT;
			break;

		case KeyEvent.VK_RIGHT:
			m_pacMan.m_gameModel.m_player.m_requestedDirection = Thing.RIGHT;
			break;

		case KeyEvent.VK_UP:
			m_pacMan.m_gameModel.m_player.m_requestedDirection = Thing.UP;
			break;

		case KeyEvent.VK_DOWN:
			m_pacMan.m_gameModel.m_player.m_requestedDirection = Thing.DOWN;
			break;

			//N is for NEW SINGLE PLAYER GAME
		case KeyEvent.VK_N:
			// make sure we DO NOT carry over multiplayer state accidently
			// as such we set multiplayer as false
			m_pacMan.m_gameModel.localMultiplayer = false;
			m_pacMan.m_gameModel.m_state = GameModel.STATE_NEWGAME;
			m_pacMan.m_gameUI.m_bDrawPaused = false;
			break;
			
			//C is for CHOOSING color
		case KeyEvent.VK_C:
			m_pacMan.m_gameModel.m_state = GameModel.STATE_COLOR;
			break;

			//L is for LOCAL MULTIPLAYER
		case KeyEvent.VK_L:
			// set Local multiplayer as true.
			m_pacMan.m_gameModel.localMultiplayer = true;
			m_pacMan.m_gameModel.m_state = GameModel.STATE_NEWGAME;
			m_pacMan.m_gameUI.m_bDrawPaused = false;
			break;

			//P is for PAUSE
		case KeyEvent.VK_P:
			if (m_pacMan.m_gameModel.m_state == GameModel.STATE_GAMEOVER)
				break;

			if (m_pacMan.m_gameModel.m_state == GameModel.STATE_PAUSED) {
				m_pacMan.m_gameModel.m_state = m_pacMan.m_gameModel.m_pausedState;
				m_pacMan.m_gameUI.m_bDrawPaused = false;
				m_pacMan.m_gameUI.m_bRedrawAll = true;

			} else {
				m_pacMan.m_gameModel.m_pausedState = m_pacMan.m_gameModel.m_state;
				m_pacMan.m_gameModel.m_state = GameModel.STATE_PAUSED;
			}
			break;

		// K is for Multiplayer waiting screen
		case KeyEvent.VK_M:
			m_pacMan.m_gameModel.m_state = GameModel.STATE_MULTIPLAYER_WAITROOM;
			m_pacMan.m_gameModel.m_nTicks2AboutShow = 0;
			break;

		case KeyEvent.VK_H:
			m_pacMan.m_gameModel.m_state = GameModel.STATE_HIGHSCORE;
			
			break;
			
		// V is for SOUND
		case KeyEvent.VK_V:
			m_pacMan.m_soundMgr.m_bEnabled = !m_pacMan.m_soundMgr.m_bEnabled;
			if (m_pacMan.m_soundMgr.m_bEnabled == false)
				m_pacMan.m_soundMgr.stop();
			m_pacMan.m_bottomCanvas.repaint();
			break;

		case KeyEvent.VK_I:
			m_pacMan.toggleGhostAI();
			break;
			
			
		//1 Sets the color to white
		//2 Sets the color to yellow
		//3 Sets the color to green
		//4 Sets the color to blue
		case KeyEvent.VK_1:
			changePacManColor(Color.white);
			break;
		case KeyEvent.VK_2:
			changePacManColor(Color.yellow);
			break;
		case KeyEvent.VK_3:
			changePacManColor(Color.green);
			break;
		case KeyEvent.VK_4:
			changePacManColor(Color.blue);
			break;

		default:
			System.out.println("Hello World!");
		}
	}

	private void changePacManColor(Color newColor) {
		m_pacMan.m_gameModel.m_things[0].setColor(newColor);
		m_pacMan.m_bottomCanvas.setPacManColor(newColor);
		m_pacMan.m_bottomCanvas.repaint();
		m_pacMan.m_gameModel.m_state = GameModel.STATE_INTRO;
		
	}
}