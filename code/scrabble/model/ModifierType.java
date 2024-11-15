package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

/**
 * Enumeration of the modifier types on the Scrabble board
 */
public enum ModifierType {
	DOUBLE_LETTER, // Doubles the score of the letter placed on this cell.
	TRIPLE_LETTER, // Triples the score of the letter placed on this cell.
	DOUBLE_WORD, // Doubles the score of the entire word when a tile is placed on this cell.
	TRIPLE_WORD, // Triples the score of the entire word when a tile is placed on this cell.
	NONE  	// Normal cell.
}
