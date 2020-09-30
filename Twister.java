/**
 * 
 */
package hw3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Gaurangi Upadhyay (gupadhya)
 *
 */
public class Twister extends Game {
	public static final int SOLUTION_LIST_COUNT = 5;
	public static final int TWISTER_MAX_WORD_LENGTH = 7;
	public static final int TWISTER_MIN_WORD_LENGTH = 3;
	public static final int NEW_WORD_BUTTON_INDEX = 0;
	public static final int TWIST_BUTTON_INDEX = 1;
	public static final int CLEAR_BUTTON_INDEX = 2;
	public static final int SUBMIT_BUTTON_INDEX = 3;
	public static final int CLUE_BUTTON_SIZE = 75;
	public static final int TWISTER_GAME_TIME = 120;
	public static final int MIN_SOLUTION_WORDCOUNT = 10;
	int count = 0;

	TwisterRound twisterRound;

	@Override
	TwisterRound setupRound() {
		twisterRound = new TwisterRound();
		twisterRound.setIsRoundComplete(false);
		count = 0;
		String puzzleWord = "";
		boolean flag = false;
		Random ran = new Random();
		// generating random numbers within the length of wordsFromFile;
		// if word at random index is within TWISTER_MIN_WORD_LENGTH and
		// TWISTER_MAX_WORD_LENGTH,
		// and has more than 10 words present in wordsFromFile, it becomes the
		// puzzleWord
		while (flag != true) {
			int randomIndex = ran.nextInt(WordNerdModel.wordsFromFile.length);
			if ((WordNerdModel.wordsFromFile[randomIndex].length() >= TWISTER_MIN_WORD_LENGTH)
					&& (WordNerdModel.wordsFromFile[randomIndex].length() <= TWISTER_MAX_WORD_LENGTH)) {
				int permutationCount = findSolutionWords(WordNerdModel.wordsFromFile[randomIndex]).size();
				if (permutationCount >= 10) {
					puzzleWord = WordNerdModel.wordsFromFile[randomIndex];
					flag = true;
				}
			}
		}
		twisterRound.setSolutionWordsList(findSolutionWords(puzzleWord));
		// adding values to getSolutionListsByWordLength
		// iterating through getSolutionWordsList and checking the length of every word;
		// adding the word to the (currentWordLength - TWISTER_MIN_WORD_LENGTH) index of
		// getSolutionListsByWordLength
		for (int j = 0; j < twisterRound.getSolutionWordsList().size(); j++) {
			int currentWordLength = twisterRound.getSolutionWordsList().get(j).length();
			twisterRound.getSolutionListsByWordLength().get(currentWordLength - TWISTER_MIN_WORD_LENGTH)
					.add(twisterRound.getSolutionWordsList().get(j));
		}
		makeAClue(puzzleWord);

		// Setters for setPuzzleWord, setClueWord
		twisterRound.setPuzzleWord(puzzleWord);
		twisterRound.setClueWord(makeAClue(puzzleWord.trim()));
		return twisterRound;

	}

	// checking occurrence of each character in a word; used in the
	// findSolutionWords function
	int occurences(String word, char ch) {
		int count = 0;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == ch) {
				count++;
			}
		}
		return count;
	}

	// function to check the number of words for the given word present in
	// wordsFromFile
	List<String> findSolutionWords(String incomingWord) {
		Map<Character, Integer> stringHash = new HashMap<Character, Integer>();
		List<String> ListOfSolutionWords = new ArrayList<String>();
		String inputWord = incomingWord;
		// creating a HashMap to store the occurrence of each character within
		// incomingWord
		for (char ch : inputWord.toCharArray()) {
			stringHash.put(ch, stringHash.containsKey(ch) ? (stringHash.get(ch) + 1) : 1);
		}

		// iterating through every word in the wordsFromFile; checking whether the
		// incomingWord is a part of the text file or not
		for (int i = 0; i < WordNerdModel.wordsFromFile.length; i++) {
			char[] fileWordToCharArr = WordNerdModel.wordsFromFile[i].trim().toCharArray();
			if (fileWordToCharArr.length >= 3) { // checking if the word from wordsFromFile is greater than 3
				String matchedWord = "";
				// iterating through the current word from wordsFromFile
				for (int j = 0; j < fileWordToCharArr.length; j++) { // if letter is being inserted in the string for
																		// the first time
					if (inputWord.contains(String.valueOf(fileWordToCharArr[j]))
							&& !matchedWord.contains(String.valueOf(fileWordToCharArr[j]))) {
						matchedWord = matchedWord + String.valueOf(fileWordToCharArr[j]);
					} else { // if letter already exists in the word matchedWord
						if (inputWord.contains(String.valueOf(fileWordToCharArr[j])) && (stringHash
								.get(fileWordToCharArr[j]) != occurences(matchedWord, fileWordToCharArr[j]))) {
							matchedWord = matchedWord + String.valueOf(fileWordToCharArr[j]);
						}
					}
				}
				// if matchedWord string length is equal to fileWordToCharArr, all characters in
				// matchedWord are also present in fileWordToCharArr; adding in the list
				if (matchedWord.length() == fileWordToCharArr.length) {
					ListOfSolutionWords.add(matchedWord);
				}
			}
		}
		Collections.sort(ListOfSolutionWords);

		return ListOfSolutionWords;
	}

	String makeAClue(String puzzleWord) {
		char puzzleWordArray[] = puzzleWord.toCharArray();
		Random ran = new Random();
		for (int i = 0; i < puzzleWordArray.length; i++) {
			int randomIndex = ran.nextInt(puzzleWordArray.length); // takes random integer within the length of
																	// puzzleWord
			// swapping current index of the puzzleWord with random indexes generated by
			// randomIndex
			char toSwap = puzzleWordArray[i];
			puzzleWordArray[i] = puzzleWordArray[randomIndex];
			puzzleWordArray[randomIndex] = toSwap;
		}
		String clueWord = String.valueOf(puzzleWordArray); // converting character array to String
		return clueWord;
	}

	int nextTry(String guess) {
		if (guess.length() >= TWISTER_MIN_WORD_LENGTH) {/*if incoming word length is greater than or equal to
														TWISTER_MIN_WORD_LENGTH*/
			if (twisterRound.getSolutionWordsList().contains(guess)) { // if getSolutionWordsList contains guess
				if (!twisterRound.getSubmittedListsByWordLength(guess.length() - TWISTER_MIN_WORD_LENGTH)
						.contains(guess)) {// if getSubmittedListsByWordLength does not contain the guess word; adding
											// the word in the list
					twisterRound.getSubmittedListsByWordLength().get(guess.length() - TWISTER_MIN_WORD_LENGTH)
							.add(guess);
					count++; // counting the number of times a word has been entered in the
								// getSubmittedListsByWordLength list
					if (count == twisterRound.getSolutionWordsList().size()) {
						count = 0; // all words have been guessed;return SMILEY_INDEX
						return (GameView.SMILEY_INDEX);
					}
					return (GameView.THUMBS_UP_INDEX);// if the guess word is a part of the SolutionWordsList, return
														// THUMBS_UP_INDEX
				} else { // if input word has already been added to the submitted words list
					return (GameView.REPEAT_INDEX);
				}
			} else { // if input word is not a part of the solutions word list
				return (GameView.THUMBS_DOWN_INDEX);
			}
		} else {// if input word is less than the minimum size
			return (GameView.THUMBS_DOWN_INDEX);
		}
	}

	String getScoreString() {
		int remainingWordsToGuess = 0;
		// counting total number of words present in getSubmittedListsByWordLength list;
		// storing the value in remainingWordsToGuess
		for (int i = 0; i < twisterRound.getSubmittedListsByWordLength().size(); i++) {
			remainingWordsToGuess = remainingWordsToGuess + twisterRound.getSubmittedListsByWordLength().get(i).size();
		}
		// subtracting remainingWordsToGuess with total number of words in
		// getSolutionWordsList
		String topMessageString = "Twist to find "
				+ (twisterRound.getSolutionWordsList().size() - remainingWordsToGuess) + " of "
				+ twisterRound.getSolutionWordsList().size() + " words";
		return topMessageString;
	}

}
