//Gaurangi Upadhyay (gupadhya)
package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class SearchView {

	ComboBox<String> gameComboBox = new ComboBox<>(); // shows drop down for filtering the tableView data
	TextField searchTextField = new TextField(); // for entering search letters
	TableView<Score> searchTableView = new TableView<>(); // displays data from scores.csv
	Callback<CellDataFeatures<Score, String>, ObservableValue<String>> gameNameCallback; // displays game name according
																							// to gameId
	Callback<CellDataFeatures<Score, String>, ObservableValue<String>> scoreCallback; // rounds of game score to 2
																						// decimal places

	/**
	 * setupView() sets up the GUI components for Search functionality
	 */
	void setupView() {

		VBox searchVBox = new VBox(); // searchVBox contains searchLabel and searchHBox
		Text searchLabel = new Text("Search");
		searchVBox.getChildren().add(searchLabel);

		HBox searchHBox = new HBox(); // searchHBox contain gameComboBox and searchTextField
		searchHBox.getChildren().add(gameComboBox);
		searchHBox.getChildren().add(new Text("Search letters"));
		searchHBox.getChildren().add(searchTextField);
		searchVBox.getChildren().add(searchHBox);

		searchLabel.setStyle("-fx-font: 30px Tahoma;"
				+ " -fx-fill: linear-gradient(from 0% 50% to 50% 100%, repeat, lightgreen 0%, lightblue 50%);"
				+ " -fx-stroke: gray;" + " -fx-background-color: gray;" + " -fx-stroke-width: 1;");
		searchHBox.setPrefSize(WordNerd.GAME_SCENE_WIDTH, WordNerd.GAME_SCENE_HEIGHT / 3);
		gameComboBox.setPrefWidth(200);
		searchTextField.setPrefWidth(300);
		searchHBox.setAlignment(Pos.CENTER);
		searchVBox.setAlignment(Pos.CENTER);
		searchHBox.setSpacing(10);

		setupSearchTableView();

		WordNerd.root.setPadding(new Insets(10, 10, 10, 10));
		WordNerd.root.setTop(searchVBox);
		WordNerd.root.setCenter(searchTableView);
		WordNerd.root.setBottom(WordNerd.exitButton);
	}

	@SuppressWarnings({ "unchecked" })
	void setupSearchTableView() {
		WordNerdModel wordNerdModel = new WordNerdModel();
		// Setting the initial text of gameComboBox
		gameComboBox.setPromptText("All Games");

		// reading all entries from the scores.csv file and adding them to
		// searchTableView
		wordNerdModel.readScore();
		searchTableView.setItems(wordNerdModel.scoreList);

		/*
		 * creating a gameNameCallback; Checks the gameId of all entries in
		 * gameNameCallback; assigns "Hangman" to entries with gameId = 0 and "Twister"
		 * to entries with gameId = 1
		 */
		gameNameCallback = new Callback<CellDataFeatures<Score, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Score, String> gameName) {
				if (gameName.getValue().getGameId() == 0) {
					return (new SimpleStringProperty("Hangman"));
				} else {
					return (new SimpleStringProperty("Twister"));
				}
			}
		};

		/*
		 * creating a scoreCallback; Rounds off the score of each entry in gameScore up
		 * to 2 decimal places
		 */
		scoreCallback = new Callback<CellDataFeatures<Score, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Score, String> gameScore) {
				String tableScore = String.valueOf(Math.round(gameScore.getValue().getScore() * 100) / 100.0);
				return (new SimpleStringProperty(tableScore));
			}
		};

		/*
		 * Creates header table columns in searchTableView
		 */
		TableColumn<Score, String> gameIdColumn = new TableColumn<Score, String>("Id");
		TableColumn<Score, String> puzzleWordColumn = new TableColumn<Score, String>("Word");
		TableColumn<Score, Integer> timeStampColumn = new TableColumn<Score, Integer>("Time(sec)");
		TableColumn<Score, String> scoreColumn = new TableColumn<Score, String>("Score");

		// binding properties to individual columns
		gameIdColumn.setCellValueFactory(gameNameCallback);
		puzzleWordColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("puzzleWord"));
		timeStampColumn.setCellValueFactory(new PropertyValueFactory<Score, Integer>("timeStamp"));
		scoreColumn.setCellValueFactory(scoreCallback);

		// create table column instances
		searchTableView.getColumns().setAll(gameIdColumn, puzzleWordColumn, timeStampColumn, scoreColumn);

		// assigning proper width to all columns
		gameIdColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.25));
		puzzleWordColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.25));
		timeStampColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.25));
		scoreColumn.prefWidthProperty().bind(searchTableView.widthProperty().multiply(0.25));
	}
}
