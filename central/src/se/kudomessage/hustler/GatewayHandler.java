package se.kudomessage.hustler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

public class GatewayHandler {
	private PrintWriter out;

	public GatewayHandler(Socket socket, BufferedReader in, PrintWriter out) {
		this.out = out;
		
		System.out.println("A new gateway.");
		
		String inputString;
		JSONObject input;
		
		while (true) {
			try {
				inputString = in.readLine();
			} catch (IOException e) {
				continue;
			}

			if (inputString == null || inputString.isEmpty())
				continue;

			if (inputString.equals("CLOSE")) {
				System.out.println("Closing connection " + socket.getRemoteSocketAddress());
				
				try {
					in.close();
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			}

			try {
				input = new JSONObject(inputString);
				
				switch(input.getString("action")) {
					case "register-device":				registerDevice(input);
					break;
					case "push-message":				pushMessage(input);
					break;
					case "test-server":					testServer();
					break;
				}
			} catch (Exception e) {
				System.out.println("### ERROR WITH GATEWAY ### " + e + " ### IN FROM GATEWAY: " + inputString);
			}
		}
	}
	
	public void testServer() {
		out.println("TEST_OK");
		out.flush();
	}
	
	public void registerDevice(JSONObject input) {
		String token = input.getString("token");
		String email = Utils.getEmailByToken(token);
		
		String GCMKey = input.getString("gcm");
		
		PushHandler.registerDevice(email, GCMKey);
	}
	
	public void pushMessage(JSONObject input) {
		String token = input.getString("token");
		String email = Utils.getEmailByToken(token);
		
		JSONObject message = input.getJSONObject("message");
		
		// Upload the message to Gmail
		GmailController gc = GmailController.getInstance(email, token);
		gc.saveMessage(GmailController.Label.STANDARD, message);
		
		PushHandler.pushMessageToClients(email, message);
	}
}
