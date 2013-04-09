package se.kudomessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler {
	public SocketHandler() throws IOException {
		ServerSocket serverSocket = null;
		boolean listening = true;

		try {
			serverSocket = new ServerSocket(Constants.SOCKETPORT);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + Constants.SOCKETPORT + ".");
			System.exit(-1);
		}

		while (listening)
			new ClientHandler(serverSocket.accept()).start();

		serverSocket.close();
	}

	private class ClientHandler extends Thread {
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		
		private ClientUser cu;

		public ClientHandler(Socket socket) {
			this.socket = socket;
			System.out.println("### New connection from: " + socket.getRemoteSocketAddress().toString());
		}

		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				OutputStreamWriter outstream = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				out = new PrintWriter(outstream, true);

				cu = new ClientUser(in, out, socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}