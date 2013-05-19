package se.kudomessage.hustler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.*;

public class ClientHandler {
	private BufferedReader in;
	private PrintWriter out;
	
	private String email;
	private String accessToken;
	
	private GmailController gc;
	
	public ClientHandler(Socket socket, BufferedReader in, PrintWriter out) {
		System.out.println("A new client.");
		
		this.in = in;
		this.out = out;
		
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
				try {
					in.close();
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			}

			input = new JSONObject(inputString);

			switch(input.getString("action")) {
				case "init":				init(input);
				break;
				case "send-message":		sendMessage(input);
				break;
				case "get-contacts":		getContacts();
				break;
				case "get-messages":		getMessages(input);
				break;
			}
		}
	}
	
	public void init(JSONObject input) {
		accessToken = input.getString("token");
		email = Utils.getEmailByToken(accessToken);
		
		gc = GmailController.getInstance(email, accessToken);
		
		out.println(email);
		out.flush();
		
		PushHandler.registerClient(email, this);
	}
	
	public void getContacts() {
		JSONArray contacts = GoogleContactsController.getContacts(email, accessToken);
		
		JSONObject output = new JSONObject();
		output.put("contacts", contacts);
		
		out.println(output.toString());
		out.flush();
	}
	
	public void getMessages(JSONObject input) {
		int lower = input.getInt("lower");
		int upper = input.getInt("upper");
		
		JSONArray messages = gc.getMessages(lower, upper);
		
		JSONObject output = new JSONObject();
		output.put("messages", messages);
		
		out.println(output.toString());
		out.flush();
	}
	
	public void sendMessage(JSONObject input) {
		JSONObject message = input.getJSONObject("message");
		PushHandler.pushMessageToDevice(email, message);
	}
	
	public void pushMessage(JSONObject message) {
		JSONObject output = new JSONObject();
		output.put("action", "new-message");
		output.put("message", message);
		
		out.println(output.toString());
		out.flush();
	}
}
