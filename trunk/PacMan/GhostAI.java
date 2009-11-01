import java.awt.*;


class GhostAI extends Ghost
{
  
      
   GhostAI (GameModel gameModel, byte type, int startX, int startY, boolean bMiddle, Color color, int nExitMilliSec)
   {
      super (gameModel, type, startX, startY, bMiddle);
      m_deltaMax = m_ghostDeltaMax;
      m_destinationX = -1;
      m_destinationY = -1;
      m_color = color;
      m_bInsideRoom = true;
      m_nExitMilliSec = nExitMilliSec;
      m_nTicks2Exit = m_nExitMilliSec / gameModel.m_pacMan.m_delay;
   }
   

   
   // Overriden to update Ghost's directions
   public void tickThing ()
   {  
      boolean  bBackoff = false;
      // Don't let the ghost go back the way it came.
      byte prevDirection = STILL;
       
      // Count down for how long the Points for eating the Ghost popup
      if (m_nTicks2Popup > 0)
      {
         m_nTicks2Popup--;
         if (m_nTicks2Popup == 0)
         {
            m_gameModel.setPausedGame (false);      
            m_gameModel.m_player.setVisible (true);
            m_gameModel.m_pacMan.m_soundMgr.playSound (SoundManager.SOUND_RETURNGHOST);
         }
      }
         
      // Count down until Ghost can leave Hideout
      if (m_nTicks2Exit > 0)
      {
         m_nTicks2Exit--;
         if (m_nTicks2Exit == 0)
         {
            m_destinationX = -1;
            m_destinationY = -1;   
         }
      }
      
      // Count down until the powerup expires
      if (m_nTicks2Flee > 0)
      {
         m_nTicks2Flee--;
         if (m_nTicks2Flee == 0 && !m_bEaten)
         {
            m_deltaMax = m_ghostDeltaMax;
            m_bEaten   = false;
            m_destinationX = -1;
            m_destinationY = -1;   
         }
      }
      
      // If the ghost is located at the door and is ready to enter because
      // he was eaten, then let him in.
      if (m_bEaten &&
          m_locX == m_gameModel.m_doorLocX && 
          m_locY == (m_gameModel.m_doorLocY - 1) &&
          m_deltaLocX == 0 &&
          m_deltaLocY == 0)
      {
         m_destinationX = m_gameModel.m_doorLocX;
         m_destinationY = m_gameModel.m_doorLocY + 2;
         m_direction = DOWN;
         m_deltaLocY = 1;
         m_bInsideRoom = true;
         m_nTicks2Flee = 0;
         m_bEnteringDoor = true;
         m_deltaMax = m_ghostDeltaMax;
         return;
      }
      
      // If the ghost has entered the room and was just eaten,
      // reset it so it can wander in the room a bit before coming out
      if (m_bEaten &&
          m_locX == m_gameModel.m_doorLocX && 
          m_locY == (m_gameModel.m_doorLocY + 2) &&
          m_deltaLocX == 0 &&
          m_deltaLocY == 0)
      {
         m_destinationX = -1;
         m_destinationY = -1;
         m_direction = STILL;
         m_nTicks2Exit = 3000 / m_gameModel.m_pacMan.m_delay;
         m_bEnteringDoor = false;
         m_bEaten = false;
         return;
      }
      
      // If the ghost was just eaten and is returning to the hideout, 
      // if during this time Pacman eats another powerup, we need
      // to set the destinationX and Y back so that the ghost will continue
      // to enter the room and not get stuck
      if (m_bEnteringDoor)
      {
         m_destinationX = m_gameModel.m_doorLocX;
         m_destinationY = m_gameModel.m_doorLocY + 2;
         m_direction = DOWN;
      }
      
      // If the ghost is located at the door and is ready to leave, 
      // then let him out.
      if (m_bInsideRoom &&
          m_locX == m_gameModel.m_doorLocX && 
          m_locY == m_gameModel.m_doorLocY + 2 && 
          m_deltaLocX == 0 &&
          m_deltaLocY == 0 &&
          m_nTicks2Exit == 0)
      {
         m_destinationX = m_locX;
         m_destinationY = m_gameModel.m_doorLocY - 1;
         m_direction = UP;
         m_deltaLocY = -1;
         m_bInsideRoom = false;
         m_bEnteringDoor = false;
         m_bEaten = false;
         return;
      } 
         
      // A ghost will back off only if:
      // 1. It's not waiting to leave the room.
      // 2. It's not entering the door.
      // 3. It's not eaten.
      // 4. It's not leaving the room.
      // 5. Time to backoff is here.
      // 6. Insane AI is off
      if (m_gameModel.m_state == GameModel.STATE_PLAYING &&
          m_bInsideRoom == false &&
          m_bEnteringDoor == false &&
          m_bEaten == false &&
          (m_destinationX != m_gameModel.m_doorLocX && m_destinationY != m_gameModel.m_doorLocY - 1) &&
          (m_gameModel.m_pacMan.m_globalTickCount % m_gameModel.m_nTicks2Backoff) == 0 &&
          m_bInsaneAI == false)
      {
         m_destinationX = -1;   
         m_destinationY = -1;
         bBackoff = true;
      }
      
      // If there is a destination, then check if the destination has been reached.
      if (m_destinationX >= 0 && m_destinationY >= 0)
      {
         // Check if the destination has been reached, if so, then
         // get new destination.
         if (m_destinationX == m_locX &&
             m_destinationY == m_locY &&
             m_deltaLocX == 0 &&
             m_deltaLocY == 0)
         {
            m_destinationX = -1;
            m_destinationY = -1;
            prevDirection = m_direction;
         } else {
            // Otherwise, we haven't reached the destionation so
            // continue in same direction.
            return;
         }
      }

      // Reset the previous direction to allow backtracking
      if (bBackoff || (!m_bEaten && m_bCanBackTrack))
         prevDirection = STILL;
      
      // Get the next direction of the ghost.
      // This is where different AIs can be plugged.
      setNextDirection (prevDirection, bBackoff);
   }
   
 

}
