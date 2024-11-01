package scrabble.network.messages;

import scrabble.model.Tile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class PlayTiles extends Message {

	@Serial
	private static final long serialVersionUID = 7L;
	private int playerID;
	private Tile[] tiles;

	public PlayTiles(int senderID, int playerID, Tile[] tiles) {
		super(senderID);
		this.playerID = playerID;
		this.tiles = tiles;
	}

	public int getPlayerID() {
		return playerID;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	//Add method does the damn thing for all of the messages
}
