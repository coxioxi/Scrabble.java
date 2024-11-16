package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

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

	/**
	 * Constructs a message with a senderID.
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
	 * Makes changes on the host.
	 * <p>
	 *     A common implementation would be to send a return message to the original sender,
	 *     then to relay the original message to other clients.
	 *     For example, an execution of a <code>PlayTiles</code> message
	 * 	   would require new tiles to be sent to the sending client and for the original message
	 * 	   to be relayed to other players.
	 * </p>
	 * <p>
	 *     Note that this method must use public getter methods for
	 *     all the components which should be changed.
	 * </p>
	 *
	 * @param partyHost the PartyHost object on which to make changes.
	 */
	public abstract void execute(PartyHost partyHost);
}
