package se.kudomessage.hustler;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import org.json.JSONObject;

public class Main {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(CONSTANTS.SOCKETPORT);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + CONSTANTS.SOCKETPORT + ".");
			System.exit(1);
		}
		
		System.out.println("Listening on port: " + CONSTANTS.SOCKETPORT + ".");
		
		while (!Thread.interrupted()) {
			try {
				new ConnectionHandler(serverSocket.accept()).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {}
	}
}
