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

import java.io.Serial;

public class Challenge extends Message {
	@Serial
	private static final long serialVersionUID = 2L;
	private final int challengingPlayerID;
	private final int challengedPlayerID;

	public Challenge(int senderID, int challengingPlayerID, int challengedPlayerID) {
		super(senderID);
		this.challengingPlayerID = challengingPlayerID;
		this.challengedPlayerID = challengedPlayerID;
	}

	public int getChallengingPlayerID() {
		return challengingPlayerID;
	}

	public int getChallengedPlayerID() {
		return challengedPlayerID;
	}

	@Override
	public void execute(Controller controller) {

	}
	@Override
	public void execute(PartyHost partyHost) {

	}
}
