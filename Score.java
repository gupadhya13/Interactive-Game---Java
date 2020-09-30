/**
 * 
 */
package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Gaurangi Upadhyay (gupadhya)
 *
 */
public class Score {
	private IntegerProperty gameId = new SimpleIntegerProperty();
	private StringProperty puzzleWord = new SimpleStringProperty();
	private IntegerProperty timeStamp = new SimpleIntegerProperty();
	private FloatProperty score = new SimpleFloatProperty();

	public Score(int gameId, String puzzleWord, int timeStamp, float score) {
		this.gameId.set(gameId);
		this.puzzleWord.set(puzzleWord);
		this.timeStamp.set(timeStamp);
		this.score.set(score);
	}

	// Getters and Setters for gameId
	public void setgameId(int gameId) {
		this.gameId.set(gameId);
	}

	public int getGameId() {
		return gameId.get();
	}

	public IntegerProperty gameIdProperty() {
		return gameId;
	}

	// Getters and Setters for puzzleWord
	public void setPuzzleWord(String puzzleWord) {
		this.puzzleWord.set(puzzleWord);
	}

	public String getPuzzleWord() {
		return puzzleWord.get();
	}

	public StringProperty puzzleWordProperty() {
		return puzzleWord;
	}

	// Getters and Setters for timeStamp
	public void setTimeStamp(int timeStamp) {
		this.timeStamp.set(timeStamp);
	}

	public int getTimeStamp() {
		return timeStamp.get();
	}

	public IntegerProperty timeStampProperty() {
		return timeStamp;
	}

	// Getters and Setters for sore
	public void setScore(float score) {
		this.score.set(score);
	}

	public float getScore() {
		return score.get();
	}

	public FloatProperty scoreProperty() {
		return score;
	}

}
