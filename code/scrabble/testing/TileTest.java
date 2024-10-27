package scrabble.testing;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import scrabble.model.*;

/**
 * This test class tests some methods of tile, was initially designed to make sure tiles
 * had the right values being set inside of them before testing other core components of the program
 */

public class TileTest{
    private final Map<Character,Integer> letterValue = new HashMap<>();
    Board board = new Board();

    Tile[] tiles = {new Tile('N',new Point(7,7)), new Tile('I', new Point(7,8))
            , new Tile('C', new Point(7,9)), new Tile('E', new Point(7,10))};

    @Test
    public void testCreateTileAndGetLetter(){
        char letter = 'A';
        for(int i = 65; i <= 90; ++i){
            Tile tile = new Tile(letter);
            Assertions.assertEquals(letter++, tile.getLetter());
        }
    }

    @Test
    public void testIsNew() throws InvalidPositionException {
        int score = board.playTiles(tiles);

        for (Tile tile : tiles) {
            Assertions.assertFalse(tile.getIsNew());
        }

        for (Tile tile : tiles) {
            tile.setIsNew(true);
            Assertions.assertTrue(tile.getIsNew());
        }
    }

    @Test
    public void testIsBlank(){
        Tile tile = new Tile('A');
        Assertions.assertFalse(tile.isBlank());
        Tile newTile = new Tile();
        Assertions.assertTrue(newTile.isBlank());
    }

    @Test
    public void testGetLetterScore(){
        char letter = 'A';
        for(int i = 65; i <= 90; ++i){
            Tile tile = new Tile(letter++);
            letterValue.put(tile.getLetter(),TileScore.getScoreForLetter(tile.getLetter()));
            Assertions.assertEquals(TileScore.getScoreForLetter(tile.getLetter()), letterValue.get(tile.getLetter()));
        }
    }

    @Test
    public void testNotBlankException() {
        Tile tile = new Tile('A');
        try{
            tile.setLetter('B');
            Assertions.fail();
        }
        catch (NotBlankException e){
            System.out.println("nuh uh, "+e.getMessage());
        }
    }
}
