/**
 * 
 */
package hw3;

import java.util.Collections;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Gaurangi Upadhyay (gupadhya)
 *
 */

/**
 * startController() creates new twister, TwisterView, invokes setupRound() to
 * create a new twisterRound, refreshes the view for next round, and invokes
 * setupBindings to bind the new twisterRound properties with GUI components.
 */
public class TwisterController extends WordNerdController {
	TwisterView twisterView;
	Twister twister;

	public void startController() {
		twisterView = new TwisterView();
		twister = new Twister();
		twister.twisterRound = twister.setupRound();
		twisterView.refreshGameRoundView(twister.twisterRound);
		setupBindings();

		VBox lowerPanel = new VBox();
		lowerPanel.getChildren().add(twisterView.bottomGrid);
		lowerPanel.getChildren().add(WordNerd.exitButton);
		lowerPanel.setAlignment(Pos.CENTER);

		WordNerd.root.setTop(twisterView.topMessageText);
		WordNerd.root.setCenter(twisterView.topGrid);
		WordNerd.root.setBottom(lowerPanel);

	}

	public void setupBindings() {
		// setting topMessageText
		twisterView.topMessageText
				.setText("Twist to find " + twister.twisterRound.getSolutionWordsList().size() + " words");
		twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[GameView.SMILEY_INDEX]);
		// Adding ButtonHandlers to playButtons
		twisterView.playButtons[0].setOnAction(new NewButtonHandler());
		twisterView.playButtons[1].setOnAction(new TwistButtonHandler());
		twisterView.playButtons[2].setOnAction(new ClearButtonHandler());
		twisterView.playButtons[3].setOnAction(new SubmitButtonHandler());
		// Adding ButtonHandler to clueButtons
		for (int i = 0; i < twisterView.clueButtons.length; i++) {
			twisterView.clueButtons[i].setOnAction(new ClueButtonHandler());
		}
		// Adding ButtonHandler to answerButtons
		for (int i = 0; i < twisterView.answerButtons.length; i++) {
			twisterView.answerButtons[i].setOnAction(new AnswerButtonHandler());
		}
		// Adding ButtonHandler to solutionListViews
		for (int i = 0; i < twisterView.solutionListViews.length; i++) {
			twisterView.solutionListViews[i].getItems().clear();
		}
		/* If a round is complete, adds the details of that round(GameID, PuzzleWord, Time, Score) to scoreString;
		 * passes the scoreString to wordNerdModel.writeScore to update SCORE_FILE_NAME*/
		twister.twisterRound.isRoundCompleteProperty().addListener((observable, oldValue, newValue) -> {
			int wordsGuessed = 0;
			String scoreString  = "";
			if(twister.twisterRound.getIsRoundComplete()== true){
				String puzzleWord = twister.twisterRound.getPuzzleWord();
			    puzzleWord = puzzleWord.replace("\n", "").replace("\r", "");
			    //counts the number of words correctly guessed
			    for (int i = 0; i < twister.twisterRound.getSubmittedListsByWordLength().size(); i++) {
			    	wordsGuessed = wordsGuessed + twister.twisterRound.getSubmittedListsByWordLength().get(i).size();
				}
			    scoreString = WordNerd.TWISTER_ID + "," + puzzleWord + "," + (120 - Integer.parseInt(GameView.wordTimer.timerButton.getText())) + "," + wordsGuessed/(float)twister.twisterRound.getSolutionWordsList().size();
			    wordNerdModel.writeScore(scoreString);
			}
		});
		
		// Adding wordLengthLabels, solutionListViews, wordScoreLabels to bottomGrid
		// Setting text in wordScoreLabels
		for (int i = 0; i < twister.twisterRound.getSolutionListsByWordLength().size(); i++) {
			if (twister.twisterRound.getSolutionListsByWordLength().get(i).size() > 0) {
				twisterView.bottomGrid.add(twisterView.wordLengthLabels[i], 0, i);
				twisterView.bottomGrid.add(twisterView.solutionListViews[i], 1, i);
				twisterView.bottomGrid.add(twisterView.wordScoreLabels[i], 2, i);
				int start = 0;
				twisterView.wordScoreLabels[i]
						.setText(start + "/" + twister.twisterRound.getSolutionListsByWordLength().get(i).size());
			}
		}
		// Bind a listener to twisterRound's clueWordProperty
		// so that whenever it changes, the clueButtons should also
		// change in TwisterView
		twister.twisterRound.clueWordProperty().addListener((observable, oldValue, newValue) -> {
			for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++) {
				twisterView.clueButtons[i].setText(String.valueOf(newValue.charAt(i)));
			}
		});
		// When timer runs out, set smiley to sadly, isRoundComplete to true
		GameView.wordTimer.timeline.setOnFinished(event -> {
			twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[GameView.SADLY_INDEX]);
			twister.twisterRound.setIsRoundComplete(true);
		});
		// Disabling answerButtons, clueButtons and playButtons when round is complete
		for (int i = 0; i < twisterView.answerButtons.length; i++) {
			twisterView.answerButtons[i].disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
			twisterView.clueButtons[i].disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
		}
		for (int i = 1; i < twisterView.playButtons.length; i++) {
			twisterView.playButtons[i].disableProperty().bind(twister.twisterRound.isRoundCompleteProperty());
		}
	}

	// taking every non empty character from clueButtons,
	// appending to StringBuilder, clearing text of clueButtons,
	// passing value of StringBuilder to makeAClue()
	// setting value of newClue to clueButtons in twisterView
	class TwistButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			StringBuilder stringToTwist = new StringBuilder();
			for (int i = 0; i < twisterView.clueButtons.length; i++) {
				if (!twisterView.clueButtons[i].getText().isEmpty()) {
					stringToTwist.append(twisterView.clueButtons[i].getText());
					twisterView.clueButtons[i].setText("");
				}
			}
			String newClue = twister.makeAClue(stringToTwist.toString());
			for (int i = 0; i < newClue.length(); i++) {
				twisterView.clueButtons[i].setText(String.valueOf(newClue.charAt(i)));
			}
		}
	}

	// If button in clueButtons is clicked, adding the character in that button
	// to first available empty position from left of answerButtons;
	// clearing the text inside clicked clueButtons
	class ClueButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Button b = (Button) event.getSource();
			for (int i = 0; i < twisterView.answerButtons.length; i++) {
				if (twisterView.answerButtons[i].getText().equals("")) {
					twisterView.answerButtons[i].setText(b.getText());
					break;
				}
			}
			b.setText("");
		}
	}

	// If button in answerButtons is clicked, adding the character in that button
	// to first available empty position from left of clueButtons;
	// clearing the text inside clicked answerButtons
	class AnswerButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Button b = (Button) event.getSource();
			for (int i = 0; i < twisterView.clueButtons.length; i++) {
				if (twisterView.clueButtons[i].getText().equals("")) {
					twisterView.clueButtons[i].setText(b.getText());
					break;
				}
			}
			b.setText("");
		}
	}

	// If clear Button is pressed, taking each character from answerButtons
	// and adding it to the first empty position from left of clueButtons
	class ClearButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String current = "";
			for (int j = 0; j < twisterView.answerButtons.length; j++) {
				if (!twisterView.answerButtons[j].getText().equals("")) {
					current = twisterView.answerButtons[j].getText();
					twisterView.answerButtons[j].setText("");
					for (int i = 0; i < twisterView.clueButtons.length; i++) {
						if (twisterView.clueButtons[i].getText().equals("")) {
							twisterView.clueButtons[i].setText(current);
							break;
						}
					}
				}
			}
		}
	}

	class SubmitButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			String finalAnswer = "";
			// getting the word in answerButtons
			for (int i = 0; i < twisterView.answerButtons.length; i++) {
				if (!twisterView.answerButtons[i].getText().equals("")) {
					finalAnswer = finalAnswer + twisterView.answerButtons[i].getText();
				}
			}
			// calling nextTry function, storing value in returnedIndex; displaying graphic
			// according to returnedIndex
			int returnedIndex = twister.nextTry(finalAnswer);
			twisterView.smileyButton.setGraphic(twisterView.smileyImageViews[returnedIndex]);

			int wordLength = 0;
			// if returnedIndex is THUMBS_UP_INDEX or SMILEY_INDEX
			// clearing the text in answerButtons, clueButtons
			// Updating topMessageText in twisterView
			// adding word in answerButtons to twisterView in sorted order
			// setting text in wordScoreLabels on right hand side
			if (returnedIndex == GameView.THUMBS_UP_INDEX || returnedIndex == GameView.SMILEY_INDEX) {
				for (int i = 0; i < twisterView.answerButtons.length; i++) {
					twisterView.answerButtons[i].setText("");
				}

				for (int i = 0; i < twister.twisterRound.getClueWord().length(); i++) {
					twisterView.clueButtons[i]
							.setText(Character.toString(twister.twisterRound.getClueWord().charAt(i)));
				}
				wordLength = finalAnswer.length();
				twisterView.solutionListViews[wordLength - Twister.TWISTER_MIN_WORD_LENGTH].getItems().add(finalAnswer);
				twisterView.topMessageText.setText(twister.getScoreString());
				Collections.sort(twisterView.solutionListViews[wordLength - Twister.TWISTER_MIN_WORD_LENGTH].getItems());
				twisterView.wordScoreLabels[wordLength - Twister.TWISTER_MIN_WORD_LENGTH].setText(twister.twisterRound
						.getSubmittedListsByWordLength().get(wordLength - Twister.TWISTER_MIN_WORD_LENGTH).size() + "/"
						+ twister.twisterRound.getSolutionListsByWordLength().get(wordLength - Twister.TWISTER_MIN_WORD_LENGTH).size());
			}

			// if returned index is SMILEY_INDEX i.e. all words correctly guessed;
			// disabling answerButtons and clueButtons,
			// disabling all playButtons except "New Word" play button,
			// stopping the timer, setting setIsRoundComplete to true
			if (returnedIndex == GameView.SMILEY_INDEX) {
				GameView.wordTimer.timeline.stop();
				twister.twisterRound.setIsRoundComplete(true);
			}
		}
	}

	// if New Word is requested, restart game time, call refreshGameRoundView() and
	// setupBindings() to start a new round
	class NewButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			twister.twisterRound = twister.setupRound();
			twisterView.refreshGameRoundView(twister.twisterRound);
			GameView.wordTimer.restart(Twister.TWISTER_GAME_TIME);
			setupBindings();
		}
	}
}
