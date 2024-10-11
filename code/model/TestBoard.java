package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class TestBoard {
    Tile[] tiles = {new Tile('N'),new Tile('I'),new Tile('C'), new Tile('E')};

    //valid points
    Point[] points = {new Point(7,7),new Point(8,7),new Point(9,7),new Point(10,7)};
    Board board = new Board();

    @Test
    public void testAddToBoard() throws InvalidPositionException {
        board.playTiles(tiles);
        System.out.println("Tiles : Board");
        for (int i = 0; i < tiles.length; i++) {
            Assertions.assertEquals(tiles[i], board.getXAndY((int) points[i].getX(), (int) points[i].getY()));
            System.out.println(tiles[i].getLetter() + " : " + board.getXAndY((int)points[i].getX(), (int)points[i].getY()).getLetter());
        }
        System.out.println();
    }

    @Test
    public void testHasAdjacentTile() throws InvalidPositionException {
        board.playTiles(tiles, points);

        Assertions.assertTrue(board.hasAdjacentCaller(new Point(points[points.length - 1].x + 1, points[points.length - 1].y))); // Right of 'E'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point(points[0].x - 1, points[0].y))); // Left of 'N'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point(points[2].x, points[0].y + 1))); // Below 'C'
        Assertions.assertTrue(board.hasAdjacentCaller(new Point(points[2].x, points[0].y - 1))); // Above 'C'

        // Test a point with no adjacent tiles
        Assertions.assertFalse(board.hasAdjacentCaller(new Point(14, 14)));
    }

    @Test
    public void testHasDuplicates() throws InvalidPositionException {
        //for if positions are right
        board.playTiles(tiles);
        System.out.println("hasDuplicates is working when positions are right");
        System.out.println();

        //for if position is wrong
        Point[] points = {new Point(7,7),/* wrong points*/new Point(7,7),new Point(9,7),new Point(10,7),new Point(11,7)};
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
        System.out.println("sameXorY is working when positions are right");
        System.out.println();

        Point[] points = {new Point(7,7),/* wrong points*/new Point(7,8),new Point(9,7),new Point(10,7),new Point(11,7)};
        try{
            board.playTiles(tiles);
            Assertions.fail();
        }
        catch (InvalidPositionException e){
            System.out.println("Error message when and exception is thrown in sameXorY method:" + e.getMessage());
            System.out.println();
        }
    }

    @Test
    public void printBoardState() throws InvalidPositionException {
        System.out.println("Board State:");
        board.playTiles(tiles);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Tile tile = board.getXAndY(i, j);
                if (tile != null) {
                    System.out.print(tile.getLetter() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
