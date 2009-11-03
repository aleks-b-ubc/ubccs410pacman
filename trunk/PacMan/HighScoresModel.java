
import java.util.HashMap;
import java.util.TreeSet;

public class HighScoresModel {

	private static HashMap highScores = new HashMap<Integer, String>();

	public boolean addHighScore(Integer score, String name) {
		boolean successfulAdd = false;
		
		if( isHighScore(score) && highScores.size() <= 10 )
		{
			if( highScores.size() == 10 )
			{
				removeLowestScore();
			}
			highScores.put(score, name);
			successfulAdd = true;
		}

		return successfulAdd;
	}

	private boolean isHighScore(Integer score) {
		boolean highScore = false;
		
		TreeSet values = new TreeSet<Integer>();
		values.addAll(highScores.keySet());
		
		Integer lowestScore = (Integer) values.first();
		
		if ( score > lowestScore )
		{
			highScore = true;
		}

		return highScore;
	}
	
	private boolean removeLowestScore()
	{
		boolean done = false;
		TreeSet values = new TreeSet<Integer>();
		values.addAll(highScores.keySet());
		
		Integer lowestScore = (Integer) values.first();
		highScores.remove(lowestScore);
		done = true;
		
		
		return done;
	}

}

