package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Tile;
import scrabble.network.host.PartyHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class NewTiles extends Message {
	@Serial
	private static final long serialVersionUID = 5L;
	private Tile[] tiles;

	public NewTiles(int senderID, Tile[] tiles) {
		super(senderID);
		this.tiles = tiles;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	@Override
	public void execute(Controller controller) {
		controller.getModel().addTiles(tiles);
		controller.addRack(this.tiles);
	}

	@Override
	public void execute(PartyHost partyHost) {}
}
