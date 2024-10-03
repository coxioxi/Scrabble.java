
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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
        letterKeyValue.put(" ",point0);

    }
    public void addToBoard(String letter, int row, int column) {
        board[row][column] = new Tile(letter,letterKeyValue.get(letter), new Point(row,column));

    }

    public static void main(String[] args) {
        Board test = new Board();
    }
    public void boardScan(){
        
    }

}
