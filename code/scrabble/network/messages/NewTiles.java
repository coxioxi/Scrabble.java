package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Tile;

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

	@Serial
	private void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		tiles = readTile(in);
	}

	@Serial
	private void writeObject(ObjectOutputStream out)
			throws IOException {
		out.defaultWriteObject();
		writeTiles(out, tiles);
	}

	@Override
	public void execute(Controller controller) {

	}
}
