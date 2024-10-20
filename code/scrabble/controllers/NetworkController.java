package scrabble.controllers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkController {
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream out;

	public NetworkController(String address, int port) {
		try {
			socket = new Socket(address, port);
			System.out.println("Connected");
			input = new DataInputStream(System.in);
			out = new DataOutputStream(
					socket.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String line = "";
		while (!line.equals("End")) {

			try {
				line = input.readLine();
				out.writeUTF(line);
			}
			catch (IOException i) {
				System.out.println(i);
			}

			try {
				input.close();
				out.close();
				socket.close();
			}

			catch (IOException i) {
				System.out.println(i);
			}
		}
	}

	public static void main(String[] args)
	{
		NetworkController client
				= new NetworkController(args[0], Integer.parseInt(args[1]));
	}

}
