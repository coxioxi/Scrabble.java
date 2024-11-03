package scrabble.network.messages;

import scrabble.controller.Controller;
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

	@Override
	public void execute(Controller controller) {
		controller.getModel().playTiles(playerID,tiles);
		// how to update view to show score

	}

	//Add method does the damn thing for all of the messages
}
