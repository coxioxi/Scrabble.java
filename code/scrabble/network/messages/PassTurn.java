package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.Controller;
import scrabble.network.PartyHost;

import java.io.IOException;
import java.io.Serial;

/**
 * Message sent when a turn is passed by a player
 */
public class PassTurn extends Message{
	@Serial
	private static final long serialVersionUID = 25L;
	private final int playerID;

	/**
	 * Constructor for the PassTurn class
	 *
	 * @param senderID the hostID
	 * @param playerID the player who is passing a turn
	 */
	public PassTurn(int senderID, int playerID) {
		super(senderID);
		this.playerID = playerID;
	}

	/**
	 * Getter for player ID
	 *
	 * @return int player ID
	 */
	public int getPlayerID() {
		return playerID;
	}


	/**
	 * This execute is responsible for passing the turn of the current player
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	@Override
	public void execute(Controller controller) {
		try {
			if(controller.getSelfID() == this.playerID) controller.getMessenger().sendMessage(this);
			controller.passTurn(playerID);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This execute is responsible for sending the passTurn message to the host and other players
	 * so their model is updated accordingly
	 *
	 * @param partyHost the <code>PartyHost</code> on which to make changes. Note that this object
	 *                   must use public getter methods for all the components which should be changed.
	 */
	@Override
	public void execute(PartyHost partyHost) {
		try {
			partyHost.sendToAllButID(this.playerID, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
