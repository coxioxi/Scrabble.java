package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.network.host.PartyHost;

public class AssignID extends Message{
	private int playerID;

	public AssignID(int senderID, int playerID) {
		super(senderID);
		this.playerID = playerID;
	}

	@Override
	public void execute(Controller controller) {
		controller.setSelfID(playerID);
	}

	@Override
	public void execute(PartyHost partyHost) {
		// nothing
	}
}
