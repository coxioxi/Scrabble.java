package scrabble.network.host;

import scrabble.model.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

/**
 * Maintains the bag of tiles. Allows tiles to be randomly removed and for tiles to be added.
 * TODO: change the probability of tile's given
 */
public class TileBag {
    private ArrayList<Tile> tilebag;// stores a number of a given tile

    public static void main(String[] args) {
        TileBag tb = new TileBag();
        tb.getNext(4);
        tb.addTiles(new Tile[]{new Tile('A')});
        System.out.println(tb.getRemainingTiles());
    }

    /**
	 * Constructs a TileBag with the standard 100 tiles - 2 (no blanks).
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
            tilebag.add(tile);
        }
    }


    public ArrayList<Tile> getTileBag() {
        return tilebag;
    }

    /**
     * Randomly selects the next <code>numTiles</code> tiles from the bag.
     * @param numTiles how many tiles should be returned. If this number
     *                 exceeds the number of tiles remaining, the array returned will
     *                 be of size <code>getRemainingTiles</code>.
     * @return a randomly picked array of tiles with length <code>numTiles</code>.
     */
    public Tile[] getNext(int numTiles) {

        Tile[] newTiles = new Tile[numTiles];
        Random random = new Random();
        for(int i = 0; i < numTiles; i++){
            if(!tilebag.isEmpty()) {
                Tile tile = tilebag.get(random.nextInt(tilebag.size()));
                newTiles[i] = tile;
                tilebag.remove(tile);
            }
        }
        return newTiles;
    }

    /**
     * Gets the number of remaining tiles available to be removed.
     * @return number of remaining tiles.
     */
    public int getRemainingTiles() {
        return tilebag.size();
    }

    private void fillTileBag() {
        char[] letters = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

        int[] letterNum = new int[]{9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        ArrayList<Tile> letterList = new ArrayList<>();
        for(int i = 0; i < letters.length; ++i){
            for(int j = 0; j <letterNum[i]; ++j){
                if(letters[i] != ' ') {
                    letterList.add(new Tile(letters[i]) );
                }else{
                    letterList.add(new Tile());
                }
            }
        }
        tilebag = letterList;
    }


}
