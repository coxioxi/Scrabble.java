package scrabble.network.host;

import scrabble.model.Tile;

import java.util.HashMap;
import java.util.Random;
/*
 * Represents the TileBag in a game of Scrabble.
 * Must hold the correct number of tiles at the start, then shuffle
 * shuffling must be done whenever tiles are added
 * must be able to remove a specified number of tiles from self and
 * return them as an array of tiles
 *
 */
public class TileBag {
    private HashMap<Tile,Integer> tileBag;

    public void addTiles(Tile[] tiles) {
        for(Tile tile: tiles){
            tileBag.replace(tile,tileBag.replace(tile,tileBag.get(tile)+1));
        }

    }

    public void removeTiles(Tile[] tiles) {
        for(Tile tile: tiles){
            tileBag.replace(tile,tileBag.replace(tile,tileBag.get(tile)-1));
        }

    }

    public Tile[] getNext(int numTiles) {
        Tile[] keyArray = (Tile[]) tileBag.keySet().toArray();
        Tile[] newTiles = new Tile[numTiles];
        Random random = new Random();
        for(int i = 0; i < numTiles; i++){
            Tile tile = keyArray[random.nextInt(keyArray.length)];
            newTiles[i] = tile;
            tileBag.replace(tile,tileBag.get(tile)-1);

        }
        return newTiles;
    }

    private void fillTileBag(){
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
    }
}
