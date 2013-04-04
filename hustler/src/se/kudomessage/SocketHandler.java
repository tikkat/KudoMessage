package se.kudomessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
			String inputLine;
			
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				while (true) {
					inputLine = in.readLine();
					if (inputLine == null)
						continue;
					
					System.out.println("### In from socket: " + inputLine);
					
					switch (inputLine) {
						case "INITIALIZE":					cu = new ClientUser(in.readLine(), out, in);
						break;
						case "REGISTER_ANDROID_DEVICE":		cu.registerAndroidDevice(in.readLine());
						break;
						case "SEND_MESSAGE":				cu.sendMessage(in.readLine());
						break;
						case "SENT_MESSAGE":				cu.sentMessage(in.readLine());
						break;
						case "RECEIVED_MESSAGE":			cu.receivedMessage(in.readLine());
						break;
					}
					
					if (inputLine.equals("CLOSE")) {
						out.write("OK");
						break;
					}
				}
				
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}