package scrabble.network.messages;

import scrabble.model.Tile;

import java.io.Serial;

public class ExchangeTiles extends Message{
	@Serial
	private static final long serialVersionUID = 3L;
	private final int playerID;
	private final Tile[] toExchange;

	public ExchangeTiles(int senderID, int playerID, Tile[] toExchange) {
		super(senderID);
		this.playerID = playerID;
		this.toExchange = toExchange;
	}

	public int getPlayerID() {
		return playerID;
	}

	public Tile[] getToExchange() {
		return toExchange;
	}


}
