package se.kudomessage.hustler.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

import se.kudomessage.hustler.Utils;
import se.kudomessage.hustler.controllers.GmailController;

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
				System.err.println("Error with gateway: " + e + " Error message: " + inputString);
			}
		}
	}
	
	public void testServer() {
		out.println("TEST_OK");
		out.flush();
	}
	
	public void registerDevice(JSONObject input) {
		try {
			String token = input.getString("token");
			String email = Utils.getEmailByToken(token);
			
			String GCMKey = input.getString("gcm");
			
			PushHandler.registerDevice(email, GCMKey);
		} catch (Exception e) {
			System.err.println("Error in register device: " + e);
		}
	}
	
	public void pushMessage(JSONObject input) {
		try {
			String token = input.getString("token");
			String email = Utils.getEmailByToken(token);
			
			JSONObject message = input.getJSONObject("message");
			
			// Upload the message to Gmail
			GmailController gc = GmailController.getInstance(email, token);
			gc.saveMessage(GmailController.Label.STANDARD, message);
			
			PushHandler.pushMessageToClients(email, message);
		} catch (Exception e) {
			System.err.println("Error in pushMessage: " + e);
		}
	}
}
