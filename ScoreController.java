//Gaurangi Upadhyay (gupadhya)
package hw3;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.LineChart;

public class ScoreController extends WordNerdController {
	ScoreView scoreView;
	ScoreChart scoreChart;
	WordNerdModel wordNerdModel = new WordNerdModel();
	List<LineChart<Number, Number>> lineChartList = new ArrayList<>();

	/*
	 * Displays ScoreView; Calls scoreChart.drawChart by passing
	 * wordNerdModel.scoreList; adds views for Hangman and twister to scoreGrid
	 */
	@Override
	public void startController() {

		scoreView = new ScoreView();
		scoreView.setupView();
		scoreChart = new ScoreChart();
		wordNerdModel.readScore();

		lineChartList = scoreChart.drawChart(wordNerdModel.scoreList);
		scoreView.scoreGrid.add(lineChartList.get(0), 0, 1, 2, 1);
		scoreView.scoreGrid.add(lineChartList.get(1), 0, 2, 2, 1);
		WordNerd.root.setTop(null);
		WordNerd.root.setCenter(scoreView.scoreGrid);
	}

	@Override
	public void setupBindings() {
		// to be implemented in HW3
	}

}
