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
