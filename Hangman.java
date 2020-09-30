//Gaurangi Upadhyay (gupadhya)
package hw3;

import java.util.Random;

/**
 * Gaurangi Upadhyay (gupadhya)
 *
 */

public class Hangman extends Game {
	static final int MIN_WORD_LENGTH = 5; // minimum length of puzzle word
	static final int MAX_WORD_LENGTH = 10; // maximum length of puzzle word
	static final int HANGMAN_TRIALS = 10; // max number of trials in a game
	static final int HANGMAN_GAME_TIME = 30; // max time in seconds for one round of game

	HangmanRound hangmanRound;
	HangmanController hangmanController;

	/**
	 * setupRound() is a replacement of findPuzzleWord() in HW1. It returns a new
	 * HangmanRound instance with puzzleWord initialized randomly drawn from
	 * wordsFromFile. The puzzleWord must be a word between HANGMAN_MIN_WORD_LENGTH
	 * and HANGMAN_MAX_WORD_LEGTH. Other properties of HangmanRound are also
	 * initialized here.
	 */
	@Override
	HangmanRound setupRound() {
		hangmanRound = new HangmanRound();
		String puzzleWord = null;
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < WordNerdModel.wordsFromFile.length; i++) {
			if ((WordNerdModel.wordsFromFile[i].trim().length() >= MIN_WORD_LENGTH)
					&& (WordNerdModel.wordsFromFile[i].trim().length() <= MAX_WORD_LENGTH)) {
				sb.append(WordNerdModel.wordsFromFile[i] + "\n");
			}
		}
		String[] fileterdStrArray = sb.toString().split("\n");
		Random ran = new Random();
		puzzleWord = fileterdStrArray[ran.nextInt(fileterdStrArray.length)];

		hangmanRound.setPuzzleWord(puzzleWord);
		hangmanRound.setClueWord(makeAClue(hangmanRound.getPuzzleWord()));
		return hangmanRound;
	}

	/**
	 * Returns a clue that has at least half the number of letters in puzzleWord
	 * replaced with dashes. The replacement should stop as soon as number of dashes
	 * equals or exceeds 50% of total word length. Note that repeating letters will
	 * need to be replaced together. For example, in 'apple', if replacing p, then
	 * both 'p's need to be replaced to make it a--le
	 */
	@Override
	String makeAClue(String puzzleWord) {
		int dashCount = 0;
		String[] strArr = puzzleWord.split("");
		int randomNum = 0;
		while (dashCount <= (puzzleWord.length() / 2)) {
			Random ran = new Random();
			randomNum = ran.nextInt(puzzleWord.length() - 1);
			puzzleWord = puzzleWord.replaceAll(strArr[randomNum], "_");
			dashCount = countDashes(puzzleWord);
		}
		return puzzleWord;
	}

	/** countDashes() returns the number of dashes in a clue String */
	int countDashes(String word) {
		int DashCount = 0;
		DashCount = word.length() - word.replace("_", "").length();
		return DashCount;
	}

	/**
	 * getScoreString() returns a formatted String with calculated score to be
	 * displayed after each trial in Hangman. See the handout and the video clips
	 * for specific format of the string.
	 */

	@Override
	String getScoreString() {
		hangmanController = new HangmanController();
		String Gamescore = "";
		int hitCount = hangmanRound.getHitCount();
		int missCount = hangmanRound.getMissCount();
		if (hangmanRound.getMissCount() == 0) {
			Gamescore = String.valueOf(hitCount);
			return ("Hit:" + hitCount + " Miss:" + missCount + " Score: " + Gamescore);
		} else {
			Gamescore = String.valueOf(hitCount / (float) missCount);
			return ("Hit:" + hitCount + " Miss:" + missCount + " Score: " + Gamescore);
		}
	}

	/**
	 * nextTry() takes next guess and updates hitCount, missCount, and clueWord in
	 * hangmanRound. Returns INDEX for one of the images defined in GameView
	 * (SMILEY_INDEX, THUMBS_UP_INDEX...etc. The key change from HW1 is that because
	 * the keyboardButtons will be disabled after the player clicks on them, there
	 * is no need to track the previous guesses made in userInputs
	 */
	@Override
	int nextTry(String guess) {
		int hitCount = hangmanRound.getHitCount();
		int MissCount = hangmanRound.getMissCount();
		String clueWord = hangmanRound.getClueWord();

		if (hangmanRound.getClueWord().contains(guess)) {
			return 3;
		}

		if ((hitCount + MissCount) > 9 && (!hangmanRound.getClueWord().equals(hangmanRound.getPuzzleWord()))) {
			return 4;
		}

		if (hangmanRound.getPuzzleWord().contains(guess)) {
			hitCount++; // update hitCount every time guessed character is a part of puzzle word
			hangmanRound.setHitCount(hitCount);
			char[] clueWordArr = hangmanRound.getClueWord().toCharArray();
			char[] puzzleWordArr = hangmanRound.getPuzzleWord().toCharArray();
			for (int i = 0; i < puzzleWordArr.length; i++) {
				if (puzzleWordArr[i] == guess.charAt(0)) {
					clueWordArr[i] = guess.charAt(0);
					clueWord = String.valueOf(clueWordArr);
					hangmanRound.setClueWord(clueWord);
				}
			}
			// when the clue-word has been correctly guessed
			if (hangmanRound.getClueWord().equals(hangmanRound.getPuzzleWord())) {
				hangmanRound.setIsRoundComplete(true);
				return 0;
			}
			return 1;
		}
		// guess character is not a part of the clue word
		else {
			MissCount++; // update miss count if guessed character is not a part of the puzzle word
			hangmanRound.setMissCount(MissCount);
			return 2;
		}

	}
}
