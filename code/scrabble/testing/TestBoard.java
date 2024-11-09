package scrabble.testing;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

import scrabble.model.*;

/**
 * This test class tests some of the methods that form the backbone of this program, run this class to get a short simulation of the game
 * running, creating players, placing words on the board, getting the right scores, and updating players total score.
 */

public class TestBoard {
    Player player1 = new Player("Samuel",1, 0);
    Player player2 = new Player("Ian",2, 1);
    Player player3 = new Player("David",3, 2);
    Player player4 = new Player("Max",4, 3);

    //valid tiles
    Tile[] tiles0 = {new Tile('N',new Point(7,7)), new Tile('I', new Point(7,8))
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

    Tile[] tiles7 = {new Tile('O', new Point(9,8))
            , new Tile('A', new Point(11,8)), new Tile('L', new Point(12,8))};

    Tile[] tiles8 = {new Tile('A', new Point(8,9)), new Tile('R', new Point(9,9))};

    Tile[] tiles9 = {new Tile('T', new Point(8,10))};

    Tile[] tiles11 = {new Tile('O',new Point(12,12)), new Tile('R', new Point(13,12))};

    Tile[] tiles12 = {new Tile('M', new Point(4,8))
            , new Tile('I', new Point(5,8)), new Tile('N', new Point(6,8))};

    Tile[] tiles13 = {new Tile('M', new Point(3,9))
            , new Tile('A', new Point(4,9)), new Tile('N', new Point(5,9))};

    Tile[] tiles14 = {new Tile('O', new Point(3,10))
            , new Tile('N', new Point(3,11)), new Tile('O', new Point(3,12))};

    //invalid tiles
    Tile[] tiles15 = {new Tile('N',new Point(0,-1)), new Tile('I', new Point(1,-1))
            , new Tile('C', new Point(1,-1)), new Tile('E', new Point(3,-1))};

    Tile[] tiles16 = {new Tile('N',new Point(-1,0)), new Tile('O', new Point(0,0))
            , new Tile('G', new Point(-1,0)), new Tile('E', new Point(2,0))};

    Tile[] tiles17 = {new Tile('N',new Point(13,0)), new Tile('O', new Point(14,0))
            , new Tile('V', new Point(14,0)), new Tile('E', new Point(16,0))};

    Tile[] tiles18 = {new Tile('N',new Point(0,0)), new Tile('I', new Point(1,0))
            , new Tile('V', new Point(2,0)), new Tile('E', new Point(4,0))};

    Tile[] tiles19 = {new Tile('N',new Point(5,0)), new Tile('I', new Point(6,0))
            , new Tile('C', new Point(7,0)), new Tile('L', new Point(9,0))};

    Tile[] tiles20 = {new Tile('X',new Point(7,0)), new Tile('I', new Point(6,1))
            , new Tile('C', new Point(7,2)), new Tile('E', new Point(9,3))};

    Board board = new Board();
    int score = 0;

    @AfterEach
    public void clear(){board.clearBoard();}

    @Test
    public void testAddToBoard() {
        System.out.println();
        board.addToBoard(tiles0);
        for (Tile tile: tiles0) {
            Assertions.assertEquals(tile, board.getTile((int)tile.getLocation().getX(),(int)tile.getLocation().getY()));
        }
    }

    @Test
    public void testArePointsInBounds(){
        Assertions.assertTrue(board.getAreInBounds(tiles0));
        Assertions.assertTrue(board.getAreInBounds(tiles1));
        Assertions.assertTrue(board.getAreInBounds(tiles2));
        Assertions.assertFalse(board.getAreInBounds(tiles15));
        Assertions.assertFalse(board.getAreInBounds(tiles16));
        Assertions.assertFalse(board.getAreInBounds(tiles17));
    }

    @Test
    public void testHasNoDuplicates(){
        Assertions.assertTrue(board.getHasNoDuplicates(tiles0));
        Assertions.assertTrue(board.getHasNoDuplicates(tiles1));
        Assertions.assertTrue(board.getHasNoDuplicates(tiles2));
        Assertions.assertFalse(board.getHasNoDuplicates(tiles15));
        Assertions.assertFalse(board.getHasNoDuplicates(tiles16));
        Assertions.assertFalse(board.getHasNoDuplicates(tiles17));
    }

    @Test
    public void testPointsNotOccupied(){
        board.playTiles(tiles0);
        board.playTiles(tiles1);
        board.playTiles(tiles2);

        Assertions.assertFalse(board.getPointsNotOccupied(tiles0));
        Assertions.assertFalse(board.getPointsNotOccupied(tiles1));
        Assertions.assertFalse(board.getPointsNotOccupied(tiles2));
        Assertions.assertTrue(board.getPointsNotOccupied(tiles3));
        Assertions.assertTrue(board.getPointsNotOccupied(tiles4));
        Assertions.assertTrue(board.getPointsNotOccupied(tiles5));
    }

    @Test
    public void testArePointsStartingOrAdjacent(){
        board.playTiles(tiles0);
        board.playTiles(tiles1);
        board.playTiles(tiles2);
        board.playTiles(tiles18);
        board.playTiles(tiles19);
        board.playTiles(tiles20);

        Assertions.assertTrue(board.getArePointsStartingOrAdjacent(tiles0));
        Assertions.assertTrue(board.getArePointsStartingOrAdjacent(tiles1));
        Assertions.assertTrue(board.getArePointsStartingOrAdjacent(tiles2));
        Assertions.assertFalse(board.getArePointsStartingOrAdjacent(tiles18));
        Assertions.assertFalse(board.getArePointsStartingOrAdjacent(tiles19));
        Assertions.assertFalse(board.getArePointsStartingOrAdjacent(tiles20));
    }

    @Test
    public void testArePointsConnected(){
        board.playTiles(tiles0);
        board.playTiles(tiles1);
        board.playTiles(tiles2);
        board.playTiles(tiles18);
        board.playTiles(tiles19);
        board.playTiles(tiles20);

        Assertions.assertTrue(board.getArePointsConnected(tiles0));
        Assertions.assertTrue(board.getArePointsConnected(tiles1));
        Assertions.assertTrue(board.getArePointsConnected(tiles2));
        Assertions.assertFalse(board.getArePointsConnected(tiles18));
        Assertions.assertFalse(board.getArePointsConnected(tiles19));
        Assertions.assertFalse(board.getArePointsConnected(tiles20));
    }

    @Test
    public void testAllSameRow(){
        board.playTiles(tiles0);
        board.playTiles(tiles1);
        board.playTiles(tiles2);
        board.playTiles(tiles7);

        Assertions.assertTrue(board.getAllSameRow(tiles0));
        Assertions.assertTrue(board.getAllSameRow(tiles2));
        Assertions.assertFalse(board.getAllSameRow(tiles1));
        Assertions.assertFalse(board.getAllSameRow(tiles7));
    }

    @Test
    public void testAllSameCol() {
        board.playTiles(tiles0);
        board.playTiles(tiles1);
        board.playTiles(tiles2);
        board.playTiles(tiles7);

        Assertions.assertTrue(board.getAllSameCol(tiles1));
        Assertions.assertTrue(board.getAllSameCol(tiles7));
        Assertions.assertFalse(board.getAllSameCol(tiles0));
        Assertions.assertFalse(board.getAllSameCol(tiles2));
    }

    @Test
    public void testHasAdjacentTile() {
        board.playTiles(tiles0);
        board.playTiles(tiles1);

        Assertions.assertTrue(board.hasAdjacentCaller(new Point((int) tiles0[tiles0.length - 1].getLocation().getX() + 1,(int) tiles0[tiles0.length - 1].getLocation().getY()))); // Right of 'E'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point((int) tiles0[0].getLocation().getX() - 1, (int) tiles0[0].getLocation().getY()))); // Left of 'N'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point((int) tiles0[2].getLocation().getX(), (int) tiles0[0].getLocation().getY() + 1))); // Below 'C'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point((int) tiles0[2].getLocation().getX(), (int) tiles0[0].getLocation().getY() - 1))); // Above 'C'

        Assertions.assertTrue(board.hasAdjacentCaller(tiles0[0].getLocation()));

        // Test a point with no adjacent tiles and at the edges of the board
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(14, 14)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(0, 0)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(14, 0)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(0, 14)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(7, 14)));
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(14, 7)));
    }

    @Test
    public void testScore() {
        score = board.playTiles(tiles0);
        Assertions.assertEquals(12,score);

        score = board.playTiles(tiles1);
        Assertions.assertEquals(4,score);

        score = board.playTiles(tiles2);
        Assertions.assertEquals(14,score);

        score = board.playTiles(tiles3);
        Assertions.assertEquals(11,score);

        score = board.playTiles(tiles4);
        Assertions.assertEquals(10,score);

        score = board.playTiles(tiles5);
        Assertions.assertEquals(10,score);

        score = board.playTiles(tiles7);
        Assertions.assertEquals(10,score);

        score = board.playTiles(tiles8);
        Assertions.assertEquals(13,score);

        score = board.playTiles(tiles9);
        Assertions.assertEquals(4,score);

        score = board.playTiles(tiles11);
        Assertions.assertEquals(12,score);

        score = board.playTiles(tiles12);
        Assertions.assertEquals(7,score);

        score = board.playTiles(tiles13);
        Assertions.assertEquals(15,score);

        score = board.playTiles(tiles14);
        Assertions.assertEquals(12,score);

        //Invalid words
        score = board.playTiles(tiles16);
        Assertions.assertEquals(-1,score);

        score = board.playTiles(tiles17);
        Assertions.assertEquals(-1,score);

        score = board.playTiles(tiles18);
        Assertions.assertEquals(-1,score);

        score = board.playTiles(tiles19);
        Assertions.assertEquals(-1,score);

        score = board.playTiles(tiles20);
        Assertions.assertEquals(-1,score);
    }

    @Test
    public void simulateGame() {
        System.out.println("First Turn:");
        System.out.println();

        System.out.printf(player1.getName()+"'s Turn:\n");
        score = board.playTiles(tiles0);
        player1.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player1.getName()+"'s Total Score: "+ player1.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player2.getName()+"'s Turn:\n");
        score = board.playTiles(tiles1);
        player2.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player2.getName()+"'s Total Score: "+ player2.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player3.getName()+"'s Turn:\n");

        score = board.playTiles(tiles2);
        player3.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player3.getName()+"'s Total Score: "+ player3.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player4.getName()+"'s Turn:\n");
        score = board.playTiles(tiles7);
        player4.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player4.getName()+"'s Total Score: "+ player4.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.println();

        System.out.println("Second Turn:");

        System.out.println();

        System.out.printf(player1.getName()+"'s Turn:\n");
        score = board.playTiles(tiles3);
        player1.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player1.getName()+"'s Total Score: "+ player1.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player2.getName()+"'s Turn:\n");
        score = board.playTiles(tiles5);
        player2.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player2.getName()+"'s Total Score: "+ player2.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player3.getName()+"'s Turn:\n");

        score = board.playTiles(tiles8);
        player3.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player3.getName()+"'s Total Score: "+ player3.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player4.getName()+"'s Turn:\n");
        score = board.playTiles(tiles9);
        player4.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player4.getName()+"'s Total Score: "+ player4.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.println("Third Turn:");
        System.out.println();

        System.out.printf(player2.getName()+"'s Turn:\n");
        score = board.playTiles(tiles11);
        player2.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player2.getName()+"'s Total Score: "+ player2.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player3.getName()+"'s Turn:\n");
        score = board.playTiles(tiles12);
        player3.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player3.getName()+"'s Total Score: "+ player3.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player4.getName()+"'s Turn:\n");
        score = board.playTiles(tiles13);
        player4.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player4.getName()+"'s Total Score: "+ player4.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.printf(player1.getName()+"'s Turn:\n");
        score = board.playTiles(tiles14);
        player1.increaseScore(score);
        System.out.println("Words formed this turn: " + board.getLastWordsPlayed() + "\nTurn Score: " + score);
        System.out.println(player1.getName()+"'s Total Score: "+ player1.getScore());
        System.out.println();
        board.getLastWordsPlayed().clear();

        System.out.println();

        Assertions.assertEquals(35, player1.getScore());
        Assertions.assertEquals(24, player2.getScore());
        Assertions.assertEquals(34, player3.getScore());
        Assertions.assertEquals(29, player4.getScore());

        System.out.println("Board State:");
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Tile tile = board.getTile(row, col);
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
