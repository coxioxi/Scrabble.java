package scrabble.network.host.experimenting;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
/*
cd "OneDrive - Otterbein University/IdeaProjects/Scrabble/code"
javac ./scrabble/network/host/experimenting/Client.java
java ./scrabble/network/host/experimenting/Client
 */
public class Client {
	public static void main(String[] args) throws IOException {
		String address = args[0];
		int port = Integer.parseInt(args[1]);
		Socket socket = new Socket(address, port);
		System.out.println("Connected");
		Scanner input = new Scanner(System.in);
		System.out.println("Enter message: ");
		String message = input.next();
		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.writeObject(message);
		outputStream.flush();
		outputStream.close();
	}
}
