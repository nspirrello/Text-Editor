package view;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.TextAnalyzer1;
import model.TextAnalyzer2;

public class Gui extends Application{
	TextAnalyzer1 t1;
	TextAnalyzer2 t2;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane bPane = new BorderPane();
		//Menu
		MenuBar menu = new MenuBar();
		
		Menu file = new Menu("File");
		MenuItem open = new MenuItem("Open");
		MenuItem saveCounts = new MenuItem("Save counts");
		MenuItem newText = new MenuItem("New");
		MenuItem saveText = new MenuItem("Save text");
		MenuItem graph = new MenuItem("Graph");
		MenuItem exit = new MenuItem("Exit");
		
		file.getItems().addAll(open,saveCounts,newText,saveText,graph,exit);
		
		Menu edit = new Menu("Edit");
		CheckMenuItem wordCount = new CheckMenuItem("Word count");
		CheckMenuItem syllCount = new CheckMenuItem("Syllable count");
		CheckMenuItem sentCount = new CheckMenuItem("Sentence count");
		wordCount.setSelected(true);
		syllCount.setSelected(true);
		sentCount.setSelected(true);
		
		edit.getItems().addAll(wordCount,syllCount,sentCount);
		
		menu.getMenus().addAll(file,edit);
		
		bPane.setTop(menu);
		
		TextArea textArea = new TextArea();
		textArea.setWrapText(true);
		bPane.setCenter(textArea);
		
		//Status bar
		Label wordCountStatus = new Label("Words: " );
		Label syllableCountStatus = new Label("Syllables: " );
		Label sentenceCountStatus = new Label("Sentences: " );
		Label fleschScoreStatus = new Label("Flesch Score: ");
		HBox statusBar = new HBox(30);
		statusBar.getChildren().addAll(wordCountStatus,syllableCountStatus,sentenceCountStatus,fleschScoreStatus);
		bPane.setBottom(statusBar);
		
		wordCount.setOnAction(event -> {
			if(wordCount.isSelected()){
				statusBar.getChildren().add(0, wordCountStatus);
			} else {
				statusBar.getChildren().remove(wordCountStatus);
			}
		});
		syllCount.setOnAction(event -> {
			if(syllCount.isSelected()){
				statusBar.getChildren().add(1, syllableCountStatus);
			} else {
				statusBar.getChildren().remove(syllableCountStatus);
			}
		});
		sentCount.setOnAction(event -> {
			if(sentCount.isSelected()){
				statusBar.getChildren().add(2, sentenceCountStatus);

			} else {
				statusBar.getChildren().remove(sentenceCountStatus);
			}
		});
		
		saveCounts.setOnAction(event -> {	
			String textAnOutput1 = "Words: " + t1.getNumberOfWords() + " Syllables: " + t1.getNumberOfSyllables()
						+ " Sentences: " + t1.getNumberOfSentences() + " Flesch Score: " + t1.getFleschScore();
			String textAnOutput2 = "Words: " + t2.getNumberOfWords() + " Syllables: " + t2.getNumberOfSyllables()
			+ " Sentences: " + t2.getNumberOfSentences() + " Flesch Score: " + t2.getFleschScore();
			File output = new File("outputData");
			output.mkdir();
			
			File results = new File(output,"results.txt");
			try {
				appendToFile(results, textAnOutput1 + " ");
				appendToFile(results, textAnOutput2);
				appendToFile(results,"\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		open.setOnAction(event -> {
			FileChooser fc = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Txt Files, (*.txt)","*.txt");
			fc.getExtensionFilters().add(extFilter);
			File fileRec = fc.showOpenDialog(new Stage());
			Scanner readInText = null;
			try {
				readInText = new Scanner(fileRec);
			} catch (Exception e) {
				e.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			while(readInText.hasNextLine()){
				sb.append(readInText.nextLine() + " ");
			}
			textArea.setText(sb.toString());
			
		});
		
		saveText.setOnAction(event -> {
			FileChooser fc = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Txt Files, (*.txt)","*.txt");
			fc.getExtensionFilters().add(extFilter);
			File fileRec = fc.showSaveDialog(new Stage());
			String textAreaString = textArea.getText();
			try {
				writeToFile(fileRec,textAreaString);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		});
		
		textArea.textProperty().addListener((observable, oldValue, newValue) -> {
			t1 = new TextAnalyzer1(newValue);
			t2 = new TextAnalyzer2(newValue);
			t1.findFleschInfo();
			t2.findFleschInfo();
			wordCountStatus.setText("Words: " + t1.getNumberOfWords());
			syllableCountStatus.setText("Syllables: " + t1.getNumberOfSyllables());
			sentenceCountStatus.setText("Sentences: " + t1.getNumberOfSentences());
			fleschScoreStatus.setText("Flesch Score: " + t1.getFleschScore());
		});
		
		newText.setOnAction(event -> {
			textArea.clear();
		});
		
		exit.setOnAction(event -> {
			File results = new File("results.txt");
			try {
				FileWriter fw = new FileWriter(results);
				PrintWriter pw = new PrintWriter(fw);
				pw.flush();
				pw.close();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
		});
		//reads the textFile "WarAndPeace" using Regex and appends every match to a StringBuilder
		System.out.println("War and Peace is currently being truncated...");
		FileInputStream fStream = new FileInputStream("WarAndPeace.txt");
		DataInputStream in = new DataInputStream(fStream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		StringBuilder sb = new StringBuilder();
		while((strLine = br.readLine()) != null){
			sb.append(strLine + " ");
		}
		in.close();
		//uses the created string for method input
		preTruncateIt(sb.toString());
		//creates a directory and 10 files inside it
		File parentDir = new File("inputData");
		
		File ten = new File(parentDir,"10%Text.txt");
		File twenty = new File(parentDir,"20%Text.txt");
		File thirty = new File(parentDir,"30%Text.txt");
		File fourty = new File(parentDir,"40%Text.txt");
		File fifty = new File(parentDir,"50%Text.txt");
		File sixty = new File(parentDir,"60%Text.txt");
		File seventy = new File(parentDir,"70%Text.txt");
		File eighty = new File(parentDir,"80%Text.txt");
		File ninety = new File(parentDir,"90%Text.txt");
		File hundred = new File(parentDir,"100%Text.txt");
		
		
		//collects the data from each process
		graph.setOnAction(event -> {
			long[] allTimes = new long[20];
			try {
				allTimes[0] = getTimesForFile(ten,0);
				allTimes[1] = getTimesForFile(ten,1);
				allTimes[2] = getTimesForFile(twenty,0);
				allTimes[3] = getTimesForFile(twenty,1);
				allTimes[4] = getTimesForFile(thirty,0);
				allTimes[5] = getTimesForFile(thirty,1);
				allTimes[6] = getTimesForFile(fourty,0);
				allTimes[7] = getTimesForFile(fourty,1);
				allTimes[8] = getTimesForFile(fifty,0);
				allTimes[9] = getTimesForFile(fifty,1);
				allTimes[10] = getTimesForFile(sixty,0);
				allTimes[11] = getTimesForFile(sixty,1);
				allTimes[12] = getTimesForFile(seventy,0);
				allTimes[13] = getTimesForFile(seventy,1);
				allTimes[14] = getTimesForFile(eighty,0);
			    allTimes[15] = getTimesForFile(eighty,1);
			    allTimes[16] = getTimesForFile(ninety,0);
			    allTimes[17] = getTimesForFile(ninety,1);
			    allTimes[18] = getTimesForFile(hundred,0);
			    allTimes[19] = getTimesForFile(hundred,1);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
					
			final NumberAxis xAxis = new NumberAxis();
			final NumberAxis yAxis = new NumberAxis();
			xAxis.setLabel("Percentage of File");
			yAxis.setLabel("Time taken");
			final LineChart<Number,Number> lineChart1 = new LineChart<Number,Number>(xAxis,yAxis);
			lineChart1.setTitle("BigO for Text Analyzers");
			
			//Fills and Creates the data graph
			XYChart.Series series1 = new XYChart.Series<>();
			series1.setName("Text Analyzer1");
			
			series1.getData().add(new XYChart.Data(10,allTimes[0]));
			series1.getData().add(new XYChart.Data(20,allTimes[2]));
			series1.getData().add(new XYChart.Data(30,allTimes[4]));
			series1.getData().add(new XYChart.Data(40,allTimes[6]));
			series1.getData().add(new XYChart.Data(50,allTimes[8]));
			series1.getData().add(new XYChart.Data(60,allTimes[10]));
			series1.getData().add(new XYChart.Data(70,allTimes[12]));
			series1.getData().add(new XYChart.Data(80,allTimes[14]));
			series1.getData().add(new XYChart.Data(90,allTimes[16]));
			series1.getData().add(new XYChart.Data(100,allTimes[18]));
			
			XYChart.Series series2 = new XYChart.Series<>();
			series2.setName("Text Analyzer2");
			
			series2.getData().add(new XYChart.Data(10,allTimes[1]));
			series2.getData().add(new XYChart.Data(20,allTimes[3]));
			series2.getData().add(new XYChart.Data(30,allTimes[5]));
			series2.getData().add(new XYChart.Data(40,allTimes[7]));
			series2.getData().add(new XYChart.Data(50,allTimes[9]));
			series2.getData().add(new XYChart.Data(60,allTimes[11]));
			series2.getData().add(new XYChart.Data(70,allTimes[13]));
			series2.getData().add(new XYChart.Data(80,allTimes[15]));
			series2.getData().add(new XYChart.Data(90,allTimes[17]));
			series2.getData().add(new XYChart.Data(100,allTimes[19]));
			
			lineChart1.getData().addAll(series1,series2);
			
			HBox holder = new HBox(30);
			holder.getChildren().addAll(lineChart1);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(holder,600,300));
			stage.show();
			
		});
		
		
		primaryStage.setScene(new Scene(bPane,600,300));
		primaryStage.show();
		
		
		
	}
	public void preTruncateIt(String fileText){
		//get the amount of words, split by a percent, add to strings, write all strings to file
		
		int num = 0;
		String pattern = "\\w+";
		Pattern tokenSpliter = Pattern.compile(pattern);
		Matcher m = tokenSpliter.matcher(fileText);
		while(m.find()){
			++num;
		}
		
		File parentDir = new File("inputData");
		parentDir.mkdir();
		
		//create 10 files and write the truncatedString into each respective one	
		int tenPe = (int) (num * .1);
		int twentyPe = (int) (num * .2);
		int thirtyPe = (int) (num * .3);
		int fourtyPe = (int) (num * .4);
		int fiftyPe = (int) (num * .5);
		int sixtyPe = (int) (num * .6);
		int seventyPe = (int) (num * .7);
		int eightyPe = (int) (num * .8);
		int ninetyPe = (int) (num * .9);
		
		
		try {
			writeToFile(new File(parentDir,"10%Text.txt"),truncateIt(fileText, tenPe));
			writeToFile(new File(parentDir,"20%Text.txt"),truncateIt(fileText, twentyPe));
			writeToFile(new File(parentDir,"30%Text.txt"),truncateIt(fileText, thirtyPe));
			writeToFile(new File(parentDir,"40%Text.txt"),truncateIt(fileText, fourtyPe));
			writeToFile(new File(parentDir,"50%Text.txt"),truncateIt(fileText, fiftyPe));
			writeToFile(new File(parentDir,"60%Text.txt"),truncateIt(fileText, sixtyPe));
			writeToFile(new File(parentDir,"70%Text.txt"),truncateIt(fileText, seventyPe));
			writeToFile(new File(parentDir,"80%Text.txt"),truncateIt(fileText, eightyPe));
			writeToFile(new File(parentDir,"90%Text.txt"),truncateIt(fileText, ninetyPe));
			writeToFile(new File(parentDir,"100%Text.txt"),fileText);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	public String truncateIt(String fullText, int numOfWords){
		//regex pattern matches words (everything but whitespace)
		String pattern = "[^\\s]+";
		StringBuilder sb = new StringBuilder();
		Pattern tokenSpliter = Pattern.compile(pattern);
		Matcher m = tokenSpliter.matcher(fullText);
		//takes the matches and appends to a stringbuilder
		for(int i = 0;i < numOfWords;i++){
			if(m.find()){
				sb.append(m.group() + " ");
			}
		}
		return sb.toString();
	}
	//writes to a given textFile and overrides the file each call
	public void writeToFile(File fileName, String truncatedString) throws IOException{
		fileName.createNewFile();
		 FileWriter fw = new FileWriter(fileName);
		 PrintWriter pw = new PrintWriter(fw);
		 pw.print(truncatedString);
		 pw.close();
		
	}
	//appends to a given textFile
	public void appendToFile(File fileName, String toWrite) throws IOException{
		fileName.createNewFile();
		 FileWriter fw = new FileWriter(fileName, true);
		 PrintWriter pw = new PrintWriter(fw);
		 pw.print(toWrite + " ");
		 pw.close();
		
	}
	//returns the time variable for graphing from a file. whichOne determines which of the two analyzers
	//will be used
	public long getTimesForFile(File fileName, int whichOne) throws IOException{
		FileInputStream fStream = new FileInputStream(fileName);
		System.out.println("The Data is currently being plotted for File:" + fileName);
		DataInputStream in = new DataInputStream(fStream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		long time = 0;
		String strLine;
		StringBuilder sb = new StringBuilder();
		while((strLine = br.readLine()) != null){
			sb.append(strLine + " ");
		}
		in.close();
		TextAnalyzer1 t1 = new TextAnalyzer1(sb.toString());
		TextAnalyzer2 t2 = new TextAnalyzer2(sb.toString());
		if(whichOne == 0){
			t1.findFleschInfo();
			time = t1.getTimeTaken();
		} else if(whichOne == 1){
			t2.findFleschInfo();
			time = t2.getTimeTaken();
		}
		System.out.println(time);
		return time;
	}
	

}
