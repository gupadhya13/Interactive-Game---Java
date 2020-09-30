//Gaurangi Upadhyay (gupadhya)
package hw3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SearchController extends WordNerdController {
	SearchView searchView;
	WordNerdModel wordNerdModel = new WordNerdModel();
	ObservableList<Score> ScoreList;
	ObservableList<Score> hangmanScoreList;
	ObservableList<Score> twisterScoreList;

	@Override
	public void startController() {
		searchView = new SearchView();
		searchView.setupView();
		setupBindings();
		// to be implemented in HW3
	}

	@Override
	public void setupBindings() {
		// adding values to combo-box; Selecting All Games as the default table-view
		ObservableList<String> gameName = FXCollections.observableArrayList();
		gameName.add("All Games");
		gameName.add("Hangman");
		gameName.add("Twister");
		searchView.gameComboBox.setItems(gameName);
		searchView.gameComboBox.getSelectionModel().selectFirst();

		searchView.gameComboBox.setOnAction(new FilterGamesButtonHandler());
		searchView.searchTextField.setOnAction(new FilterGamesButtonHandler());

		// Listener to filter searchTableView according to the text entered in
		// searchTextField
		searchView.searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			wordNerdModel.readScore();
			/*
			 * checking which game has been selected in the combo-box and displaying scores
			 * of that game in searchTableView
			 */
			if (searchView.gameComboBox.getSelectionModel().getSelectedItem().equals("Hangman")) {
				searchView.searchTableView.setItems(hangmanScoreList);
			} else if (searchView.gameComboBox.getSelectionModel().getSelectedItem().equals("Twister")) {
				searchView.searchTableView.setItems(twisterScoreList);
			} else if (searchView.gameComboBox.getSelectionModel().getSelectedItem().equals("All Games")) {
				searchView.searchTableView.setItems(wordNerdModel.scoreList);
			}

			/*
			 * if searchTextField is not blank, filtering searchTableView according to the
			 * text entered in searchTextField
			 */
			if (!searchView.searchTextField.getText().isBlank()) {
				ScoreList = FXCollections.observableArrayList();
				char[] wordToChar = newValue.toCharArray(); // breaks searchTableView into character array
				for (int j = 0; j < searchView.searchTableView.getItems().size(); j++) {
					int count = 0;
					String currentWord = searchView.searchTableView.getItems().get(j).getPuzzleWord();
					for (int i = 0; i < wordToChar.length; i++) {
						if (currentWord.contains(String.valueOf(wordToChar[i]))) { // if word in currentWord contains
																					// given char;
																					// incrementing count.
							count++;
						}
						if (count == wordToChar.length) { // if count is equal to the length of char array;
															// all elements of char array are present in word;
															// adding the word to ScoreList
							ScoreList.add(searchView.searchTableView.getItems().get(j));
						}
					}
				}
				searchView.searchTableView.setItems(ScoreList); //setting the searchTableView with ScoreList
			}
		});
	}

	// Filters table-view according to the game selected in the combo-box
	class FilterGamesButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			wordNerdModel.readScore();
			hangmanScoreList = FXCollections.observableArrayList();
			twisterScoreList = FXCollections.observableArrayList();
			// iterating through scoreList in WordNerdModel to make two separate lists:
			// hangmanScoreList and twisterScoreList
			for (int i = 0; i < wordNerdModel.scoreList.size(); i++) {
				if (wordNerdModel.scoreList.get(i).getGameId() == 0) {
					hangmanScoreList.add(wordNerdModel.scoreList.get(i));
				} else {
					twisterScoreList.add(wordNerdModel.scoreList.get(i));
				}
			}

			// if value inside combo-box is "Hangman" and the search field is not blank;
			// filter on the basis of text inside search field
			if (searchView.gameComboBox.getSelectionModel().getSelectedItem().equals("Hangman")) {
				searchView.searchTableView.setItems(hangmanScoreList);
				if (!searchView.searchTextField.getText().isBlank()) {
					searchView.searchTableView.setItems(hangmanScoreList);
					ScoreList = FXCollections.observableArrayList();
					char[] wordToChar = searchView.searchTextField.getText().toCharArray();
					for (int j = 0; j < searchView.searchTableView.getItems().size(); j++) {
						int count = 0;
						String currentWord = searchView.searchTableView.getItems().get(j).getPuzzleWord();
						for (int i = 0; i < wordToChar.length; i++) {
							if (currentWord.contains(String.valueOf(wordToChar[i]))) {/*if word in currentWord contains
								                                                        given char;
								                                                        incrementing count.*/
								count++;
							}
							if (count == wordToChar.length) { // if count is equal to the length of char array;
															// all elements of char array are present in word;
															// adding the word to ScoreList
								ScoreList.add(searchView.searchTableView.getItems().get(j));
							}
						}
					}
					searchView.searchTableView.setItems(ScoreList); //setting the searchTableView with ScoreList
				}
			}
			// if twister is chosen from combo-box and the search field is not empty; filter
			// on the basis of search field
			else if (searchView.gameComboBox.getValue().equals("Twister")) {
				searchView.searchTableView.setItems(twisterScoreList);
				if (!searchView.searchTextField.getText().isBlank()) {
					ScoreList = FXCollections.observableArrayList();
					char[] wordToChar = searchView.searchTextField.getText().toCharArray();
					for (int j = 0; j < searchView.searchTableView.getItems().size(); j++) {
						int count = 0;
						String currentWord = searchView.searchTableView.getItems().get(j).getPuzzleWord();
						for (int i = 0; i < wordToChar.length; i++) {
							if (currentWord.contains(String.valueOf(wordToChar[i]))) {/*if word in currentWord contains
                                														given char;
                                														incrementing count.*/
								count++;
							}
							if (count == wordToChar.length) {// if count is equal to the length of char array;
															// all elements of char array are present in word;
															// adding the word to ScoreList
								ScoreList.add(searchView.searchTableView.getItems().get(j));
							}
						}
					}
					searchView.searchTableView.setItems(ScoreList);//setting the searchTableView with ScoreList
				}
			}
			// if "All Games" is chosen from combo-box and the search field is not empty;
			// filter on the basis of search field
			else if (searchView.gameComboBox.getValue().equals("All Games")) {
				searchView.searchTableView.setItems(wordNerdModel.scoreList);
				if (!searchView.searchTextField.getText().isBlank()) {
					ScoreList = FXCollections.observableArrayList();
					char[] wordToChar = searchView.searchTextField.getText().toCharArray();
					for (int j = 0; j < searchView.searchTableView.getItems().size(); j++) {
						int count = 0;
						String currentWord = searchView.searchTableView.getItems().get(j).getPuzzleWord();
						for (int i = 0; i < wordToChar.length; i++) {
							if (currentWord.contains(String.valueOf(wordToChar[i]))) {/*if word in currentWord contains
												                                      given char;
																					  incrementing count.*/
								count++;
							}
							if (count == wordToChar.length) {// if count is equal to the length of char array;
															// all elements of char array are present in word;
															// adding the word to ScoreList
								ScoreList.add(searchView.searchTableView.getItems().get(j));
							}
						}
					}
					searchView.searchTableView.setItems(ScoreList); //setting the searchTableView with ScoreList
				}
			}
		}
	}
}
