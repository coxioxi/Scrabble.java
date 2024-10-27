package scrabble.network.messages;

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
}
