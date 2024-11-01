package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.NotBlankException;
import scrabble.model.Tile;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html">
 	Serializable Documentation</a>
 */
public abstract class Message implements Serializable {

	public static final int HOST_ID = -1;
	@Serial
	private static final long serialVersionUID = 1L;

	private final int senderID;

	public static void printInstance(Message message ) {
		if (message instanceof Challenge) {
			System.out.println("Challenge");
		} else if (message instanceof ExchangeTiles) {
			System.out.println("Exchange");
		} else if (message instanceof ExitParty) {
			System.out.println("Exit");
		} else if (message instanceof NewTiles) {
			System.out.println("NewTiles");
		} else if (message instanceof PassTurn) {
			System.out.println("Pass");
		} else if (message instanceof PlayTiles) {
			System.out.println("PlayTiles");
		}
	}

	protected Message(int senderID) {
		this.senderID = senderID;
	}

	public int getSenderID() {
		return senderID;
	}

	/**
	 * This method operates on the controller to make changes to the GUI
	 * and the view based on what type of message this is. For example,
	 * a play tiles message will update the board model to have the tiles placed on it
	 * and change the view for the player
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	public abstract void execute(Controller controller);
}
