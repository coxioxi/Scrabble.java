package scrabble.model.exceptions;

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
