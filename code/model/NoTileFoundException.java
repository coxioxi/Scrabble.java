package model;

/**
 * Exception for making changes to a tile in a list when it is not in the list
 */
public class NoTileFoundException extends Exception{
	public NoTileFoundException() {
		super();
	}

	public NoTileFoundException(String message) {
		super(message);
	}
}
