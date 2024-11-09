package scrabble.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrabble.model.Tile;
import scrabble.network.host.TileBag;

import java.util.HashMap;

public class TileBagTest {

    // try to give tiles from empyt tile bag
    @Test
    public void getTilesTest(){
        TileBag tileBag = new TileBag();
        tileBag.getNext(80);
        HashMap<Tile,Integer> curTileBag = tileBag.getTileBag();
        boolean hasZeroTiles = true;
        Tile[] tiles = curTileBag.keySet().toArray(new Tile[0]);
        for(Tile tile : tiles){
            if(curTileBag.get(tile) < 0){
                hasZeroTiles = false;
            }
        }
        Assertions.assertTrue(hasZeroTiles,"less than zero amount for a letter");



    }

    @Test
    public void getRemainingTest(){
        TileBag tileBag = new TileBag();
        tileBag.getNext(120);
        Assertions.assertEquals(0,tileBag.getRemainingTiles());

        tileBag = new TileBag();
        tileBag.getNext(100);
        Tile[] tiles = tileBag.getNext(3);
        Assertions.assertEquals(0,tiles.length);


    }
    @Test
    public void normalUseTest(){
        TileBag tileBag = new TileBag();
        Tile[] tiles = tileBag.getNext(4);

        tileBag.addTiles(new Tile[]{tiles[0]});
        Assertions.assertEquals(97,tileBag.getRemainingTiles());
    }


}
