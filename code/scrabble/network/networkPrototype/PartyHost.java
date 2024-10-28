package scrabble.network.networkPrototype;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

/*
this class is the server side
Usage: 	javac scrabble/network/networkPrototype/PartyHost.java
		java scrabble/network/networkPrototype/PartyHost
		Use ../controllers/NetworkController as client
David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"

 */
public class PartyHost implements Runnable {

	private Socket[] socket;
	private BufferedOutputStream[] outputStreams;
	private ServerSocket server;
	private boolean inGame;
	private ArrayList<Thread> listeners;
	private ArrayList<ClientHandler> handlers;

	public PartyHost(int port) {
		inGame = false;
		server = null;
		listeners = new ArrayList<>(4);
		handlers = new ArrayList<>(4);
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(1000);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void startGame() {
		this.inGame = true;
	}

	@Override
	public void run() {
		ArrayList<Socket> clientSockets = new ArrayList<>(4);
		while (!inGame) {
			try {
				System.out.println("Looking for clients...");
				Socket client = server.accept();
				clientSockets.add(client);
				System.out.println("New client added");
				ClientHandler clientHandler = new ClientHandler(client, this);
				handlers.add(clientHandler);
				Thread clientThread = new Thread(clientHandler);
				listeners.add(clientThread);
				clientThread.start();
			}
			catch (SocketTimeoutException e) {
				System.out.println("\ttrying again..");
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		try {
			System.out.println("Game started");
			Thread.sleep(1000);
			System.out.println("...");
			Thread.sleep(1000);
			System.out.println("Game has ended");
			for (Thread t : listeners) {
				t.join();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void handleMessage(String message, Socket clientSocket) {
		if (message.equals("End")) {
			System.out.println("Client Socket closed");

		}
		else if (message.equals("Start")) {
			System.out.println("Clients says start");
			this.startGame();
			System.out.println(this.inGame);
		}
	}

	public static void main(String[] args) throws UnknownHostException {
		int port = 5000;

		System.out.println("Your IP: " + Inet4Address.getLocalHost().getHostAddress());
		System.out.println("Listening at port " + port);

		Thread thread = new Thread(new PartyHost(port));
		thread.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}
}
