/**
 * 
 */
package hw3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Gaurangi Upadhyay (gupadhya)
 *
 */
public class WordNerdModel {

	static String[] wordsFromFile;
	public static final String WORDS_FILE_NAME = "data\\wordsFile.txt";
	public static final String SCORE_FILE_NAME = "data\\scores.csv";
	ObservableList<Score> scoreList;

	static String readWordsFile(String wordsFileName) {
		String strToReturn = "";
		StringBuilder readFile = new StringBuilder();
		Scanner fileScanner = null;
		try {
			File file = new File(wordsFileName);
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileScanner.useDelimiter("\n").hasNext()) {
			readFile.append(fileScanner.next() + "\n");
		}
		wordsFromFile = readFile.toString().split("\n");
		/*
		 * If the chosen file is empty, throws an exception and control goes back to
		 * previously selected file
		 */
		try {
			if (readFile.toString().length() == 0) {
				strToReturn = "badWordSource.txt";
				readWordsFile(WordNerd.text);
				throw new InvalidWordSourceException("Check word source format");
			}
			/*
			 * If the chosen file contains characters which are not letters, throws an
			 * exception and control goes back to previously selected file
			 */
			for (int i = 0; i < wordsFromFile.length; i++) {
				char[] chars = wordsFromFile[i].trim().toCharArray();
				for (char c : chars) {
					if (!Character.isLetter(c)) {
						strToReturn = "badWordSource.txt";
						readWordsFile(WordNerd.text);
						throw new InvalidWordSourceException("Check word source format");
					}
				}
			}
		} catch (InvalidWordSourceException ex) {
			ex.showAlert();
		}
		return strToReturn;
	}

	/* appends the incoming scoreString to SCORE_FILE_NAME */
	public void writeScore(String scoreString) {
		try {
			scoreString = scoreString.replace("\n", "");
			BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE_NAME, true));
			writer.write(scoreString);
			writer.newLine();
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* reads entries of SCORE_FILE_NAME and adds each entry to scoreList */
	public void readScore() {
		scoreList = FXCollections.observableArrayList();
		StringBuilder readFile = new StringBuilder();
		Scanner fileScanner = null;
		try {
			File file = new File(SCORE_FILE_NAME);
			fileScanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileScanner.useDelimiter("\n").hasNext()) {
			readFile.append(fileScanner.next() + "\n");
		}
		String[] wordsFromCSV = readFile.toString().split("\n");
		for (int i = 0; i < wordsFromCSV.length; i++) {
			Score score = new Score(Integer.parseInt(wordsFromCSV[i].split(",")[0]), wordsFromCSV[i].split(",")[1],
					Integer.parseInt(wordsFromCSV[i].split(",")[2]), Float.parseFloat(wordsFromCSV[i].split(",")[3]));
			scoreList.add(score);
		}

	}

}
