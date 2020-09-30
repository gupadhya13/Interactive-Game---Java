/**
 * 
 */
package hw3;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Gaurangi Upadhyay (gupadhya)
 *
 */
public class TwisterRound extends GameRound {
	private ObservableList<String> solutionsWordList = FXCollections.observableArrayList();
	private ObservableList<ObservableList<String>> submittedListsByWordLength = FXCollections.observableArrayList();
	private ObservableList<ObservableList<String>> solutionListsByWordLength = FXCollections.observableArrayList();

	TwisterRound() { // adding lists to solutionListsByWordLength and submittedListsByWordLength
		for (int i = 0; i <= (Twister.TWISTER_MAX_WORD_LENGTH - Twister.TWISTER_MIN_WORD_LENGTH); i++) {
			solutionListsByWordLength.add(FXCollections.observableArrayList());
		}

		for (int j = 0; j <= (Twister.TWISTER_MAX_WORD_LENGTH - Twister.TWISTER_MIN_WORD_LENGTH); j++) {
			submittedListsByWordLength.add(FXCollections.observableArrayList());
		}
	}

	public List<String> getSolutionWordsList() {
		return solutionsWordList;
	}

	public void setSolutionWordsList(List<String> solutionsWordList) {
		this.solutionsWordList = FXCollections.observableList(solutionsWordList);
	}

	public ObservableList<String> solutionWordsListProperty() {
		return solutionsWordList;
	}

	public ObservableList<ObservableList<String>> getSolutionListsByWordLength() {
		return solutionListsByWordLength;
	}

	public ObservableList<String> getSolutionListsByWordLength(int letterCount) {
		return solutionListsByWordLength.get(letterCount);
	}

	public void setSolutionListsByWordLength(String word) {
		solutionListsByWordLength.get(word.length() - Twister.TWISTER_MIN_WORD_LENGTH).add(word);
	}

	public ObservableList<ObservableList<String>> solutionListsByWordLengthProperty() {
		return solutionListsByWordLength;
	}

	public void setSubmittedListsByWordLength(String word) {
		submittedListsByWordLength.get(word.length() - Twister.TWISTER_MIN_WORD_LENGTH).add(word);
	}

	public ObservableList<ObservableList<String>> getSubmittedListsByWordLength() {
		return submittedListsByWordLength;
	}

	public ObservableList<String> getSubmittedListsByWordLength(int letterCount) {
		return submittedListsByWordLength.get(letterCount);
	}

	public ObservableList<ObservableList<String>> submittedListsByWordLengthProperty() {
		return submittedListsByWordLength;
	}
}
