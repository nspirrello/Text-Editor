package model;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer2 extends Helper{
	//Analyzer2 utilizes a one-while loop method for finding amount of sentences, words and syllables. When no matches for syllables, words, and 
	//sentences are left. The method ends.
	int[] array = {0,0,0};
	DecimalFormat df = new DecimalFormat("#.##");
	long timeTaken;
	public TextAnalyzer2(String text) {
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
		while(true){
			if(wordMatcher.find()){
				++wordNum;
			} 
			if(syllMatcher.find()){
				++syllNum;
			}
			if(sentMatcher.find()){
				++sentNum;
			}
			if(syllMatcher.find() == false && wordMatcher.find() == false && sentMatcher.find() == false){
				break;
			}
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
	public long getTimeTaken(){
		return timeTaken;
	}
	public double getNumberOfSyllables() {
		return array[1];
	}

	public double getNumberOfSentences() {
		return array[2];
	}

	public String getText() {
		return super.getText();
	}
}
