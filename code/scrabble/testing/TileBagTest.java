package scrabble.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import scrabble.model.Tile;
import scrabble.network.PartyHost;

public class TileBagTest {

    // try to give tiles from empyt tile bag


    @Test
    public void getRemainingTest(){
        String failMessage = "if there is nothing in tilebag should return 0 tiles";
        PartyHost.TileBag tileBag = new PartyHost.TileBag();
        Tile[] tiles = tileBag.getNext(4);
        tileBag.addTiles(new Tile[]{tiles[0]});
        Assertions.assertEquals(97,tileBag.getRemainingTiles(),failMessage);


        tileBag = new PartyHost.TileBag();
        tileBag.getNext(100);
        tiles = tileBag.getNext(3);
        Assertions.assertEquals(0,tileBag.getTileBag().size(),failMessage);


        tileBag = new PartyHost.TileBag();
        tileBag.getNext(7);
        tiles = tileBag.getNext(3);
        Assertions.assertEquals(90,tileBag.getTileBag().size(),failMessage);


    }


}
