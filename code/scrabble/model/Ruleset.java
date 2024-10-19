package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is a representation of the options in a Scrabble game.
 * It holds information on the amount of time per turn, how much time is
 * allowed for the whole game, whether challenges are allowed, and what dictionary is
 * being used.
 */
public class Ruleset {
	private int totalTime;
	private int turnTime;
	private final boolean areChallengesAllowed;
	private final String dictionaryFileName;
	private ArrayList<String> dictionary;


	/**
	 * Creates a scrabble.model.Ruleset object
	 * @param totalTime how much time (in seconds) is allotted for the whole game.
	 *                  Between 300 and 3600.
	 * @param turnTime how much time (in seconds) a player may take on their turn.
	 *                 Between 30 and 300
	 * @param areChallengesAllowed whether challenges are allowed or not
	 * @param dictionaryFileName the path to the file which will be the dictionary
	 */
	public Ruleset(int totalTime, int turnTime,
				   boolean areChallengesAllowed, String dictionaryFileName) {
		this.totalTime = totalTime;
		this.turnTime = turnTime;
		this.areChallengesAllowed = areChallengesAllowed;
		this.dictionaryFileName = dictionaryFileName;
		dictionary = readInDictionary();
	}

	/**
	 * Checks to see if a word is in the dictionary
	 * @param words the words to be checked
	 * @return true if it is in dictionary, false otherwise
	 */
	public boolean isWordInDictionary(String[] words) {
		for(String word: words){
			if(!dictionary.contains(word)){
				return false;
			}
		}
		//TODO: binary search on dictionary
		return true;
	}

	/**
	 * getter for total time
	 * @return the total time of the game (in seconds)
	 */
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int time){
		totalTime = time;
	}

	/**
	 * getter for turn time
	 * @return the turn time for players (in seconds)
	 */
	public int getTurnTime() {
		return turnTime;
	}

	public void setTurnTime(int time){
		turnTime = time;
	}
	/**
	 * getter for challenges enabled
	 * @return whether challenges are allowed
	 */
	public boolean isAreChallengesAllowed() {
		return areChallengesAllowed;
	}

	/*
	reads in the words in the dictionary using the pathname given
	 */
	private ArrayList<String> readInDictionary() {
		//TODO: read in the word list from dictionary. put into arraylist, then convert and return
		ArrayList<String> list = new ArrayList<>();
		try{
			File dictionary = new File("./code/scrabble/" + dictionaryFileName);
			Scanner scanner = new Scanner(dictionary);
			while(scanner.hasNext()){
				list.add(scanner.nextLine());
			}

		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return list;

	}


}
