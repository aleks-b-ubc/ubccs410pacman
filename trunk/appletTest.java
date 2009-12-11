import java.applet.Applet;
import java.awt.GridLayout;

import javax.swing.JFrame;


public class appletTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create the frame this applet will run in
        JFrame appletFrame = new JFrame("Some applet");

//The frame needs a layout manager, use the GridLayout to maximize
//the applet size to the frame.
        appletFrame.setLayout(new GridLayout(1,0));

//Have to give the frame a size before it is visible
        appletFrame.setSize(610, 500);

//Make the frame appear on the screen. You should make the frame appear
//before you call the applet's init method. On some Java implementations,
//some of the graphics information is not available until there is a frame.
//If your applet uses certain graphics functions like getGraphics() in the
//init method, it may fail unless there is a frame already created and
//showing.
        appletFrame.setVisible(true);
        appletFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



//Create an instance of the applet
        Applet myApplet = new PacMan();

//Add the applet to the frame
        appletFrame.add(myApplet);

//Initialize and start the applet
        myApplet.init();
        myApplet.start();


	}

}
