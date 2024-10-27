<<<<<<<< HEAD:code/scrabble/model/exceptions/InvalidPositionException.java
package scrabble.model.exceptions;
========
package scrabble.model;
>>>>>>>> origin/ScoreUpdates:code/scrabble/model/InvalidPositionException.java

/**
 * Exception for tiles placed incorrectly on the scrabble board
 */
public class InvalidPositionException extends Exception{
	public InvalidPositionException() {
		super();
	}

	public InvalidPositionException(String s){
		super(s);
	}
}
