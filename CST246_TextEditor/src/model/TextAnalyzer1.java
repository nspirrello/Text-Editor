package model;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer1 extends Helper{
	//Analyzer1 utilizes a three-while loop method for finding amount of sentences, words and syllables
	int[] array = {0,0,0};
	DecimalFormat df = new DecimalFormat("#.##");
	long timeTaken;
	public TextAnalyzer1(String text) {
		super(text);
	}
	
	public void findFleschInfo(){
		int sentNum = 0;
		int syllNum = 0;
		int wordNum = 0;
		String wordPatt = "\\w+";
		String syllPatt = "[AEIOUYaeiouy]+";
		String sentPatt = "[^!?.]+";
		Pattern wordSpliter = Pattern.compile(wordPatt);
		Pattern syllSpliter = Pattern.compile(syllPatt);
		Pattern sentSpliter = Pattern.compile(sentPatt);
		Matcher wordMatcher = wordSpliter.matcher(getText());
		Matcher syllMatcher = syllSpliter.matcher(getText());
		Matcher sentMatcher = sentSpliter.matcher(getText());
		long startTime = System.currentTimeMillis();
		while(wordMatcher.find()){
			++wordNum;
		}
		while(sentMatcher.find()){
			++sentNum;
		}
		while(syllMatcher.find()){
			++syllNum;
		}
		long stopTime = System.currentTimeMillis();
		timeTaken = stopTime - startTime;
		array[0] = wordNum;
		array[1] = syllNum;
		array[2] = sentNum;
	}
	public int getFleschScore(){
		 return (int) (206.835 - 1.015*(getNumberOfWords() / getNumberOfSentences()) - 84.6*(getNumberOfSyllables() / getNumberOfWords()));
	}
	public double getNumberOfWords() {
		return array[0];
	}

	public double getNumberOfSyllables() {
		return array[1];
	}

	public double getNumberOfSentences() {
		return array[2];
	}
	public long getTimeTaken(){
		return timeTaken;
	}
	@Override
	public String getText() {
		return super.getText();
	}

}
