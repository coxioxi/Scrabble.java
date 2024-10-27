package scrabble.network.messages;

import java.io.Serial;

public class ExitParty extends Message {
	@Serial
	private static final long serialVersionUID = 4L;
	private final int playerID;

	public ExitParty(int senderID, int playerID) {
		super(senderID);
		this.playerID = playerID;
	}

	public int getPlayerID() {
		return playerID;
	}
}
