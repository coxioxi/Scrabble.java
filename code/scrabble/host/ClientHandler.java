package scrabble.host;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable{

	private Socket clientSocket;
	private DataInputStream inputStream;

	public ClientHandler(Socket clientSocket)
			throws IOException {
		this.clientSocket = clientSocket;
		this.inputStream = new DataInputStream(
				new BufferedInputStream(
						clientSocket.getInputStream()
				)
		);
	}

	@Override
	public void run() {
		String line = null;
		try {
			line = inputStream.readUTF();
		} catch (IOException e) {
			try {
				this.clientSocket.close();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		if (line != null) {
			System.out.println("Client says: " + line);
		}
	}
}
