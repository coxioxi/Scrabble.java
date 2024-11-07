package scrabble.testing;

//import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import scrabble.model.*;

import scrabble.model.Tile;
import scrabble.network.client.ClientMessenger;
import scrabble.network.host.PartyHost;
import scrabble.network.host.TileBag;
import scrabble.network.messages.Message;
import scrabble.network.messages.NewTiles;
import scrabble.network.messages.PlayTiles;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class NetworkTest {
    /*
     *
     * 1. Constructor - PartyHost(int port)
     *    Test Cases:
     *    Verify that creating a PartyHost instance with a valid port sets up the host correctly.
     *    Test how the constructor behaves with an invalid port (e.g., negative numbers or out-of-range ports).
     *    2. getIPAddress()
     *    Test Cases:
     *    Ensure this method returns the correct IP address format.
     *    Confirm behavior if the IP address cannot be determined (e.g., mock a network exception).
     *    3. getPort()
     *    Test Cases:
     *    Verify that this method returns the correct port number as set during initialization.
     *
     *    5. propertyChange(PropertyChangeEvent evt)
     *    Test Cases:
     *    Check that changes are correctly detected and acted upon based on the property change events.
     *    Test various types of PropertyChangeEvent events to ensure expected behavior.
     *    6. getPlayerID(HostReceiver hr)
     *    Test Cases:
     *    Verify that this method assigns and returns a unique player ID for each client.
     *    Test behavior when given a null or invalid HostReceiver.
     *    7. acceptClients()
     *    Test Cases:
     *    Confirm that this method successfully accepts clients and establishes connections.
     *    Test how it handles network issues or full capacity (e.g., max players limit, if applicable).
     *
     * */
    PartyHost partyHost;
    ClientMessenger clientMessenger;
    Tile[] score;
    String host = "192.168.1.182";
    int port = 5000;
    TileBag tileBag;
    Message message;
    @BeforeEach
    public void clientSetup() throws IOException {
        tileBag = new TileBag();


        Socket socket;
        try {
            socket = new Socket(host, port);
        }
        catch (IOException e) {
            System.out.print("Error in socket: " + e);
            throw new RuntimeException(e);
        }

        clientMessenger = new ClientMessenger(socket, evt -> {
            Message.printInstance((Message) evt.getNewValue());
        });

        Thread thread = new Thread(clientMessenger);
        thread.start();

        String messageType;

    }
    @BeforeEach
    public void hostSetup(){
        PartyHost partyHost = new PartyHost(port);
        Thread thread = new Thread(partyHost);
        thread.start();
    }

    @Test
    public void messages() throws IOException {
        Tile[] tiles = tileBag.getNext(5);

        clientMessenger.sendMessage(new PlayTiles(1,1, tiles));
        //Message.printInstance(message);
        NewTiles test = (NewTiles)message;
        System.out.println(test.getTiles().length);
        Assertions.assertInstanceOf(NewTiles.class, message, "Doesn't Works");
    }
}
