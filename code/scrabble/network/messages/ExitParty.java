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
 * This class is responsible for sending exit messages to the host so this player can disconnect
 * notice that adding tiles back to the back are being handled by the host since the host keeps
 * track of all the players rack tiles
 */
public class ExitParty extends Message {
	@Serial
	private static final long serialVersionUID = 4L;
	private final int playerID;

	/**
	 * Constructor for the AssignID class.
	 * Initializes senderID and playerID by calling parent super class
	 *
	 * @param senderID The host ID
	 * @param playerID the ID of the player sending the message
	 */
	public ExitParty(int senderID, int playerID) {
		super(senderID);
		this.playerID = playerID;
	}

	/**
	 * Getter for player ID
	 *
	 * @return Int player ID
	 */
	public int getPlayerID() {
		return playerID;
	}


	/**
	 * This execute is responsible for sending the exit message to
	 * the host so this player can be disconnected
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	@Override
	public void execute(Controller controller) {
		try {
			if (controller.getSelfID() == this.playerID) controller.getMessenger().sendMessage(this);
			else {
				System.out.println("message.ExitParty#execute(Controller):\n\tplease implement me.");
			}
		} catch (IOException ignore) {
		}
	}

	/**
	 * This execute is responsible for sending the exit party message to all players. but the one who sent
	 * so the other clients know that this player has disconnected and can update their views accordingly
	 *
	 * @param partyHost the <code>PartyHost</code> on which to make changes. Note that this object
	 *                   must use public getter methods for all the components which should be changed.
	 */
	@Override
	public void execute(PartyHost partyHost) {
		//get new tiles and send it back to the client (this message playerID)
		partyHost.exit(this.playerID);
	}
}
