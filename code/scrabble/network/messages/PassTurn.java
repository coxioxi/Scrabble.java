package scrabble.network.messages;

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
}
