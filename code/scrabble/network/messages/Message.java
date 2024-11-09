package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.NotBlankException;
import scrabble.model.Tile;
import scrabble.network.host.PartyHost;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html">
 	Serializable Documentation</a>
 */
public abstract class Message implements Serializable {

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
		} else if (message instanceof AssignID) {
			System.out.println("AssignID");
		} else if (message instanceof StartGame) {
			System.out.println("startGame");
		}
	}

	/**
	 * Constructs a message with a senderID. Only to be used by subclasses
	 * @param senderID the ID of the message's sender.
	 */
	protected Message(int senderID) {
		this.senderID = senderID;
	}

	/**
	 * Gets the sender ID.
	 * @return returns the ID of the player who sent the message.
	 */
	public int getSenderID() {
		return senderID;
	}

	/**
	 * This method operates on the controller to make changes to the GUI
	 * and the view based on what type of message this is. For example,
	 * a play tiles message will update the board model to have the tiles placed on it
	 * and change the view for the player.
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	public abstract void execute(Controller controller);

	/**
	 * This method operates on the <code>host</code> to make changes to the clients
	 * when necessary. For example, an execution of a <code>PlayTiles</code> message
	 * would require new tiles to be sent to the sending client and for the original message
	 * to be relayed to other players.
	 *
	 * @param partyHost the <code>PartyHost</code> on which to make changes. Note that this object
	 *                   must use public getter methods for all the components which should be changed.
	 */
	public abstract void execute(PartyHost partyHost);
}
