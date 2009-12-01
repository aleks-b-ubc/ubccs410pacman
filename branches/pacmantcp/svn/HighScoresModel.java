package branches.pacmantcp.svn;
import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HighScoresModel implements Serializable{
	
	// HashMap Data Structure used to maintain list of high scores
	private static HashMap highScores = new HashMap<Integer, String>();

	public HighScoresModel() {

	}

	// Adding a high score
	public boolean addHighScore(Integer score, String name) {
		boolean successfulAdd = false;

		// Checks to see if it is indeed a high score before adding
		if (isHighScore(score) && highScores.size() <= 10) {
			if (highScores.size() == 10) {
				removeLowestScore();
			}
			highScores.put(score, name);
			successfulAdd = true;
		}

		return successfulAdd;
	}

	// Helper method to check for high score validity
	private boolean isHighScore(Integer score) throws NoSuchElementException {
		boolean highScore = false;

		TreeSet values = new TreeSet<Integer>();
		values.addAll(highScores.keySet());

		try {

			Integer lowestScore = (Integer) values.first();

			if (score > lowestScore || highScores.size() < 10) {
				highScore = true;
			}
		} catch (NoSuchElementException e) {
			highScore = true;
		}

		return highScore;
	}

	// Helper method to remove the lowest score if list is full
	private boolean removeLowestScore() {
		boolean done = false;
		TreeSet values = new TreeSet<Integer>();
		values.addAll(highScores.keySet());

		Integer lowestScore = (Integer) values.first();
		highScores.remove(lowestScore);
		done = true;

		return done;
	}

	// Returns the current number of high scores
	public int numberOfHighScores() {
		return highScores.size();
	}

	// Method of helping paint the high scores
	public String[] forDrawingHighScores() {
		String[] highScoresArray = new String[highScores.size()];

		TreeSet allValues = new TreeSet<Integer>();
		allValues.addAll(highScores.keySet());
		Iterator<Integer> myIterator = allValues.iterator();
		int arrayCount = 0;
		
		// Associate score with user
		while (myIterator.hasNext()) {
			Integer currentScore = myIterator.next();
			String currentName = (String) highScores.get(currentScore);
			highScoresArray[arrayCount] = currentName + "  " + currentScore;
			arrayCount++;
		}
		return highScoresArray;
	}

}