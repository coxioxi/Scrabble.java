package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;

/**
 * Enumeration of the modifier types on the Scrabble board
 */
public enum ModifierType {
	DOUBLE_LETTER(false, 2, "Double Letter", "DL", new Color(57, 70, 140)), // Doubles the score of the letter placed on this cell.
	TRIPLE_LETTER(false, 3, "Triple Letter", "TL", new Color(25, 43, 147)), // Triples the score of the letter placed on this cell.
	  DOUBLE_WORD(true , 2, "Double Word", "DW", new Color(154, 75, 75)), // Doubles the score of the entire word when a tile is placed on this cell.
	  TRIPLE_WORD(true , 3, "Triple Word", "TW", new Color(177, 19, 19)), // Triples the score of the entire word when a tile is placed on this cell.
	         NONE(false, 1, "Normal Cell", " ", new Color(221, 221, 221));  	// Normal cell.

	private final boolean appliesToWord;
	private final int multiplier;
	private final String name;
	private final String abbreviation;
	private final Color color;

	ModifierType(boolean appliesToWord, int multiplier, String name, String abbreviation, Color color) {
		this.appliesToWord = appliesToWord;
		this.multiplier = multiplier;
		this.name = name;
		this.abbreviation = abbreviation;
		this.color = color;
	}

	public boolean isAppliesToWord() {
		return appliesToWord;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public String getName() {
		return name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public Color getColor() {
		return color;
	}
}
