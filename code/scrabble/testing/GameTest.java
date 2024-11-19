package scrabble.testing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scrabble.model.*;
import scrabble.network.PartyHost;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class GameTest {
    PartyHost.TileBag tileBag;
    Game game;
    @BeforeEach
    public void startGame(){
        tileBag = new PartyHost.TileBag();
        game = new Game(new Player[]{new Player("Player 1",0,0),
                new Player("Player 2",1,2),new Player("Player 3",2,1)  },
                new Board(),
                new Ruleset(99,99,false,"dictionary.txt"),
                new Player.LocalPlayer("Player 1",0,0));
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
    public void passTurnTest(){
        game.passTurn(game.getCurrentPlayer());
        Assertions.assertEquals(game.getCurrentPlayer()+1,game.getCurrentPlayer());

        game.passTurn(game.getCurrentPlayer()-1);
        Assertions.assertEquals(game.getCurrentPlayer()+2,game.getCurrentPlayer());

        game.passTurn(game.getCurrentPlayer()-1);
        game.isActive(game.getCurrentPlayer()-1);


    }

    @Test
    public void RulesetTest(){
        //Valid words
        Assertions.assertTrue(isWordInDictionary(new String[]{"WORD","LONGEST"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"NICE"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"NONE"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"EVEN"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"EVENING"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"ICE"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"GONE"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"RE", "EON", "OR"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"NO", "OVAL"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"CARE", "NOR"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"ET", "AT"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"MINI"}));
        Assertions.assertTrue(isWordInDictionary(new String[]{"MA", "MAN", "IN"}));

        //Invalid words
        Assertions.assertFalse(isWordInDictionary(new String[]{"MA", "EON", "THYOLR"}));
        Assertions.assertFalse(isWordInDictionary(new String[]{"EVEN", "ET", "THEMPOIPIH"}));
        Assertions.assertFalse(isWordInDictionary(new String[]{"RE", "EON", "HELLOINU"}));
        Assertions.assertFalse(isWordInDictionary(new String[]{"GONE", "EON", "XORL"}));
        Assertions.assertFalse(isWordInDictionary(new String[]{"TONELA"}));
        Assertions.assertFalse(isWordInDictionary(new String[]{"PIROCA"}));
    }

    public HashSet<String> readInDictionary(){
        HashSet<String> list = new HashSet<>();
        try{
            File dictionary = new File("code\\dictionary.txt").getAbsoluteFile();
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
