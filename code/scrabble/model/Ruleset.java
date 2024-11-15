package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Scanner;

import java.util.HashSet;

/**
 * This class is a representation of the options in a Scrabble game.
 * It holds information on the amount of time per turn, how much time is
 * allowed for the whole game, whether challenges are allowed, and what dictionary is
 * being used.
 */
public class Ruleset implements Serializable {
	@Serial
	private static final long serialVersionUID = 9L;
	private final int totalTime;				// Total time allotted for the whole game (in seconds)
	private final int turnTime;					// Time allotted per turn for each player (in seconds)
	private final boolean challengesAllowed;	// Indicates if challenges are allowed during gameplay
	private final String dictionaryFileName;	// Path to the dictionary file
	private HashSet<String> dictionary;			// Set of valid words from the dictionary


	/**
	 * Creates a scrabble.model.Ruleset object
	 * @param totalTime how much time (in seconds) is allotted for the whole game.
	 *                  Between 300 and 3600.
	 * @param turnTime how much time (in seconds) a player may take on their turn.
	 *                 Between 30 and 300
	 * @param challengesAllowed whether challenges are allowed or not
	 * @param dictionaryFileName the path to the file which will be the dictionary
	 */
	public Ruleset(int totalTime, int turnTime,
				   boolean challengesAllowed, String dictionaryFileName) {
		this.totalTime = totalTime;						// Initialize total game time
		this.turnTime = turnTime;						// Initialize turn time
		this.challengesAllowed = challengesAllowed;		// Set challenge allowed
		this.dictionaryFileName = dictionaryFileName;	// Set dictionary file name
		//dictionary = readInDictionary();				// Load words from the dictionary file
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
		return true;
	}

	/**
	 * Getter for total time
	 * @return the total time of the game (in seconds)
	 */
	public int getTotalTime() {
		return totalTime;
	}


	/**
	 * Getter for turn time
	 * @return the turn time for players (in seconds)
	 */
	public int getTurnTime() {
		return turnTime;
	}

	/**
	 * Getter for challenges enabled
	 * @return whether challenges are allowed
	 */
	public boolean areChallengesAllowed() {
		return challengesAllowed;
	}

	/**
	 * Reads in the words in the dictionary using the pathname given
	 */
	public void setupDictionary() {
		HashSet<String> list = new HashSet<>();
		try{
			File dictionary = new File(dictionaryFileName).getAbsoluteFile();
			Scanner scanner = new Scanner(dictionary);
			while(scanner.hasNext()){
				list.add(scanner.nextLine());
			}

		}catch(IOException ignore){
		}
		this.dictionary = list;
	}


}
