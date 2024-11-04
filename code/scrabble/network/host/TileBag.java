package scrabble.network.host;

import scrabble.model.Tile;

import java.util.HashMap;
import java.util.Random;

/**
 * Maintains the bag of tiles. Allows tiles to be randomly removed and for tiles to be added.
 */
public class TileBag {
    private HashMap<Tile,Integer> tileBag;      // stores a number of a given tile
    private int remainingTiles;         // the number of tiles in the bag

    public static void main(String[] args) {
        TileBag tb = new TileBag();
        tb.getNext(4);
        tb.addTiles(new Tile[]{new Tile('A')});
        System.out.println(tb.getRemainingTiles());
    }

    /**
	 * Constructs a TileBag with the standard 100 tiles.
     * <br>
	 * See also: <a href="https://en.wikipedia.org/wiki/Scrabble_letter_distributions">Letter distributions</a>
	 */
    public TileBag() {
        fillTileBag();
    }

    /**
     * Adds a collection of tiles to the bag.
     * @param tiles the tiles to add to the bag.
     */
    public void addTiles(Tile[] tiles) {
        for(Tile tile: tiles){
            tileBag.replace(tile,tileBag.replace(tile,tileBag.get(tile)+1));
        }
    }

    // ???
    public void removeTiles(Tile[] tiles) {
        for(Tile tile: tiles){
            tileBag.replace(tile,tileBag.replace(tile,tileBag.get(tile)-1));
        }

    }

    /**
     * Randomly selects the next <code>numTiles</code> tiles from the bag.
     * @param numTiles how many tiles should be returned. If this number
     *                 exceeds the number of tiles remaining, the array returned will
     *                 be of size <code>getRemainingTiles</code>.
     * @return a randomly picked array of tiles with length <code>numTiles</code>.
     */
    public Tile[] getNext(int numTiles) {
        Tile[] keyArray = tileBag.keySet().toArray(new Tile[0]);
        Tile[] newTiles = new Tile[numTiles];
        Random random = new Random();
        for(int i = 0; i < numTiles; i++){
            Tile tile = keyArray[random.nextInt(keyArray.length)];
            newTiles[i] = tile;
            tileBag.replace(tile,tileBag.get(tile)-1);
            remainingTiles--;
        }
        return newTiles;
    }

    /**
     * Gets the number of remaining tiles available to be removed.
     * @return number of remaining tiles.
     */
    public int getRemainingTiles() {
        return remainingTiles;
    }

    private void fillTileBag() {
        remainingTiles = 100;
        tileBag = new HashMap<>();
        tileBag.put(new Tile('A'),9);
        tileBag.put(new Tile('B'),2);
        tileBag.put(new Tile('C'),2);
        tileBag.put(new Tile('D'),4);
        tileBag.put(new Tile('E'),12);
        tileBag.put(new Tile('F'),2);
        tileBag.put(new Tile('G'),3);
        tileBag.put(new Tile('H'),2);
        tileBag.put(new Tile('I'),9);
        tileBag.put(new Tile('J'),1);
        tileBag.put(new Tile('K'),1);
        tileBag.put(new Tile('L'),4);
        tileBag.put(new Tile('M'),2);
        tileBag.put(new Tile('N'),6);
        tileBag.put(new Tile('O'),8);
        tileBag.put(new Tile('P'),2);
        tileBag.put(new Tile('Q'),1);
        tileBag.put(new Tile('R'),6);
        tileBag.put(new Tile('S'),4);
        tileBag.put(new Tile('T'),6);
        tileBag.put(new Tile('U'),4);
        tileBag.put(new Tile('V'),2);
        tileBag.put(new Tile('W'),2);
        tileBag.put(new Tile('X'),1);
        tileBag.put(new Tile('Y'),2);
        tileBag.put(new Tile('Z'),1);
        tileBag.put(new Tile(), 2);
    }
}
