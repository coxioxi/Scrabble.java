package scrabble.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrabble.model.*;
import scrabble.network.host.TileBag;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class GameTest {
    TileBag tileBag;
    Game game;
    @BeforeEach
    public void startGame(){
        tileBag = new TileBag();
        game = new Game(new Player[]{new Player("Player 1",0,0),
                new Player("Player 2",1,2),new Player("Player 3",2,1)  },
                new Board(),
                new Ruleset(99,99,false,"dictionary.txt"),
                new LocalPlayer("Player 1",0,0));
        game.setActive(0,true);
        game.setActive(1,true);
        game.setActive(2,true);


    }
    @Test
    public void isGameOverTest(){
        Assertions.assertFalse(game.isGameOver(),"Game should not be over upon game initialization");
        game.passTurn(1);
        game.passTurn(1);
        Assertions.assertFalse(game.isActive(1),"player should be inactive"); //the Casting of network player is creating an exception
        game.passTurn(0);
        game.passTurn(0);
        game.passTurn(2);
        game.passTurn(2);
        Assertions.assertFalse(game.isGameOver(),"Game should not be over upon game initialization");

    }
    @Test
    public void RulesetTest(){
        HashSet<String> dictionary = readInDictionary();
        boolean word = dictionary.contains("WORD");
        System.out.println(dictionary.isEmpty());
        Assertions.assertTrue(isWordInDictionary(new String[]{"WORD","LONGEST"}),"should return true for the words in the scrabble dictionary");
    }

    public HashSet<String> readInDictionary(){
        HashSet<String> list = new HashSet<>();
        try{
            File dictionary = new File("C:\\Users\\jyelm\\IdeaProjects\\comp3100-fall2024-2\\code\\dictionary.txt");
            Scanner scanner = new Scanner(dictionary);
            while (scanner.hasNext()){
                list.add(scanner.nextLine());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    public boolean isWordInDictionary(String[] words) {
        for(String word: words){
            if(!readInDictionary().contains(word)){
                return false;
            }
        }
        return true;
    }

}
