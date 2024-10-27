package scrabble.network.messages;

import scrabble.model.NotBlankException;
import scrabble.model.Tile;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 see <a href="https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html">
 	Serializable Documentation</a>
 */
public abstract class Message implements Serializable {

	public static final int HOST_ID = -1;
	@Serial
	private static final long serialVersionUID = 1L;

	private final int senderID;

	protected Message(int senderID) {
		this.senderID = senderID;
	}

	public int getSenderID() {
		return senderID;
	}

	protected static void writeTiles(ObjectOutputStream out, Tile[] tiles)
			throws IOException {
		for (Tile t: tiles) {
			out.writeObject(t.getLetter());
			out.writeObject(t.isBlank());
			out.writeObject(t.getLocation());
			out.writeObject(t.getIsNew());
		}
	}

	protected static Tile[] readTile(ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		ArrayList<Tile> tiles = new ArrayList<>(7);
		while (in.available() > 0) {
			char    letter   = (Character) 	in.readObject();
			boolean isBlank  = (Boolean) 	in.readObject();
			Point location = (Point) 		in.readObject();
			boolean isNew 	 = (Boolean) 	in.readObject();
			Tile tile;
			if (isBlank) {
				tile = new Tile();
				try {
					tile.setLetter(letter);
				} catch (NotBlankException e) {
					System.out.println("LMAO wtf just happened.\nerror in serializing tile");
				}
			}
			else {
				tile = new Tile(letter);
			}
			tile.setLocation(location);
			tile.setIsNew(isNew);
			tiles.add(tile);
		}
		return tiles.toArray(new Tile[0]);
	}
}
