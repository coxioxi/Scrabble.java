package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.util.ArrayList;

/**
 * This class holds a list of strings and a score.
 * it is a stand-alone convenience class for helper methods of Board.score()
 */
public class ScoreData {
	private ArrayList<String> words; // list of the words played
	private int score; // current running score

	/**
	 * Constructs an object from a list of words and a score
	 * @param words the words for this object
	 * @param score the score for this object
	 */
	public ScoreData(ArrayList<String> words, int score) {
		this.words = words;
		this.score = score;
	}

	/**
	 * getter for score
	 * @return score as an int
	 */
	public int getScore() {
		return score;
	}

	/**
	 * setter for score
	 * @param score the score value to set this.score to.
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * getter for word list
	 * @return the ArrayList containing strings
	 */
	public ArrayList<String> getWords() {
		return words;
	}

	/**
	 * setter for words
	 * @param words the list of words to set this.words to.
	 */
	public void setWords(ArrayList<String> words) {
		this.words = words;
	}
}
