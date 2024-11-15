package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.Controller;
import scrabble.network.host.PartyHost;

/**
 * This class is responsible for assigning IDs to players
 */
public class AssignID extends Message{
	private int playerID;

/**
 * Constructor for the AssignID class.
 * Initializes playerID and senderID by calling parent super class
 *
 * @param senderID the ID of the player sending a message
 * @param playerID the player ID
 **/
	public AssignID(int senderID, int playerID) {
		super(senderID);
		this.playerID = playerID;
	}

	/**
	 * sets selfID inside the controller class
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	@Override
	public void execute(Controller controller) {
		controller.setSelfID(playerID);
	}

	/**
	 * This method doesn't have any utility for this message class
	 */
	@Override
	public void execute(PartyHost partyHost) {}
}
