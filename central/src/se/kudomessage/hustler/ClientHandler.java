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
	private String token;
	
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

			try {
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
					case "add-contact":		  	addContact(input);
					break;
				}
			} catch (JSONException e) {}
		}
	}
	
	public void init(JSONObject input) {
		try {
			token = input.getString("token");
			email = Utils.getEmailByToken(token);
			
			gc = GmailController.getInstance(email, token);
			
			out.println(email);
			out.flush();
			
			PushHandler.registerClient(email, this);
		} catch (JSONException e) {
			System.out.println("ERROR IN INIT: " + e);
			
			out.println("ERROR");
			out.flush();
		}
	}
	
	public void getContacts() {
		JSONArray contacts = GoogleContactsController.getContacts(email, token);
		
		JSONObject output = new JSONObject();
		try {
			output.put("contacts", contacts);
		} catch (JSONException e) {}
		
		out.println(output.toString());
		out.flush();
	}
	
	public void addContact(JSONObject input) {
		try {
			String name = input.getString("name");
			String number = input.getString("number");
			
			GoogleContactsController.addContact(email, token, name, number);
		} catch (JSONException e) {
			System.out.println("ERROR IN ADD-CONTACT: " + e);
		}
	}
	
	public void getMessages(JSONObject input) {
		try {
			int lower = input.getInt("lower");
			int upper = input.getInt("upper");
		
			JSONArray messages = gc.getMessages(lower, upper);
		
			JSONObject output = new JSONObject();
			output.put("messages", messages);
		
			out.println(output.toString());
			out.flush();
		} catch (Exception e) {
			System.out.println("ERROR IN GET-MESSAGES: " + e);
		}
	}
	
	public void sendMessage(JSONObject input) {
		try {
			JSONObject message = input.getJSONObject("message");
			PushHandler.pushMessageToDevice(email, message);
		} catch (Exception e) {
			System.out.println("ERROR IN SEND-MESSAGE: " + e);
		}
	}
	
	public void pushMessage(JSONObject message) {
		try {
			JSONObject output = new JSONObject();
			output.put("action", "new-message");
			output.put("message", message);
			
			out.println(output.toString());
			out.flush();
		} catch (Exception e) {
			System.out.println("ERROR IN PUSH-MESSAGE: " + e);
		}
	}
}
