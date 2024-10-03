
import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {
    private final Tile[][] board;
    private final Map<String,Integer> letterKeyValue = new HashMap<>();
    private final Map<Point,String> boardSpecialCell =  new HashMap<>();

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
        board[row][column] = new Tile(letter.toUpperCase(),letterKeyValue.get(letter.toUpperCase()), new Point(row,column));
    }

    public static void main(String[] args) {
        Board test = new Board();
        test.addToBoard("r",2,9);
        test.addToBoard("u",3,9);
        test.addToBoard("n",4,9);
        test.addToBoard("i",4,10);
        test.addToBoard("g",4,11);
        test.addToBoard("h",4,12);
        test.addToBoard("t",4,13);
        test.boardView();
        System.out.println();
    }
    public void boardView(){
        for(int row = 0; row < board.length; ++row){
            for(int column = 0; column < board[row].length; ++column){
                if(board[row][column] != null){
                    System.out.print(" "+board[row][column].getLetter()+" ");
                }
                else{
                    System.out.print(" X ");
                }
            }
            System.out.println();
        }
    }
    public void boardScan(){
        List<Tile> Tiles = new ArrayList<>();
        for(int row = 0; row < board.length; ++row){
            for(int column = 0; column < board[row].length; ++column){
                if(board[row][column] != null){
                    Tiles.add(board[row][column]);
                }
            }
        }
        //
        for( int i = 0; i < Tiles.size(); ++i){
            for(int j = 0; j < Tiles.size(); ++j){
                
            }
        }
    }
    public void clearBoard(){
        for(int row = 0; row < board.length; ++row){
            Arrays.fill(board[row], null);
            }
        }
    }
