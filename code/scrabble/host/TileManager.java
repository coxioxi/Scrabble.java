package scrabble.host;

import scrabble.model.Tile;

public interface TileManager {
	void addTiles(Tile[] tiles);

	Tile[] getNext(int numTiles);
}
