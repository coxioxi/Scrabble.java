package model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class represents the scrabble board. It maintains the placement of
 * model.Tile objects on the board and allows for changes to be made.
 * When changes are made, model.Board scores the word(s) played and allows them to be accessed
 * in the future. Note that model.Board does not check the validity of words, only their values.
 */
public class Board {
    private  Tile[][] board;  // where model.Tile objects are placed
    private Map<Point,ModifierType> boardSpecialCell;   // map of modifier cells
    private ArrayList<String> lastWordsPlayed = new ArrayList<>();;   // the words which have most recently been played
    private final ArrayList<String> dictionary;
    final int rows = 15;
    final int cols = 15;

    /**
     * Constructs a new model.Board object
     */
    public Board() {
        initializeModifierCells();
        board = new Tile[15][15];
        dictionary = importDictionary();
    }

    /**
     * @return a list of the words played on the most recent board change
     */
    public ArrayList<String> getLastWordsPlayed() {
        return lastWordsPlayed;
    }

    /**
     * Clears the board of all tiles, puts null values in their place
     */
    public void clearBoard(){
        for (Tile[] tiles : board) {
            Arrays.fill(tiles, null);
        }
    }

    /**
     * Returns tiles inside of given x and y locations
     */
    public Tile getXAndY(int x, int y){
        return board[x][y];
    }

    public void removeTiles(Tile[] tiles) {
        // TODO: implement to remove tiles from points on board. double check that
        // tile specified and tile on board correspond
    }

    /**
     * A caller method for testing purposes
     */
    public boolean hasAdjacentCaller(Tile tile){
        return hasAdjacentTile(tile);
    }

    /**
     * This method places tiles at positions on the board and returns the score of
     * the play made
     *
     * @param tiles the tiles which are being placed on the board
     * @return the score of the word(s) played as an integer
     * @throws InvalidPositionException when placed incorrectly. At least one tile
     *                  must be adjacent to some other previously placed tile, or
     *                  one of the tiles must be at the starting tile (7,7).
     *                  No tile may be placed on an already occupied cell
     */
    public int playTiles(Tile[] tiles)
            throws InvalidPositionException{
        int score = 0;
        /*TODO:
            this method must score the words played appropriately
                (implement score method)
            it must change the lastWordsPlayed field with these word(s).
            update the board with tiles
         */
        // It is very likely that this method will need helper methods.
        //sameXorY(points);
        //hasDuplicates(points);
        validatePositions(tiles);  // half implemented
        //int score = score(tiles, points);   // not implemented
        addToBoard(tiles);      // half implemented
        return score;
    }


    /**********************************************
                    Private methods
     **********************************************/


    /*
    helper method which adds tiles to the board at specified points.
    does not check scoring or validity of play.
     */
    public void addToBoard(Tile[] tiles) throws InvalidPositionException {
        //TODO: implement. for each tile in tiles, add to board at corresponding point
        for(int i = 0; i < tiles.length; ++i)
            board[(int) tiles[i].getLocation().getX()][(int) tiles[i].getLocation().getY()] = tiles[i];
    }

    /*
    helper method; calculates the score of tiles played with words and modifier cells.
    returns score as an int
    also updates lastWordsPlayed
     */
    public int score(Tile[] originTiles) {
        //TODO: implement score method.

        /*
        needs to find: 1. completely new words and 2.concatenated words (already played tiles)
        takes origin tile and scan down and right
        3. appropriately apply multipliers
        4. set lastWordsPlayed to currently scored words
        P.S. if you can try and get started on playtiles that would be apprecieated and a good area in testboard for demonstration
        -Jy'el
        */

        int sum = 0;
        for(int i = 0; i < originTiles.length; ++i){
            int row = originTiles[i].getLocation().y;
            int col = originTiles[i].getLocation().x;

            while(board[row][col] != null){
                if(board[row][col].getIsNew()){
                    row = originTiles[i].getLocation().y;
                    while (board[row][col] != null) {
                        sum += board[row][col].getScore();
                        ++row;
                    }
                }
                ++row;
            }
            row = originTiles[i].getLocation().y;

            while(board[row][col] != null){
                if(board[row][col].getIsNew()) {
                    col = originTiles[i].getLocation().x;
                    while(board[row][col] != null) {
                        sum += board[row][col].getScore();
                        ++col;
                    }
                }
                ++col;
            }
        }
        /*
        for(int i = 0; i < originTiles.length; ++i){
            if (originTiles[i].getIsNew() && boardSpecialCell.containsKey(new Point(originTiles[i].getLocation().x,originTiles[i].getLocation().y))){
                sum *= boardSpecialCell.get(new Point(originTiles[i].getLocation().x,originTiles[i].getLocation().y));
            }
        }
         */

        StringBuilder string = new StringBuilder();
        for (Tile tile : originTiles) {
            string.append(tile);
        }
        lastWordsPlayed.add(String.valueOf(string));
        return sum;
    }

    public ArrayList<String> stringBuild(Set<Point> originTiles, Tile[] newTiles, Point[] newTilePoints) throws InvalidPositionException {
        ArrayList<String> string = new ArrayList<>();
        for(Point originPoint : originTiles){
            String tempString = "";
            int row = (int)originPoint.getX();
            int column = (int)originPoint.getX();

            while(board[row][column] != null){
                tempString += board[row][column].getLetter();
                row = row + 1;
            }
            row = (int)originPoint.getX();
            if(tempString.length() > 1){
                string.add(tempString);
            }
            tempString = "";
            while(board[row][column] != null){
                tempString += board[row][column].getLetter();
                column = column + 1;
            }
            if(tempString.length() > 1){
                string.add(tempString);
            }
        }
        return string;
    }

    /*
    helper method; checks that points meet valid positions requirements of Scrabble.
    (here, tiles are represented by the points) checks that:
        no tiles are placed in occupied cell
        1 tile is placed on starting tile
            or 1 tile is adjacent to already placed tile
        all tiles are connected, either by adjacency, or adjacency to adjacency
     */
    private void validatePositions(Tile[] tiles) throws InvalidPositionException {

        //TODO: add check that all tiles are connected
        //  this means that all tiles are next to each other, or separated
        //  by an already placed tile. There may not be gaps.
        boolean areValid = true;

        // are any points already occupied?
        for (Tile tile: tiles) {
            if (board[(int) tile.getLocation().getX()][(int) tile.getLocation().getY()] != null)
                throw new InvalidPositionException(
                        "Illegal placement: some cells are already occupied"
                );
        }

        // is any tile played on the starting tile?
        boolean isStarting = false;
        for (Tile tile: tiles) {
            if ((int) tile.getLocation().getY() == 7 && (int) tile.getLocation().getX() == 7)
                isStarting = true;
        }

        // is any tile next to an already placed tile?
        boolean hasAdjacentTile = false;
        if (!isStarting) {
            for (Tile tile: tiles) {
                if (hasAdjacentTile(tile))
                    hasAdjacentTile = true;
            }
        }

        // did both of last two checks fail? throw an exception
        if (!hasAdjacentTile && !isStarting)
            throw new InvalidPositionException(
                    "Invalid placement: not adjacent to a cell and not starting"
            );

        // check if they are all connected

    }

    /*
    helper method; checks if any of the four adjacent cells to point are occupied
    returns true if adjacent is occupied
     */
    private boolean hasAdjacentTile(Tile tile) {
        int x = tile.getLocation().x;
        int y = tile.getLocation().y;

        if (x - 1 >= 0 && board[x - 1][y] != null && !board[x - 1][y].isBlank()) {
            return true;
        }
        else if (x + 1 < cols && board[x + 1][y] != null && !board[x + 1][y].isBlank()) {
            return true;
        }
        else if (y - 1 >= 0 && board[x][y - 1] != null && !board[x][y - 1].isBlank()) {
            return true;
        }
        else if (y + 1 < rows && board[x][y + 1] != null && !board[x][y + 1].isBlank()){
            return true;
        }
        else
            return false;
    }

    /*
    helper method; checks if any points have same x and y value
    throws exception if duplicates found
     */
    private void hasDuplicates(Tile[] tiles) throws InvalidPositionException {
        boolean hasDuplicates = false;
        for (int i = 0; i < tiles.length - 1 && !hasDuplicates; i++) {
            for (int j = i + 1; j < tiles.length && !hasDuplicates; j++) {
                Point point1 = tiles[i].getLocation();
                Point point2 = tiles[j].getLocation();
                if (point1.getX() == point2.getX() && point1.getY() == point2.getY())
                    hasDuplicates = true;
            }
        }
        if (hasDuplicates)
            throw new InvalidPositionException(
                    "Duplicate locations are not allowed"
            );
    }

    /*
    helper method; checks that all points have either same x or y value
    throws exception if points are not in a line.
     */
    private void sameXorY(Tile[] tiles) throws InvalidPositionException {
        boolean hasSameX = true;
        boolean hasSameY = true;
        for (int i = 0; i < tiles.length - 1 && (hasSameX || hasSameY); i++) {
            if (tiles[i].getLocation().getX() != tiles[i+1].getLocation().getX())
                hasSameX = false;
            if (tiles[i].getLocation().getY() != tiles[i+1].getLocation().getY())
                hasSameY = false;
        }
        if (!(hasSameX || hasSameY))
            throw new InvalidPositionException(
                "Illegal orientation: not all tiles are in a line"
            );
    }

    /*
    this method sets up the boardSpecialCell field with all the correct placements
    for modifier cells using Point objects and model.ModifierType enumerations.
 */
    private void initializeModifierCells() {
        boardSpecialCell = new HashMap<>();
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

    private boolean isValidWord(Set<Point> originPoints, Tile[] newTiles, Point[] newTilePoints) throws InvalidPositionException {
        ArrayList<String> strings = stringBuild(originPoints,newTiles,newTilePoints);
                for(String string: strings) {
            if (!dictionary.contains(string)) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<String> importDictionary(){
        ArrayList<String> list = new ArrayList<>();
        try{
            File dictionary = new File("./code/dictionary.txt");
            Scanner scanner = new Scanner(dictionary);
            while(scanner.hasNext()){
                list.add(scanner.nextLine());
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    /*
    takes a players chosen tiles and returns
    the top most and left most tiles of the given list
    and adds to board
    */
    public Set<Tile> findOrigin(Tile[] tiles) throws InvalidPositionException {
        Set<Tile> parentTile = new HashSet<>();
        //adds tile to board for the purpose of finding previous tile location
        addToBoard(tiles);

        for( Tile tile : tiles){
            int row = (int)tile.getLocation().getX();
            int column = (int)tile.getLocation().getY();
            int tempRow = 0;
            int tempColumn = 0;
            //gets new tiles top and left most row and column
            while(board[row][column] != null){
                row = row - 1;

                if(board[row][column] == null){
                    tempRow = row+1;
                    String letter = board[tempRow][column].getLetter() + "";
                }
            }
            row = (int)tile.getLocation().getX();
            while(board[row][column] != null){
                column = column - 1;

                if(board[row][column] == null){
                    tempColumn = column+1;
                    String letter = board[row][tempColumn].getLetter() + "";
                }
            }
            column = (int)tile.getLocation().getY();
            Tile top = board[tempRow][column];
            Tile left = board[row][tempColumn];
            if(!top.getLocation().equals(tile.getLocation()) ) {
                parentTile.add(top);
            }
            if(!left.getLocation().equals(tile.getLocation())){
                parentTile.add(left);
            }
        }
        removeTiles(tiles);
        return parentTile;
    }


}
//    public boolean isValid(Set<Tile> tiles){
//        //take top and left most tile and run down and right creating word
//        ArrayList<String> strings = stringBuild(tiles);
//        for(String string: strings) {
//            if (!dictionary.contains(string)) {
//                return false;
//            }
//        }
//        return true;
//    }
//    /*
//    constructs string from tile set
//     */
//    public ArrayList<String> stringBuild(Set<Tile> tiles){
//        //take top and left most tile and run down and right creating word
//        ArrayList<String> string = new ArrayList<>();
//        for(Tile tile : tiles){
//            String tempString = "";
//            int row = (int)tile.getLocation().getX();
//            int column = (int)tile.getLocation().getY();
//
//            while(board[row][column] != null){
//                tempString += board[row][column].getLetter();
//                row = row + 1;
//            }
//            row = (int)tile.getLocation().getX();
//            if(tempString.length() > 1){
//                string.add(tempString);
//            }
//            tempString = "";
//            while(board[row][column] != null){
//                tempString += board[row][column].getLetter();
//                column = column + 1;
//            }
//            if(tempString.length() > 1){
//                string.add(tempString);
//            }
//        }
//        return string;
//
//    }
 /*

    public model.Board(){
        board = new model.Tile[15][15];
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
            board[row][column] = new model.Tile(letter.toUpperCase(),letterKeyValue.get(letter.toUpperCase()), new Point(row,column));
        } else {
            System.out.println("Invalid position or tile already exists at (" + row + ", " + column + ").");
        }
    }

    public static void main(String[] args) {
        model.Board test = new model.Board();
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
        List<model.Tile> tiles = new ArrayList<>();
        List<List<model.Tile>> words = new ArrayList<>();
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
            List<model.Tile> tempTiles = new ArrayList<>();
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
  */




