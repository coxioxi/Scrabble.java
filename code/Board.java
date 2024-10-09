/**
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;
import java.util.*;

/**
 * This class represents the scrabble board. It maintains the placement of
 * Tile objects on the board and allows for changes to be made.
 *
 */
public class Board {
    private  Tile[][] board;  // where Tile objects are placed
    private Map<Point,ModifierType> boardSpecialCell;   // map of modifier cells
    private String[] lastWordsPlayed;   // the words which have most recently been played


    /**
     * Constructs a new Board object
     */
    public Board() {
        initializeModifierCells();
    }

    /**
     * This method places tiles at positions on the board and returns the score of
     * the play made
     *
     * @param tiles the tiles which are being placed on the board
     * @param points where the tiles are being placed. The size of this array and
     *               tiles must be the same and ordered to correspond. points[0] must
     *               correspond to tiles[0], points[1] must correspond to tiles[1], etc.
     *               Note that neither array may be empty, but arrays of size 1 are allowed.
     * @return the score of the word(s) played as an integer
     * @throws InvalidPositionException when placed incorrectly. At least one tile
     *                  must be adjacent to some other previously placed tile, or
     *                  one of the tiles must be at the starting tile (7,7).
     *                  No tile may be placed on an already occupied cell
     */
    public int playTiles(Tile[] tiles, Point[] points)
            throws InvalidPositionException{
        /*TODO:
            this method must ensure that all tiles are placed on unoccupied cells.
            it must check that one tile is placed on 7,7, or that one tile
                is adjacent to an already placed tile.
            it must score the words played appropriately.
            it must change the lastWordsPlayed field with these word(s).
         */
        // It is very likely that this method will need helper methods.
        return 0;
    }

    public String[] getLastWordsPlayed() {
        return lastWordsPlayed;
    }

    private void initializeModifierCells() {
        boardSpecialCell  =  new HashMap<>();
        boardSpecialCell.put(new Point(0,0), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(3,0), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(7,0), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(11,0), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(14,0), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(1,1), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(5,1), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(9,1), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(13,1), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(2,2), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(6,2), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(8,2), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(12,2), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(0,3), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(3,3), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(7,3), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(11,3), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(14,3), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(4,4), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(10,4), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(1,5), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(5,5), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(9,5), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(13,5), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(2,6), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(6,6), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(8,6), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(12,6), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(0,7), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(3,7), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(7,7), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(11,7), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(14,7), ModifierType.TRIPLE_WORD);

        boardSpecialCell.put(new Point(0,14), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(3,14), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(7,14), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(11,14), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(14,14), ModifierType.TRIPLE_WORD);
        boardSpecialCell.put(new Point(1,13), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(5,13), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(9,13), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(13,13), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(2,12), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(6,12), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(8,12), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(12,12), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(0,11), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(3,11), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(7,11), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(11,11), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(14,11), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(4,10), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(10,10), ModifierType.DOUBLE_WORD);
        boardSpecialCell.put(new Point(1,9), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(5,9), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(9,9), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(13,8), ModifierType.TRIPLE_LETTER);
        boardSpecialCell.put(new Point(2,8), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(6,8), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(8,8), ModifierType.DOUBLE_LETTER);
        boardSpecialCell.put(new Point(12,8), ModifierType.DOUBLE_LETTER);
    }

    /**
     * Clears the board of all tiles, puts null values in their place
     */
    public void clearBoard(){
		for (Tile[] tiles : board) {
			Arrays.fill(tiles, null);
		}
    }

    /*

    public Board(){
        board = new Tile[15][15];
        int point1 = 1;
        int point2 = 2;
        int point3 = 3;
        int point4 = 4;
        int point5 = 5;
        int point6 = 8;
        int point7 = 10;
        int point0 = 0;
        letterKeyValue.put(" ",point0);
        letterKeyValue.put("A",point1);
        letterKeyValue.put("E",point1);
        letterKeyValue.put("I",point1);
        letterKeyValue.put("L",point1);
        letterKeyValue.put("N",point1);
        letterKeyValue.put("O",point1);
        letterKeyValue.put("U",point1);
        letterKeyValue.put("S",point1);
        letterKeyValue.put("T",point1);
        letterKeyValue.put("R",point1);
        letterKeyValue.put("D",point2);
        letterKeyValue.put("G",point2);
        letterKeyValue.put("B",point3);
        letterKeyValue.put("C",point3);
        letterKeyValue.put("M",point3);
        letterKeyValue.put("P",point3);
        letterKeyValue.put("F",point4);
        letterKeyValue.put("H",point4);
        letterKeyValue.put("V",point4);
        letterKeyValue.put("W",point4);
        letterKeyValue.put("Y",point4);
        letterKeyValue.put("K",point5);
        letterKeyValue.put("J",point6);
        letterKeyValue.put("X",point6);
        letterKeyValue.put("Q",point7);
        letterKeyValue.put("Z",point7);

    }

    public void addToBoard(String letter, int row, int column) {
        if (row >= 0 && row < 15 && column >= 0 && column < 15 && board[row][column] == null) {
            board[row][column] = new Tile(letter.toUpperCase(),letterKeyValue.get(letter.toUpperCase()), new Point(row,column));
        } else {
            System.out.println("Invalid position or tile already exists at (" + row + ", " + column + ").");
        }
    }

    public static void main(String[] args) {
        Board test = new Board();
        test.addToBoard("r",5,7);
        test.addToBoard("u",6,7);
        test.addToBoard("n",7,7);
        test.addToBoard("i",7,8);
        test.addToBoard("g",7,9);
        test.addToBoard("h",7,10);
        test.addToBoard("t",7,11);
        test.addToBoard("k",7,6);
        test.addToBoard("a",8,11);
        test.addToBoard("l",9,11);
        test.addToBoard("k",10,11);
        test.addToBoard("t",8,10);
        test.boardView();
        test.boardScan();
        System.out.println();
    }

    public void boardView(){
        for(int row = 0; row < board.length; ++row){
            for(int column = 0; column < board[row].length; ++column){
                if(board[row][column] != null){
                    System.out.print(" "+board[row][column].getLetter()+" ");
                }
                else{
                    System.out.print(" * ");
                }
            }
            System.out.println();
        }
    }
    public void boardScan(){
        List<Tile> tiles = new ArrayList<>();
        List<List<Tile>> words = new ArrayList<>();
        List<String> newWords = new ArrayList<>();
        for(int row = 0; row < board.length; ++row){
            for(int column = 0; column < board[row].length; ++column){
                if(board[row][column] != null){
                    tiles.add(board[row][column]);
                }
            }
        }
        //
        for(int i = 0; i < tiles.size() - 1; ++i){
            List<Tile> tempTiles = new ArrayList<>();
            for(int j = 0; j < tiles.size(); ++j){
                if ((tiles.get(i).getLocation().getX() == tiles.get(j).getLocation().getX()) || (tiles.get(i).getLocation().getY() == tiles.get(j).getLocation().getY())) {
                    tempTiles.add(tiles.get(j));
                }
            }

            String tempString ="";
            for (int k = 0; k < tempTiles.size(); k++) {
                if(tempTiles.get(tempTiles.size() - 1).getLocation().getX() == tempTiles.get(0).getLocation().getX() || tempTiles.get(tempTiles.size() - 1).getLocation().getY() == tempTiles.get(0).getLocation().getY() )
                    tempString += tempTiles.get(k).getLetter();
            }

            if (newWords.isEmpty() && !tempString.isEmpty()) {
                newWords.add(tempString);
            } else if (!newWords.contains(tempString)&& !tempString.isEmpty()) {
                newWords.add(tempString);
            }
        }

        for (int i = 0; i < newWords.size(); i++) {
            System.out.println(newWords.get(i));
        }
    }

    /*
    Takes in an x,y coordinate and returns a number between -1 and 5
    return values and labels:
        -1 = Invalid Coordinates
        0 = Double Letter (light blue)
        1 = Triple letter (dark blue)
        2 = Double Word (light red)
        3 = Triple Word (dark red)
        4 = Start (light red)
        5 = Blank (gray)
    *
    public static int locationCheck(int x, int y) {

        final int[][] scrabbleBoard = {
                // 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14
                {3, 5, 5, 0, 5, 5, 5, 3, 5, 5, 5, 0, 5, 5, 3}, // 0
                {5, 2, 5, 5, 5, 1, 5, 5, 5, 1, 5, 5, 5, 2, 5}, // 1
                {5, 5, 2, 5, 5, 5, 0, 5, 0, 5, 5, 5, 2, 5, 5}, // 2
                {0, 5, 5, 2, 5, 5, 5, 0, 5, 5, 5, 2, 5, 5, 0}, // 3
                {5, 5, 5, 5, 2, 5, 5, 5, 5, 5, 2, 5, 5, 5, 5}, // 4
                {5, 1, 5, 5, 5, 1, 5, 5, 5, 1, 5, 5, 5, 1, 5}, // 5
                {5, 5, 0, 5, 5, 5, 0, 5, 0, 5, 5, 5, 0, 5, 5}, // 6
                {3, 5, 5, 0, 5, 5, 5, 4, 5, 5, 5, 0, 5, 5, 3}, // 7
                {5, 5, 0, 5, 5, 5, 0, 5, 0, 5, 5, 5, 0, 5, 5}, // 8
                {5, 1, 5, 5, 5, 1, 5, 5, 5, 1, 5, 5, 5, 1, 5}, // 9
                {5, 5, 5, 5, 2, 5, 5, 5, 5, 5, 2, 5, 5, 5, 5}, // 10
                {0, 5, 5, 2, 5, 5, 5, 0, 5, 5, 5, 2, 5, 5, 0}, // 11
                {5, 5, 2, 5, 5, 5, 0, 5, 0, 5, 5, 5, 2, 5, 5}, // 12
                {5, 2, 5, 5, 5, 1, 5, 5, 5, 1, 5, 5, 5, 2, 5}, // 13
                {3, 5, 5, 0, 5, 5, 5, 3, 5, 5, 5, 0, 5, 5, 3}  // 14
        };

        if(x >= 0 && x < 15 && y >= 0 && y < 15)
            return scrabbleBoard[x][y];
        else
            return -1;
    }
    */
}
