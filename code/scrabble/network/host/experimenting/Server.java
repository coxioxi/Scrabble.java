package scrabble.network.host.experimenting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/*
cd "OneDrive - Otterbein University/IdeaProjects/Scrabble/code"
javac ./scrabble/network/host/experimenting/Server.java
java ./scrabble/network/host/experimenting/Server
 */
public class Server {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 5000;

		System.out.println("Your IP: " + Inet4Address.getLocalHost().getHostAddress());
		System.out.println("Listening at port " + port);
		System.out.println("Looking for clients...");
		ServerSocket server = new ServerSocket(port);
		Socket client = server.accept();
		ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
		Object object = inputStream.readObject();
		if (object instanceof String) {
			System.out.println((String) object);
		}
		inputStream.close();
	}
}
