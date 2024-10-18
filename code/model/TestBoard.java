package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TestBoard {
    int score = 0;

    Player player1 = new Player("Samuel",1);
    Player player2 = new Player("Ian",2);
    Player player3 = new Player("David",3);
    Player player4 = new Player("Max",4);

    Tile[] tiles = {new Tile('N',new Point(7,7)), new Tile('I', new Point(7,8))
            , new Tile('C', new Point(7,9)), new Tile('E', new Point(7,10))};

    Tile[] tiles1 = {new Tile('O', new Point(8,7))
            , new Tile('N', new Point(9,7)), new Tile('E', new Point(10,7))};

    Tile[] tiles2 = {new Tile('V', new Point(10,8))
            , new Tile('E', new Point(10,9)), new Tile('N', new Point(10,10))};

    Tile[] tiles3 = {new Tile('I', new Point(10,11))
            , new Tile('N', new Point(10,12)), new Tile('G', new Point(10,13))};

    Tile[] tiles4 = {new Tile('C', new Point(11,11)), new Tile('E', new Point(12,11))};

    Tile[] tiles5 = {new Tile('O', new Point(11,13))
            , new Tile('N', new Point(12,13)), new Tile('E', new Point(13,13))};

    //Tile[] tiles6 = {new Tile('O', new Point(12,12)), new Tile('R', new Point(13,12))};


    //valid points
    Board board = new Board();

    @Test
    public void testAddToBoard() throws InvalidPositionException {
        board.addToBoard(tiles);
        System.out.println("Tiles : Board");
        for (Tile tile: tiles) {
            Assertions.assertEquals(tile, board.getXAndY((int)tile.getLocation().getX(),(int)tile.getLocation().getY()));
            System.out.println(tile.getLetter() + " : " + board.getXAndY((int)tile.getLocation().getX(),(int)tile.getLocation().getY()).getLetter());
        }
        System.out.println();
    }

    @Test
    public void testScore() throws InvalidPositionException {
        score = board.playTiles(tiles);
        Assertions.assertEquals(12, score);

        score = board.playTiles(tiles1);
        Assertions.assertEquals(4, score);

        score = board.playTiles(tiles2);
        Assertions.assertEquals(14,score);

        score = board.playTiles(tiles3);
        Assertions.assertEquals(11, score);

        score = board.playTiles(tiles4);
        Assertions.assertEquals(10,score);

        score = board.playTiles(tiles5);
        Assertions.assertEquals(10, score);
    }

    @Test
    public void testHasAdjacentTile() throws InvalidPositionException {
        board.playTiles(tiles);
        board.playTiles(tiles1);

        Assertions.assertTrue(board.hasAdjacentCaller(new Point((int)tiles[tiles.length - 1].getLocation().getX() + 1,(int) tiles[tiles.length - 1].getLocation().getY()))); // Right of 'E'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point((int)tiles[0].getLocation().getX() - 1, (int)tiles[0].getLocation().getY()))); // Left of 'N'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point((int)tiles[2].getLocation().getX(), (int)tiles[0].getLocation().getY() + 1))); // Below 'C'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point((int)tiles[2].getLocation().getX(), (int)tiles[0].getLocation().getY() - 1))); // Above 'C'

        Assertions.assertTrue(board.hasAdjacentCaller(tiles[0].getLocation()));

        // Test a point with no adjacent tiles and at the edges of the board
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(14, 14)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(0, 0)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(14, 0)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(0, 14)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(7, 14)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(14, 7)));
    }


    @Test
    public void testHasDuplicates() throws InvalidPositionException {
        //for if positions are right
        board.playTiles(tiles);
        System.out.println("hasDuplicates is working when positions are right");
        System.out.println();

        //for if position is wrong
        tiles[1].setLocation(new Point(7,7));
        try{
            board.playTiles(tiles);
            Assertions.fail();
        }
        catch (InvalidPositionException e){
            System.out.println("Error message when and exception is thrown in hasDuplicates method: " + e.getMessage());
            System.out.println();
        }
    }

    @Test
    public void testSameXorY() throws InvalidPositionException {
        //for if positions are right
        board.playTiles(tiles);
        System.out.println("sameXorY is working when positions are valid");
        System.out.println();

        tiles[1].setLocation(new Point(7,8));
        try{
            board.playTiles(tiles);
            Assertions.fail();
        }
        catch (InvalidPositionException e){
            System.out.println("Error message when and exception is thrown in sameXorY method: " + e.getMessage());
            System.out.println();
        }
    }

    private Boolean validWordCheck(Set<Point> originPoints) throws InvalidPositionException {
        return board.isValidWordCaller(originPoints);
    }

    private Set<Point> pointGetter(Tile[] tiles) throws InvalidPositionException {
        Tile[] originTiles = board.findOrigin(tiles);
        Set<Point> originPoints = new HashSet<>();

        for(Tile tile: originTiles)
            originPoints.add(new Point(tile.getLocation().x, tile.getLocation().y));

        return originPoints;
    }

    @Test
    public void simulateGame() throws InvalidPositionException {

        //System.out.println(board.getDictionary().contains("E"));

        System.out.println("First Turn:");

        System.out.println();

        System.out.printf(player1.getName()+"'s Turn:\n");
        if(validWordCheck(pointGetter(tiles))){
            score = board.playTiles(tiles);
            player1.setScore(score);
            System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
            System.out.println(player1.getName()+"'s Total Score: "+ player1.getScore());
            board.getLastWordsPlayed().clear();
        }
        else
            System.out.println("Word is not in the dictionary");

        System.out.printf(player2.getName()+"'s Turn:\n");
        if(validWordCheck(pointGetter(tiles1))) {
            score = board.playTiles(tiles1);
            player2.setScore(score);
            System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
            System.out.println(player2.getName()+"'s Total Score: "+ player2.getScore());
            board.getLastWordsPlayed().clear();
        }
        else
            System.out.println("Word is not in the dictionary");

        System.out.printf(player3.getName()+"'s Turn:\n");
        if(validWordCheck(pointGetter(tiles2))) {
            score = board.playTiles(tiles2);
            player3.setScore(score);
            System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
            System.out.println(player3.getName()+"'s Total Score: "+ player3.getScore());
            board.getLastWordsPlayed().clear();
        }
        else
            System.out.println("Word is not in the dictionary");

        System.out.printf(player4.getName()+"'s Turn:\n");
        if(validWordCheck(pointGetter(tiles3))) {
            score = board.playTiles(tiles3);
            player4.setScore(score);
            System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
            System.out.println(player4.getName()+"'s Total Score: "+ player4.getScore());
            board.getLastWordsPlayed().clear();
        }
        else
            System.out.println("Word is not in the dictionary");
        System.out.println();

        System.out.println("Second Turn:");

        System.out.println();

        System.out.printf(player1.getName()+"'s Turn:\n");
        if(validWordCheck(pointGetter(tiles4))){
            score = board.playTiles(tiles4);
            player1.setScore(score);
            System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
            System.out.println(player1.getName()+"'s Total Score: "+ player1.getScore());
            board.getLastWordsPlayed().clear();
        }
        else
            System.out.println("Word is not in the dictionary");

        System.out.printf(player2.getName()+"'s Turn:\n");
        if(validWordCheck(pointGetter(tiles5))) {
            score = board.playTiles(tiles5);
            player2.setScore(score);
            System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
            System.out.println(player2.getName()+"'s Total Score: "+ player2.getScore());
            board.getLastWordsPlayed().clear();
        }
        else
            System.out.println("Word is not in the dictionary");

        System.out.println();

        System.out.println("Board State:");
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Tile tile = board.getXAndY(row, col);
                if (tile != null) {
                    System.out.print(tile.getLetter() + " ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
    }
}
