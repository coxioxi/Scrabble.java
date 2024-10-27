package scrabble.network.messages;

import java.io.Serial;
import java.io.Serializable;

/**
 see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html">
 	Serializable Documentation</a>
 */
public abstract class Message implements Serializable {

	public static final int HOST_ID = -1;
	@Serial
	private static final long serialVersionUID = 1L;

	private final int senderID;

	protected Message(int senderID) {
		this.senderID = senderID;
	}

	public int getSenderID() {
		return senderID;
	}
}
