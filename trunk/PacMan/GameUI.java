import java.awt.*;
import java.util.*;

// GameUI represents the View in an MVC model.  It consists
// of a single Canvas where the play field, intro, and about is
// painted into.  
@SuppressWarnings("serial")
public class GameUI extends Canvas
{
   
   // Cache the various offset to be used
   // when painting the walls for each cell in the maze
   int         WALL0;            // W0   W1 W2 W3 W4  W5  W6
   int         WALL1;            //  |    |  |  |  |  |    |
   int         WALL2;            
   int         WALL3;            // PAL_BEND is a arc through W2-W3-W4
   int         WALL4;            // PAL_EDGE is a solid line through W0-W1 or W5-W6
   int         WALL5;            // PAL_LINE is a solid line through W2-W4
   int         WALL6;
   
   Hashtable   m_redrawHash;    // Contains grid cells that always need to be redrawn, 
                                // even in update.  These are here for special cases like
                                // Powerup pills which are large enough such that passing 
                                // Ghosts or Pacman could clip them.
   HighScoresModel m_highScoresModel;
   
   // Double buffer members
   Graphics    m_offGraphics;
   
   Font        m_font;              // Font for Ghost points string and About page
   boolean     m_bRedrawAll = false;   // Set to true to tell Update to Paint
   boolean     m_bDrawReady = false;
   boolean     m_bShowMultiplayer = false;
   boolean     m_bShowHighScore = false;
   
   boolean	   m_bShowHostingGame = false;
   String	   hostingIP;
   String	   portNumber;
   
   
   Color pacmanColour = Color.yellow; //This is the default color for packman
   
   GameUI (PacMan pacMan, GameModel gameModel, HighScoresModel scoresModel, int width, int height) 
   {
      setSize (width, height);
      m_pacMan    = pacMan;
      m_highScoresModel = scoresModel;
      m_gridInset = CELL_LENGTH / 2;
      WALL0 = 0;
      WALL2 = WALL3 - CELL_LENGTH / 8;
      WALL4 = WALL3 + CELL_LENGTH / 8;
      WALL5 = CELL_LENGTH - WALL1;
      WALL6 = CELL_LENGTH - 1;
      
      m_font = new Font ("Helvetica", Font.BOLD, 14);
   // Refresh the Redraw Hash whenever the board changes.
   void refreshRedrawHash ()
   {
      m_redrawHash.clear ();
      
      // Powerups always need to be redrawn because they may get clipped by the 
      // redraw last location of the player or the ghosts
               m_redrawHash.put (Integer.toString (x) + " " + Integer.toString (y), new Point (x, y));
         }
   }
   
   // Sets the clip so that any rendering is done within the grid
   public void setClip (Graphics g)
   {
      g.setClip (m_gridInset, m_gridInset, CELL_LENGTH * m_gameModel.m_gameSizeX, CELL_LENGTH * m_gameModel.m_gameSizeY);
   }
   
   // Update will only redraw the changed game cells..
   public void update (Graphics g)
   {
      {
         m_bRedrawAll = false;
         paint (g);
      }
      
      if (m_bShowIntro)
      {
         updateIntro (g);
         return;
      }
      
      for (int i =0; i < m_gameModel.m_things.length; i++)
      {
      }
      
      for (Enumeration e = m_redrawHash.elements(); e.hasMoreElements();)
      {
         Point p = ((Point)e.nextElement ()).getLocation ();
         drawGameCell (m_offGraphics, p.x, p.y, false);
      }
      
      // Redraw the Hideout Door
      drawHideoutDoor (m_offGraphics);
      
      for (int i =0; i < m_gameModel.m_things.length; i++)
      {
      }
      // Draw the Ready string (seen right before playing starts)
      if (m_bDrawReady)
      {
         drawReadyString (m_offGraphics);
      }
      
      g.drawImage (m_offImage, 0, 0, this); 
   }
  
   // Draws everything
   public void paint (Graphics g)
   {
      Dimension dim = getSize ();
      
      // Create double buffer if it does not exist or is not
      // the right size
      if (m_offImage == null ||
          m_offDim.width != dim.width ||
          m_offDim.height != dim.height)
      {
         m_offDim = dim;
         m_offImage = createImage (m_offDim.width, m_offDim.height);
         m_offGraphics = m_offImage.getGraphics ();
      }
      
      // Clear everything
      m_offGraphics.setColor (Color.black);
	   m_offGraphics.fillRect (0, 0, m_offDim.width, m_offDim.height);
      
      if (m_bShowIntro)
      {
         paintIntro (g);
         return;
      }
      
      if(m_bShowHostingGame){
    	  paintHostingGame(g);
    	  return;
      }
         
      if (m_bShowColor)
      {
         paintColour (g);
         return;
      }
      
      if (m_bShowMultiplayer)
      {
    	  paintMultiplayer(g);
    	  return;
      }
      
      if(m_bShowHighScore){
    	  paintHighScores(g);
    	  return;
      }
      
      m_offGraphics.setColor (Color.blue);
      
      // Draw from left to right
      for (int x = 0; x < m_gameModel.m_gameSizeX; x++)
      {
         // Draw the column 
         for (int y = 0; y < m_gameModel.m_gameSizeY; y++)
         {
            drawGameCell (m_offGraphics, x, y, false);
         }
      }
     
      setClip (m_offGraphics);
      
      // Draw Hideout Door
      drawHideoutDoor (m_offGraphics);
      
      // Draw PacMan Player and Ghosts
      for (int i =0; i < m_gameModel.m_things.length; i++)
         m_gameModel.m_things[i].draw (this, m_offGraphics);
     
      // Draw the Ready string (seen right before playing starts)
      if (m_bDrawReady)
         drawReadyString (m_offGraphics);
      
      if (m_bDrawGameOver)
         drawGameOverString (m_offGraphics);
      
      if (m_bDrawPaused)
         drawPausedString (m_offGraphics);
      
      // Blitz buffer into actual graphic
      g.drawImage (m_offImage, 0, 0, this); 
   }
   
   private void paintHostingGame(Graphics g) {
	   int         x             = 0;
	      int         y             = 0;
	      int         width         = 0;
	      int         stringLength  = 0;
	      FontMetrics fm;
	      
	      m_offGraphics.setColor (Color.black);
	      m_offGraphics.fillRect (0, 0, m_offDim.width, m_offDim.height);
	      if (m_imagePacman == null)
	      {
	         m_imagePacman = m_gameModel.m_pacMan.getImage (m_gameModel.m_pacMan.getCodeBase (), "pacman.jpg");
	      }
	      
	      // Draw Logo Image
	      y = 50;
	      x = (m_offDim.width - m_imagePacman.getWidth (this)) / 2;
	      m_offGraphics.drawImage (m_imagePacman, x, y, this);
	      
	      m_offGraphics.setFont (m_font);
	      m_offGraphics.setColor (Color.white);
	      fm = m_offGraphics.getFontMetrics();
	      
	      m_offGraphics.setColor (Color.white);
	      x = 10;
	      y = m_gridInset + 10 * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
	      
	      m_offGraphics.drawString ("Welcome to Multiplayer!", x, y);
	      
	      y += fm.getAscent() + fm.getDescent ();
	      m_offGraphics.drawString ("You are HOSTING a game.", x, y);
	      
	      y += fm.getAscent() + fm.getDescent ();
	      m_offGraphics.drawString ("Waiting for connections.", x, y);
	      
	      y += fm.getAscent() + fm.getDescent ();
	      m_offGraphics.drawString ("Your IP address is: "+hostingIP, x, y);
	      
	      y += fm.getAscent() + fm.getDescent();
	      m_offGraphics.drawString("Your port number is: "+portNumber, x, y);

	      // Blitz buffer to screen
	      g.drawImage (m_offImage, 0, 0, this); 
	
}

private void paintHighScores(Graphics g) {
	   int         x             = 0;
	      int         y             = 0;
	      FontMetrics fm;
	      
	      m_offGraphics.setColor (Color.black);
	      m_offGraphics.fillRect (0, 0, m_offDim.width, m_offDim.height);
	      if (m_imagePacman == null)
	      {
	    	//TODO REMOVE COMMENT
	         //m_imagePacman = m_gameModel.m_pacMan.getImage (m_gameModel.m_pacMan.getCodeBase (), "pacman.jpg");
	      }
	      
	      // Draw Logo Image
	      y = 50;
	      
	    //TODO REMOVE COMMENT
	      //x = (m_offDim.width - m_imagePacman.getWidth (this)) / 2;
	      //m_offGraphics.drawImage (m_imagePacman, x, y, this);
	      
	      m_offGraphics.setFont (m_font);
	      m_offGraphics.setColor (Color.white);
	      fm = m_offGraphics.getFontMetrics();
	      
	      m_offGraphics.setColor (Color.white);
	      x = 10;
	      y = m_gridInset + 10 * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
	      
	      m_offGraphics.drawString ("HIGH SCORES", x, y);
	      
	    //For Testing Only - Still needs to be incorporated
	      m_highScoresModel.addHighScore(5000, "Anna");
	      m_highScoresModel.addHighScore(2000, "Bob");
	      m_highScoresModel.addHighScore(3000, "Adi");
	      m_highScoresModel.addHighScore(4000, "Danny");
	      
	      int count = m_highScoresModel.numberOfHighScores() - 1;
	      String[] draw = new String[m_highScoresModel.numberOfHighScores()];
	      draw = m_highScoresModel.forDrawingHighScores();
	      
	      while( count >= 0 )
	      {
	    	  y = y + 20;
	    	  m_offGraphics.drawString(draw[count], x, y);
	    	  count--;
	      }
	       
	      // Blitz buffer to screen
	      g.drawImage (m_offImage, 0, 0, this); 
	
}

public void paintMultiplayer(Graphics g) {
	   	  int         x             = 0;
	      int         y             = 0;
	      FontMetrics fm;
	      
	      m_offGraphics.setColor (Color.black);
	      m_offGraphics.fillRect (0, 0, m_offDim.width, m_offDim.height);
	      if (m_imagePacman == null)
	      {
	    	//TODO REMOVE COMMENT
	        // m_imagePacman = m_gameModel.m_pacMan.getImage (m_gameModel.m_pacMan.getCodeBase (), "pacman.jpg");
	      }
	      
	      // Draw Logo Image
	      y = 50;
	      //x = (m_offDim.width - m_imagePacman.getWidth (this)) / 2;
	      //m_offGraphics.drawImage (m_imagePacman, x, y, this);
	      
	      m_offGraphics.setFont (m_font);
	      m_offGraphics.setColor (Color.white);
	      fm = m_offGraphics.getFontMetrics();
	      
	      m_offGraphics.setColor (Color.white);
	      x = 10;
	      y = m_gridInset + 10 * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
	      
	      m_offGraphics.drawString ("Welcome to Multiplayer!", x, y);
	      
	      y += fm.getAscent() + fm.getDescent ();
	      m_offGraphics.drawString ("Press 'H' to Host the game.", x, y);
	      
	      y += fm.getAscent() + fm.getDescent ();
	      m_offGraphics.drawString ("Press 'C' to Connect the game.", x, y);
	      
	       
	      // Blitz buffer to screen
	      g.drawImage (m_offImage, 0, 0, this); 

}


// Displays the Color selection page containing the PAC-MAN banner
   public void paintColour (Graphics g)
   {
      Font        m_Colorfont = new Font ("Helvetica", Font.BOLD, 18);
      m_offGraphics.setColor (Color.black);
      if (m_imagePacman == null)
      {
    	//TODO REMOVE COMMENT
        //m_imagePacman = m_gameModel.m_pacMan.getImage (m_gameModel.m_pacMan.getCodeBase (), "pacman.jpg");
      }
      // Draw Logo Image
      y = 50;
      
      //TODO REMOVE COMMENT
      //m_offGraphics.drawImage (m_imagePacman, x, y, this);
      
      m_offGraphics.setFont (m_Colorfont);
      fm = m_offGraphics.getFontMetrics();
      
      x = 10;
      m_offGraphics.drawString ("Welcome to PAC-MAN in Java!", x, y);
      
      // Calculate x,y offsets and generate color selection menu
      
      y += fm.getAscent() + fm.getDescent ();
      y += fm.getAscent() + fm.getDescent ();
      m_offGraphics.drawString ("Pess '1' for WHITE.", x, y);
      
      y += fm.getAscent() + fm.getDescent ();
      m_offGraphics.setColor (Color.yellow);
      m_offGraphics.drawString ("Pess '2' for YELLOW.", x, y);
      
      y += fm.getAscent() + fm.getDescent ();
      m_offGraphics.setColor (Color.green);
      m_offGraphics.drawString ("Pess '3' for GREEN.", x, y);
      
      y += fm.getAscent() + fm.getDescent ();
      m_offGraphics.setColor (Color.blue);
      m_offGraphics.drawString ("Pess '4' for BLUE.", x, y);
      
      g.drawImage (m_offImage, 0, 0, this); 
         
   
   public void updateIntro (Graphics g)
   {
      {
         // Draw Logo Image
         y = 50;
         m_offGraphics.drawImage (m_imagePacman, x, y, this);
      }
      for (int i =0; i < m_gameModel.m_ghosts.length; i++)
         redrawLastLocation (m_offGraphics, m_gameModel.m_ghosts[i]);
      redrawLastLocation (m_offGraphics, m_gameModel.m_player);
      
      // Draw Ghosts and PacMan Player
      for (int i =0; i < m_gameModel.m_ghosts.length; i++)
         m_gameModel.m_ghosts[i].draw (this, m_offGraphics);
      m_gameModel.m_player.draw (this, m_offGraphics);
      // Draw the Food and Powerup with 10 and 50 point worth
      x = 250;
      m_offGraphics.setColor (Color.pink);
      m_offGraphics.fillOval (m_gridInset + 12 * CELL_LENGTH + WALL2, m_gridInset + 22 * CELL_LENGTH + WALL2, WALL1, WALL1);
      if ((m_gameModel.m_pacMan.m_globalTickCount % (1000 / m_pacMan.m_delay)) > (200 / m_pacMan.m_delay))
         m_offGraphics.setColor (Color.pink);
      else
         m_offGraphics.setColor (Color.black);
      m_offGraphics.fillOval (m_gridInset + 12 * CELL_LENGTH, m_gridInset + 24 * CELL_LENGTH, CELL_LENGTH, CELL_LENGTH);
      
      g.drawImage (m_offImage, 0, 0, this); 
   }
   
   public void paintIntro (Graphics g)
   {
      int         width         = 0;
      int         stringLength  = 0;
      m_offGraphics.setColor (Color.black);
      if (m_imagePacman == null)
      {
    	 //TODO: REMOVE comment!
         //m_imagePacman = m_gameModel.m_pacMan.getImage (m_gameModel.m_pacMan.getCodeBase (), "pacman.jpg");
      }
      y = 50;
      
      //TODO REMOVE COMMENT
      
      
      // Draw Ghosts and PacMan Player
      for (int i =0; i < m_gameModel.m_ghosts.length; i++)
         m_gameModel.m_ghosts[i].draw (this, m_offGraphics);
      m_gameModel.m_player.draw (this, m_offGraphics);
      fm = m_offGraphics.getFontMetrics();
      
      x = 150;
      // Draw Ghost intro strings
      y = m_gridInset + 10 * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
      m_offGraphics.setColor (Color.red);
      m_offGraphics.drawString (padString (m_offGraphics, "\"BLINKY\" ", stringLength),x , y);
      
      y = m_gridInset + 12 * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
      m_offGraphics.setColor (Color.pink);
      m_offGraphics.drawString (padString (m_offGraphics, "\"PINKY\" ", stringLength),x ,y);
      
      y = m_gridInset + 14 * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
      m_offGraphics.setColor (Color.cyan);
      m_offGraphics.drawString (padString (m_offGraphics, "\"INKY\" ", stringLength), x , y);
      
      y = m_gridInset + 16 * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
      m_offGraphics.setColor (Color.orange);
      m_offGraphics.drawString (padString (m_offGraphics, "\"CLYDE\" ", stringLength),x , y);
      // Draw the Food and Powerup with 10 and 50 point worth
      m_offGraphics.setFont (m_font);
      m_offGraphics.setColor (Color.pink);
      m_offGraphics.fillOval (m_gridInset + 12 * CELL_LENGTH + WALL2, m_gridInset + 22 * CELL_LENGTH + WALL2, WALL1, WALL1);
      m_offGraphics.drawString ("= 10 Points", x, m_gridInset + 22 * CELL_LENGTH + fm.getAscent() / 2);
      m_offGraphics.drawString ("= 50 Points", x, m_gridInset + 24 * CELL_LENGTH + fm.getAscent() / 2);
      
      if ((m_gameModel.m_pacMan.m_globalTickCount % (1000 / m_pacMan.m_delay)) > (200 / m_pacMan.m_delay))
         m_offGraphics.setColor (Color.pink);
      else
         m_offGraphics.setColor (Color.black);
      m_offGraphics.fillOval (m_gridInset + 12 * CELL_LENGTH, m_gridInset + 24 * CELL_LENGTH, CELL_LENGTH, CELL_LENGTH);
      
      y = 500;
      m_offGraphics.drawString ("Written by Benny Chow, 2001", x ,y);
      
      // Blitz buffer to screen
      g.drawImage (m_offImage, 0, 0, this); 
   
   public String padString (Graphics g, String stuff, int length)
   {
   }
   
      int         y;
      Dimension   dim = getSize ();
      g.setFont (m_readyFont);
      fm = g.getFontMetrics();
      y = m_gridInset + (m_gameModel.m_doorLocY + 1) * CELL_LENGTH;
		y = m_gridInset + (m_gameModel.m_doorLocY + 2) * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
      g.drawString ("PAUSED", x, y);
   // draw game over string
      int         y;
      Dimension   dim = getSize ();
      g.setFont (m_readyFont);
      fm = g.getFontMetrics();
      y = m_gridInset + (m_gameModel.m_doorLocY + 1) * CELL_LENGTH;
		y = m_gridInset + (m_gameModel.m_doorLocY + 2) * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
      g.drawString ("GAME OVER", x, y);
   
      int         y;
      int         width1;
      int         width2;
      Dimension   dim = getSize ();
      g.setColor (Color.yellow);
      fm = g.getFontMetrics();
      g.setFont (m_readyFontItalic);
      x = (dim.width - (width1 + width2)) / 2;
		y = m_gridInset + m_gameModel.m_readyY * CELL_LENGTH + CELL_LENGTH / 2 + fm.getAscent() / 2;
      g.drawString ("READY ", x, y);
      g.setFont (m_readyFontItalic);
		g.drawString (" !!!", x, y);
   }
   
   public void drawHideoutDoor (Graphics g)
   {
      g.setColor (Color.pink);
      g.fillRect (m_gridInset + (m_gameModel.m_doorLocX - 1) * CELL_LENGTH, m_gridInset + m_gameModel.m_doorLocY * CELL_LENGTH + WALL2 + WALL2 / 2, CELL_LENGTH * 4, WALL4 - WALL2);
   }
   
   // and then draw the contents of the cell.
   // bClearExtra is used to clean up the rendering of Things that exceed the
   // boundbox of the each grid cell.
      int      y1 = m_gridInset + y * CELL_LENGTH;
      int      gameCell;
      Color    wallColor;
      
      if (!m_bFlipWallColor)
         wallColor = m_wallColor;
      else
         wallColor = m_wallAltColor;
      
      if (!bClearExtra)
	   
      g.setColor (wallColor);
      
      gameCell = m_gameModel.m_gameState [x][y];
      
      // Treat the Hideout door as a special case because it will 
      // be re-drawn last.
      if (y == m_gameModel.m_doorLocY &&
          x >= m_gameModel.m_doorLocX - 1&&
          x <= m_gameModel.m_doorLocX + 2)
         return;
      
      // If we are in the intro, don't draw any walls.
      if (m_bShowIntro == true)
         return;
          
      if ((gameCell & GameModel.PAL_EDGE_TOP) != 0)
      {
         g.fillRect (x1, y1, CELL_LENGTH, WALL1);
      } 
      if ((gameCell & GameModel.PAL_EDGE_LEFT) != 0)
      {
         // If the edge has no connector then smooth it out...
         if ((y != 0) &&
             (m_gameModel.m_gameState [x][y-1] & GameModel.PAL_LINE_HORIZ) != 0)
         {
            g.fillArc (x1 - WALL1, y1 + CELL_LENGTH - WALL1, WALL1 * 2, WALL1 * 2, 0, 90);
            
         } else if ((y != m_gameModel.m_gameSizeY - 1) &&
             (m_gameModel.m_gameState [x][y+1] & GameModel.PAL_LINE_HORIZ) != 0)
         {
            g.fillArc (x1 - WALL1, y1 - WALL1, WALL1 * 2, WALL1 * 2, 0, -90);
            
         } else {
            g.fillRect (x1, y1, WALL1, CELL_LENGTH);
         }
      }
      if ((gameCell & GameModel.PAL_EDGE_BOTTOM) != 0)
      {
         g.fillRect (x1, y1 + CELL_LENGTH - WALL1, CELL_LENGTH, WALL1);
      } 
      if ((gameCell & GameModel.PAL_EDGE_RIGHT) != 0)
      {
         // If the edge has no connector then smooth it out...
         if ((y != 0) &&
             (m_gameModel.m_gameState [x][y-1] & GameModel.PAL_LINE_HORIZ) != 0)
         {
            g.fillArc (x1 + CELL_LENGTH - WALL1, y1 + CELL_LENGTH - WALL1, WALL1 * 2, WALL1 * 2, 180, -90);
            
         } else if ((y != m_gameModel.m_gameSizeY - 1) &&
             (m_gameModel.m_gameState [x][y+1] & GameModel.PAL_LINE_HORIZ) != 0)
         {
            g.fillArc (x1 + CELL_LENGTH - WALL1, y1 - WALL1, WALL1 * 2, WALL1 * 2, 180, 90);
            
         } else {
            g.fillRect (x1 + CELL_LENGTH - WALL1, y1, WALL1, CELL_LENGTH);
         }
      }
      if ((gameCell & GameModel.PAL_LINE_HORIZ) != 0)
      {
         g.fillRect (x1, y1 + WALL2, CELL_LENGTH, WALL4 - WALL2);
      } 
      if ((gameCell & GameModel.PAL_LINE_VERT) != 0)
      {
         g.fillRect (x1 + WALL2, y1, WALL4 - WALL2, CELL_LENGTH);
      }
      
      if ((gameCell & GameModel.PAL_BEND_TOPLEFT) != 0)
      {
         // Smooth out the corner 
         if ((gameCell & GameModel.PAL_EDGE_BOTTOM) != 0 && (gameCell & GameModel.PAL_EDGE_RIGHT) != 0 )
         {
            g.setColor (Color.black);
            g.fillRect (x1, y1 + CELL_LENGTH - WALL1, CELL_LENGTH, WALL1);
            g.fillRect (x1 + CELL_LENGTH - WALL1, y1, WALL1, CELL_LENGTH);
            g.setColor (wallColor);
            g.fillArc (x1 - CELL_LENGTH, y1 - CELL_LENGTH, CELL_LENGTH * 2, CELL_LENGTH * 2, 0, -90);
            g.setColor (Color.black);
            g.fillArc (x1 - CELL_LENGTH + WALL1, y1 - CELL_LENGTH + WALL1, WALL5 * 2, WALL5 *2, 0, -90);
            g.setColor (wallColor);
         }
         g.fillArc (x1 - WALL4, y1 - WALL4, WALL4 * 2, WALL4 * 2, 0, -90);
         g.setColor (Color.black);
         g.fillArc (x1 - WALL2, y1 - WALL2, WALL2 * 2, WALL2 * 2, 0, -90);
         g.setColor (wallColor);
         // Draw corner for adjacent edges
         if ((x != 0) &&
             (y != 0) &&
             (m_gameModel.m_gameState [x-1][y] & GameModel.PAL_EDGE_TOP) != 0 &&
             (m_gameModel.m_gameState [x][y-1] & GameModel.PAL_EDGE_LEFT) != 0)
         {
            g.fillArc (x1 - WALL1, y1 - WALL1, WALL1 * 2, WALL1 * 2, 0, -90);
         }
         // Draw corner for edge against wall
         if ((x == 0) &&
             (y != 0) &&
             (m_gameModel.m_gameState [x][y-1] & GameModel.PAL_EDGE_LEFT) != 0)
         {
            g.fillArc (x1 - WALL1, y1 - WALL1, WALL1 * 2, WALL1 * 2, 0, -90);
         }
             
      } 
      if ((gameCell & GameModel.PAL_BEND_BOTTOMLEFT) != 0)
      {
         // Smooth out the corner 
         if ((gameCell & GameModel.PAL_EDGE_TOP) != 0 && (gameCell & GameModel.PAL_EDGE_RIGHT) != 0 )
         {
            g.setColor (Color.black);
            g.fillRect (x1, y1, CELL_LENGTH, WALL1);
            g.fillRect (x1 + CELL_LENGTH - WALL1, y1, WALL1, CELL_LENGTH);
            g.setColor (wallColor);
            g.fillArc (x1 - CELL_LENGTH, y1, CELL_LENGTH * 2, CELL_LENGTH * 2, 0, 90);
            g.setColor (Color.black);
            g.fillArc (x1 - CELL_LENGTH + WALL1, y1 + WALL1, WALL5 * 2, WALL5 *2, 0, 90);
            g.setColor (wallColor);
         }
         g.fillArc (x1 - WALL4, y1 + CELL_LENGTH - WALL4, WALL4 * 2, WALL4 * 2, 0, 90);
         g.setColor (Color.black);
         g.fillArc (x1 - WALL2, y1 + CELL_LENGTH - WALL2, WALL2 * 2, WALL2 * 2, 0, 90);
         g.setColor (wallColor);
         // Draw corner for adjacent edges
         if ((x != 0) &&
             (y != m_gameModel.m_gameSizeY - 1) &&
             (m_gameModel.m_gameState [x-1][y] & GameModel.PAL_EDGE_BOTTOM) != 0 &&
             (m_gameModel.m_gameState [x][y+1] & GameModel.PAL_EDGE_LEFT) != 0)
         {
            g.fillArc (x1 - WALL1, y1 + CELL_LENGTH - WALL1, WALL1 * 2, WALL1 * 2, 0, 90);
         }
         // Draw corner for edge against wall
         if ((x == 0) &&
             (y != m_gameModel.m_gameSizeY - 1) &&
             (m_gameModel.m_gameState [x][y+1] & GameModel.PAL_EDGE_LEFT) != 0)
         {
            g.fillArc (x1 - WALL1, y1 + CELL_LENGTH - WALL1, WALL1 * 2, WALL1 * 2, 0, 90);
         }
      }
      if ((gameCell & GameModel.PAL_BEND_BOTTOMRIGHT) != 0) // Door is always on top
      {
         // Smooth out the corner 
         if ((gameCell & GameModel.PAL_EDGE_TOP) != 0 && (gameCell & GameModel.PAL_EDGE_LEFT) != 0 )
         {
            g.setColor (Color.black);
            g.fillRect (x1, y1, CELL_LENGTH, WALL1);
            g.fillRect (x1, y1, WALL1, CELL_LENGTH);
            g.setColor (wallColor);
            g.fillArc (x1, y1, CELL_LENGTH * 2, CELL_LENGTH * 2, 90, 90);
            g.setColor (Color.black);
            g.fillArc (x1 + WALL1, y1 + WALL1, WALL5 * 2, WALL5 *2, 90, 90);
            g.setColor (wallColor);
         }
         g.fillArc (x1 + CELL_LENGTH - WALL4, y1 + CELL_LENGTH - WALL4, WALL4 * 2, WALL4 * 2, -180, -90);
         g.setColor (Color.black);
         g.fillArc (x1 + CELL_LENGTH - WALL2, y1 + CELL_LENGTH - WALL2, WALL2 * 2, WALL2 * 2, -180, -90);
         g.setColor (wallColor);
         // Draw corner for adjacent edges
         if ((x != m_gameModel.m_gameSizeX - 1) &&
             (y != m_gameModel.m_gameSizeY - 1) &&
             (m_gameModel.m_gameState [x][y+1] & GameModel.PAL_EDGE_RIGHT) != 0 &&
             (m_gameModel.m_gameState [x+1][y] & GameModel.PAL_EDGE_BOTTOM) != 0)
         {
            g.fillArc (x1 + CELL_LENGTH - WALL1, y1 + CELL_LENGTH - WALL1, WALL1 * 2, WALL1 * 2, 90, 90);
         }
         // Draw corner for edge against wall
         if ((x == m_gameModel.m_gameSizeX - 1) &&
             (y != m_gameModel.m_gameSizeY - 1) &&
             (m_gameModel.m_gameState [x][y+1] & GameModel.PAL_EDGE_RIGHT) != 0)
         {
            g.fillArc (x1 + CELL_LENGTH - WALL1, y1 + CELL_LENGTH - WALL1, WALL1 * 2, WALL1 * 2, 90, 90);
         }
      }
      if ((gameCell & GameModel.PAL_BEND_TOPRIGHT) != 0) // Door is always on bottom
      {
         // Smooth out the corner 
         if ((gameCell & GameModel.PAL_EDGE_BOTTOM) != 0 && (gameCell & GameModel.PAL_EDGE_LEFT) != 0 )
         {
            g.setColor (Color.black);
            g.fillRect (x1, y1 + CELL_LENGTH - WALL1, CELL_LENGTH, WALL1);
            g.fillRect (x1, y1, WALL1, CELL_LENGTH);
            g.setColor (wallColor);
            g.fillArc (x1, y1 - CELL_LENGTH, CELL_LENGTH * 2, CELL_LENGTH * 2, 180, 90);
            g.setColor (Color.black);
            g.fillArc (x1 + WALL1, y1 - CELL_LENGTH + WALL1, WALL5 * 2, WALL5 *2, 180, 90);
            g.setColor (wallColor);
         }
         g.fillArc (x1 + CELL_LENGTH - WALL4, y1 - WALL4, WALL4 * 2, WALL4 * 2, -180, 90);
         g.setColor (Color.black);
         g.fillArc (x1 + CELL_LENGTH - WALL2, y1 - WALL2, WALL2 * 2, WALL2 * 2, -180, 90);
         g.setColor (wallColor);
         // Draw corner for adjacent edges
         if ((x != m_gameModel.m_gameSizeX - 1) &&
             (y != 0) &&
             (m_gameModel.m_gameState [x+1][y] & GameModel.PAL_EDGE_TOP) != 0 &&
             (m_gameModel.m_gameState [x][y-1] & GameModel.PAL_EDGE_RIGHT) != 0)
         {
            g.fillArc (x1 + CELL_LENGTH - WALL1, y1 - WALL1, WALL1 * 2, WALL1 * 2, 180, 90);
         }
         // Draw corner for edge against wall
         if ((x == m_gameModel.m_gameSizeX - 1) &&
             (y != 0) &&
             (m_gameModel.m_gameState [x][y-1] & GameModel.PAL_EDGE_RIGHT) != 0)
         {
            g.fillArc (x1 + CELL_LENGTH - WALL1, y1 - WALL1, WALL1 * 2, WALL1 * 2, 180, 90);
         }
      }
      if ((gameCell & GameModel.GS_FOOD) != 0)
      {
         g.setColor (Color.pink);
         g.fillOval (x1 + WALL2, y1 + WALL2, WALL1, WALL1);
         g.setColor (wallColor);
      }
      if ((gameCell & GameModel.GS_POWERUP) != 0)
      {
         if ((m_gameModel.m_pacMan.m_globalTickCount % (1000 / m_pacMan.m_delay)) > (200 / m_pacMan.m_delay))
            g.setColor (Color.pink);
         else
            g.setColor (Color.black);
         
         g.fillOval (x1, y1, CELL_LENGTH, CELL_LENGTH);
         g.setColor (wallColor);
      }
   // This method will redraw the cells that thing last occupied.
   // The reason why we have to save the deltaLoc is because
   // the thing may have crossed over to an adjacent cell, moved in the new
   // cell, but it still rendering a part of itself to the previous cell.
   // In this case, m_locationX and Y refer to the new cell, even though
   // the thing is spilling over into the previous cell
      if (thing.m_lastLocX < 0 && thing.m_lastLocY < 0)
      if (thing.m_lastDeltaLocX < 0 && thing.m_lastLocX != 0)
         drawGameCell (g, thing.m_lastLocX - 1, thing.m_lastLocY, true);
         drawGameCell (g, thing.m_lastLocX + 1, thing.m_lastLocY, true);
      else if (thing.m_lastDeltaLocY < 0 && thing.m_lastLocY != 0)
         drawGameCell (g, thing.m_lastLocX, thing.m_lastLocY - 1, true);
      else if (thing.m_lastDeltaLocY > 0 && thing.m_lastLocY != m_gameModel.m_gameSizeY - 1)
         drawGameCell (g, thing.m_lastLocX, thing.m_lastLocY + 1, true);
      drawGameCell (g, thing.m_lastLocX, thing.m_lastLocY, true);


}
