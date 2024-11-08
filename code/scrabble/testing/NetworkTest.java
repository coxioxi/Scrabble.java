package scrabble.testing;

//import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import scrabble.model.Tile;
import scrabble.network.client.ClientMessenger;
import scrabble.network.host.PartyHost;
import scrabble.network.host.TileBag;
import scrabble.network.messages.*;

import java.io.IOException;
import java.net.Socket;

public class NetworkTest {
    PartyHost partyHost;
    ClientMessenger clientMessenger0;
    ClientMessenger clientMessenger1;
    ClientMessenger clientMessenger2;
    ClientMessenger clientMessenger3;
    ClientMessenger clientMessenger4;

    Tile[] score;
    String host = "192.168.1.182";
    int port = 5000;
    TileBag tileBag;
    NetworkTestListener listener;

    @BeforeEach
    public void hostSetup() throws IOException {
        partyHost = new PartyHost(port);
        Thread thread = new Thread(partyHost);
        thread.start();
        partyHost.startGame();
    }

    @Nested
    public class MessageTests{
        @BeforeEach
        public void clientSetup() throws IOException {
            tileBag = new TileBag();
            listener = new NetworkTestListener();
            clientMessenger0 = new ClientMessenger(new Socket(host,port),listener);
            clientMessenger1 = new ClientMessenger(new Socket(host,port),listener);
            clientMessenger2 = new ClientMessenger(new Socket(host,port),listener);
            clientMessenger3 = new ClientMessenger(new Socket(host,port),listener);

            Thread thread0 = new Thread(clientMessenger0);
            Thread thread1 = new Thread(clientMessenger1);
            Thread thread2 = new Thread(clientMessenger2);
            Thread thread3 = new Thread(clientMessenger3);
            thread0.start();
            thread1.start();
            thread2.start();
            thread3.start();
        }

        @Test
        public void PlayTilesTest() throws IOException {
            Tile[] tiles = tileBag.getNext(5);

            PlayTiles playTiles = new PlayTiles(0,0, tiles);
            clientMessenger0.sendMessage(playTiles);

        }
        @Test
        public void ExchangeTilesTest() throws IOException{
            Tile[] tiles = new Tile[]{new Tile('A'),new Tile('D'),new Tile('F')};
            ExchangeTiles exchangeTiles = new ExchangeTiles(0,0,tiles);
            clientMessenger0.sendMessage(exchangeTiles);

        }
        @Test
        public void ExitPartyTest() throws IOException{
            ExitParty exitParty = new ExitParty(0,3);
            clientMessenger0.sendMessage(exitParty);
        }
        @Test
        public void NewPlayerTest() throws IOException{
            NewPlayer newPlayer = new NewPlayer(0,0,"player1");
            clientMessenger0.sendMessage(newPlayer);
        }
        @Test
        public void PassTurnTest() throws IOException {
            PassTurn passTurn = new PassTurn(3,3);
            clientMessenger0.sendMessage(passTurn);
        }
        @Test
        public void StartGameTest() throws IOException{
            partyHost.startGame();
        }


    }
    @Test
    public void clientSetupTest() throws IOException {
        tileBag = new TileBag();
        listener = new NetworkTestListener();
        clientMessenger0 = new ClientMessenger(new Socket(host,port),listener);
        clientMessenger1 = new ClientMessenger(new Socket(host,port),listener);
        clientMessenger2 = new ClientMessenger(new Socket(host,port),listener);
        clientMessenger3 = new ClientMessenger(new Socket(host,port),listener);
        clientMessenger4 = new ClientMessenger(new Socket(host,port),listener);

        Thread thread0 = new Thread(clientMessenger0);
        Thread thread1 = new Thread(clientMessenger1);
        Thread thread2 = new Thread(clientMessenger2);
        Thread thread3 = new Thread(clientMessenger3);
        Thread thread4 = new Thread(clientMessenger4);
        thread0.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

    }


}

class NetworkTestListener implements PropertyChangeListener{
    public int tileNumber;
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getNewValue() instanceof ExchangeTiles exchangeTiles){
            System.out.println(exchangeTiles.getPlayerID());
        } else if (evt.getNewValue() instanceof PassTurn passTurn) {
            System.out.println(passTurn.getPlayerID());
        }else if (evt.getNewValue() instanceof NewTiles newtiles) {
            System.out.println(newtiles.getTiles().length);
            String string = "tiles are: ";
            for(Tile tile: newtiles.getTiles()){
                string += tile.getLetter() +", ";
            }
            System.out.println(string);
        }else if (evt.getNewValue() instanceof ExitParty exitParty) {
            System.out.println(exitParty.getPlayerID());
        }else if (evt.getNewValue() instanceof PlayTiles playTiles) {
            tileNumber = playTiles.getTiles().length;
        }else if (evt.getNewValue() instanceof NewPlayer newPlayer) {
            System.out.println(newPlayer.getPlayerName());
        } else if (evt.getNewValue() instanceof StartGame startGame) {
            String startTiles = "Starting tiles are: ";
            String playerIDs = "Players: ";
            for(Tile tile: startGame.getStartingTiles()){
                startTiles += tile.getLetter() + " ";
            }
            for(int id: startGame.getPlayerIDs()){
                playerIDs += id + ", ";
            }
            System.out.println("Player: "+ startGame.getReceivingID());
            System.out.println(startTiles);
            System.out.println(playerIDs);

        }

    }


}
