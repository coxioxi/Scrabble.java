package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.network.host.PartyHost;

import java.io.IOException;
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

	@Override
	public void execute(Controller controller) {
		try {
			controller.getMessenger().sendMessage(this);
		} catch (IOException ignore) {
		}
	}

	@Override
	public void execute(PartyHost partyHost) {
		//get new tiles and send it back to the client (this message playerID)
		try{
			partyHost.sendToAllButID(this.playerID, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
