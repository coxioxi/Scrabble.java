package scrabble.network.messages;

import scrabble.model.Tile;

public class ExchangeTiles extends Message{
	private int playerID;
	private Tile[] toExchange;
}
