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

		case KeyEvent.VK_N:
			// make sure we DO NOT carry over multiplayer state accidently
			// as such we set multiplayer as false
			m_pacMan.m_gameModel.multiplayer = false;
			m_pacMan.m_gameModel.m_state = GameModel.STATE_NEWGAME;
			m_pacMan.m_gameUI.m_bDrawPaused = false;

			// TODO: REMOVE
			System.out.println("N key pressed. Multiplayer set to "
					+ m_pacMan.m_gameModel.multiplayer);
			break;

		case KeyEvent.VK_L:
			// set multiplayer as true.
			m_pacMan.m_gameModel.multiplayer = true;
			m_pacMan.m_gameModel.m_state = GameModel.STATE_NEWGAME;
			m_pacMan.m_gameUI.m_bDrawPaused = false;

			// TODO: REMOVE
			System.out.println("M key pressed. Multiplayer set to "
					+ m_pacMan.m_gameModel.multiplayer);
			break;

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
			m_pacMan.m_gameModel.m_state = GameModel.STATE_MULTIPLAYER_SELECT;
			m_pacMan.m_gameModel.m_nTicks2AboutShow = 0;
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

		default:
			System.out.println("Hello World!");
		}
	}
}