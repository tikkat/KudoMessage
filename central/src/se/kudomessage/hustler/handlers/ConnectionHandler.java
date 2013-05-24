package se.kudomessage.hustler.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler extends Thread {
	private Socket socket;
	
	public ConnectionHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		System.out.println("New connection from " + socket.getRemoteSocketAddress());
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			OutputStreamWriter outstream = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			PrintWriter out = new PrintWriter(outstream, true);
			
			for (int i = 0; i < 5; i++) {
				String inputString = in.readLine();

				if (inputString == null || inputString.isEmpty()) {
					continue;
				}

				if (inputString.equals("CLIENT")) {
					new ClientHandler(socket, in, out);
					break;
				} else if (inputString.equals("GATEWAY")) {
					new GatewayHandler(socket, in, out);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
