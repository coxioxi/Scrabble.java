package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TileTest{

    private final Map<Character,Integer> letterValue = new HashMap<>();

    @Test
    public void testCreateTileAndGetLetter(){
        char letter = 'A';
        for(int i = 65; i <= 90; ++i){
            Tile tile = new Tile(letter);
            Assertions.assertEquals(letter++, tile.getLetter());
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
