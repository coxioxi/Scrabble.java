package scrabble.network.messages;

import scrabble.controller.Controller;

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
		controller.getModel().passTurn(playerID);

	}
}
