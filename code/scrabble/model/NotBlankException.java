package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

/**
 * Exception for trying to set a tile to have a letter value when it is not blank
 */
public class NotBlankException extends Exception {
	public NotBlankException() {
		super();
	}

	public NotBlankException(String s){
		super(s);
	}
}
