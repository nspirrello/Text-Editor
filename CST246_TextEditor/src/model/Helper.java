package model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Helper {
	private String text;

	public Helper(String text){
		this.text = text;
	}

	public List<String> getTokens(String pattern){
		ArrayList<String> tokens = new ArrayList<>();
		Pattern tokenSpliter = Pattern.compile(pattern);
		Matcher m = tokenSpliter.matcher(text);

		while(m.find()){
			tokens.add(m.group());
		}
		return tokens;
	}

	public int getCountOfSyllables(String word){
		int num = 0;
		String pattern = "[AEIOUYaeiouy]+";
		Pattern tokenSpliter = Pattern.compile(pattern);
		Matcher m = tokenSpliter.matcher(word);
		String lastToken = "";
		while(m.find()){
			++num;
			lastToken = m.group();
//			System.out.println(lastToken);
		}
		if(null == null){ //find out how this "lastToken" is a silent E
			num--;
		}
		return num;
	}
	public int getCountOfWords(String word){
		int num = 0;
		String pattern = "\\w+";
		Pattern tokenSpliter = Pattern.compile(pattern);
		Matcher m = tokenSpliter.matcher(word);
		String lastToken = "";
		while(m.find()){
			++num;
			lastToken = m.group();
		}
		return num;
	}
	public int getCountOfSentences(String word){
		int num = 0;
		String pattern = "[^!?.]+";
		Pattern tokenSpliter = Pattern.compile(pattern);
		Matcher m = tokenSpliter.matcher(word);
		String lastToken = "";
		while(m.find()){
			++num;
			lastToken = m.group();
		}
		return num;
	}
	public abstract double getNumberOfWords();

	public abstract double getNumberOfSyllables();

	public abstract double getNumberOfSentences();


	public String getText(){
		return text;
	};
	

	public int getFleschScore(){
		return 0;
	}
}
