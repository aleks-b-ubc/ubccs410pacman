package trunk.PacMan;

/**
 * 
 */
import junit.framework.TestCase;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.applet.*;

import org.fest.swing.*;
import org.fest.swing.applet.AppletViewer;
import org.fest.swing.launcher.AppletLauncher;
import org.fest.util.*;
import org.fest.assertions.*;


public class UserStoriesTest extends TestCase {
	
	private PacMan p1;
	
	protected void setUp()
	{
		/**
		Frame appletFrame = new Frame("Pacman");
		appletFrame.setLayout(new GridLayout(1,0));
		appletFrame.resize(400, 400);
		appletFrame.show();
		
		a1 = new PacMan();
		appletFrame.add(a1);;
		
		a1.init();
		a1.start();
		**/
		
		p1 = new PacMan();
		p1.testing = true;
		p1.init();

		
		AppletViewer viewer = AppletLauncher.applet(p1).start();
		

	}

	// See the current Score
	public void testSeeCurrentScore()
	{
		assertFalse(p1.m_gameModel.m_player.m_score < 0 );
		assert(p1.m_topCanvas.m_seeScore == true);	
	}
	
	// Play as Ghost
	public void testPlayAsGhost()
	{
		p1.m_gameModel.newGame();
		p1.m_gameModel.localMultiplayer = true;

		Ghost gp = null;
		gp = (Ghost) p1.m_gameModel.m_ghosts[0];
		
		assert( gp.canMove() == true );
		
	}
	
	// Initial Welcome Screen
	public void testWelcomeScreen()
	{
		p1.m_gameUI.m_bShowIntro = true;
		assert( p1.m_gameUI.m_seeIntroScreen == true );
	}
	
	// Speed of game increases with increasing levels
	public void testSpeedIncrease()
	{
		p1.m_gameModel.localMultiplayer = false;
		p1.netMultiplayer = false;
		
		
		p1.m_gameModel.m_stage = 0;
		p1.m_gameModel.newGame();
		

		int ticksStageZero = p1.m_ticksPerSec;
		int delayStageZero = p1.m_delay;
		
		p1.m_gameModel.m_nLives = 0;
		p1.m_gameModel.m_state = GameModel.STATE_DEADPACMAN;
		p1.tick();
		
		p1.m_gameModel.m_stage = 1;
		p1.m_gameModel.loadNextLevel();

		int ticksStageOne = p1.m_ticksPerSec;
		int delayStageOne = p1.m_delay;
		
		p1.m_gameModel.m_nLives = 0;
		p1.m_gameModel.m_state = GameModel.STATE_DEADPACMAN;
		p1.tick();
		

		// There should be more ticks per second for increasing levels
		assertTrue( ticksStageOne > ticksStageZero );
		// The delay between ticks should decrease for increasing levels
		assertTrue( delayStageOne < delayStageZero );
		
	}
	
	@Test ( expected = NullPointerException.class) public void empty() { 
	    p1.getCodeBase(); 
	    p1.getAppletContext();
	}
}
