import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HighScoresModel implements Serializable{

	private static HashMap highScores = new HashMap<Integer, String>();

	public HighScoresModel() {

	}

	public boolean addHighScore(Integer score, String name) {
		boolean successfulAdd = false;

		if (isHighScore(score) && highScores.size() <= 10) {
			if (highScores.size() == 10) {
				removeLowestScore();
			}
			highScores.put(score, name);
			successfulAdd = true;
		}

		return successfulAdd;
	}

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

	private boolean removeLowestScore() {
		boolean done = false;
		TreeSet values = new TreeSet<Integer>();
		values.addAll(highScores.keySet());

		Integer lowestScore = (Integer) values.first();
		highScores.remove(lowestScore);
		done = true;

		return done;
	}

	public int numberOfHighScores() {
		return highScores.size();
	}

	public String[] forDrawingHighScores() {
		String[] highScoresArray = new String[highScores.size()];

		TreeSet allValues = new TreeSet<Integer>();
		allValues.addAll(highScores.keySet());
		Iterator<Integer> myIterator = allValues.iterator();
		int arrayCount = 0;

		while (myIterator.hasNext()) {
			Integer currentScore = myIterator.next();
			String currentName = (String) highScores.get(currentScore);
			highScoresArray[arrayCount] = currentName + "  " + currentScore;
			arrayCount++;
		}
		return highScoresArray;
	}

}