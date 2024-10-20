package scrabble.host;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PartyHost implements Runnable {

	private Socket[] socket;
	private BufferedOutputStream[] outputStreams;
	private ServerSocket server;
	private boolean inGame;

	public PartyHost(int port) {
		inGame = false;
		server = null;
		try {
			server = new ServerSocket(port);
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
				Socket client = server.accept();
				clientSockets.add(client);
				System.out.println("New client added");
				Runnable clientHandler = new ClientHandler(client);
				Thread clientThread = new Thread(clientHandler);
				clientThread.start();
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void main(String[] args) {
		int port = 5000;
		Thread thread = new Thread(new PartyHost(port));
		thread.start();
	}
}
