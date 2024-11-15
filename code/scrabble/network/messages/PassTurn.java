package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.Controller;
import scrabble.network.host.PartyHost;

import java.io.IOException;
import java.io.Serial;

public class PassTurn extends Message{
	@Serial
	private static final long serialVersionUID = 6L;
	private final int playerID;

	public PassTurn(int senderID, int playerID) {
		super(senderID);
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}

	@Override
	public void execute(Controller controller) {
		try {
			controller.getMessenger().sendMessage(this);
			controller.getModel().passTurn(playerID);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void execute(PartyHost partyHost) {
		PassTurn passTurn = new PassTurn(PartyHost.HOST_ID, this.playerID);
		try {
			partyHost.sendMessage(this.playerID, passTurn);
			partyHost.sendToAllButID(this.playerID, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
