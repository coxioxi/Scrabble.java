package model;

import java.util.ArrayList;

public class ScoreData {
	private ArrayList<String> words = new ArrayList<>();
	private int score;

	public ScoreData() {
	}

	public ScoreData(ArrayList<String> words, int score) {
		this.words = words;
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public ArrayList<String> getWords() {
		return words;
	}

	public void setWords(ArrayList<String> words) {
		this.words = words;
	}
}
