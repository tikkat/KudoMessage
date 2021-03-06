package se.kudomessage.hustler.handlers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.*;

import se.kudomessage.hustler.controllers.GmailController;
import se.kudomessage.hustler.controllers.GoogleContactsController;
import se.kudomessage.hustler.utils.Utils;

public class ClientHandler {
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private String email;
	private String token;
	
	private GmailController gc;
	
	public ClientHandler(Socket socket, BufferedReader in, PrintWriter out) {
		System.out.println("A new client.");
		
		this.socket = socket;
		this.in = in;
		this.out = out;
		
		String inputString;
		JSONObject input;
		
		int countErrors = 0;
		
		while (!Thread.interrupted()) {
			if (countErrors > 5) {
				System.err.println("More errors then 5, closing socket.");
				break;
			}
			
			try {
				inputString = in.readLine();
			} catch (IOException e) {
				countErrors++;
				continue;
			}

			if (inputString == null || inputString.isEmpty()) {
				countErrors++;
				continue;
			}
			
			countErrors = 0;

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
					case "create-contact":		createContact(input);
					break;
				}
			} catch (JSONException e) {}
		}
	}
	
	public void init(JSONObject input) {
		try {
			token = input.getString("token");
			email = Utils.getEmailByToken(token);
			
			if (email == null || email.equals("")) {
				System.out.println("### init() ## Got no email, closing...");
				
				Thread.currentThread().interrupt();
				return;
			}
			
			gc = GmailController.getInstance(email, token);
			
			out.println(email);
			out.flush();
			
			PushHandler.registerClient(email, this);
		} catch (JSONException e) {
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
	
	public void createContact(JSONObject input) {
		try {
			String name = input.getString("name");
			String number = input.getString("number");
			
			GoogleContactsController.createContact(email, token, name, number);
		} catch (JSONException e) {
			System.err.println("Error in createContact: " + e);
		}
	}
	
	public void getMessages(JSONObject input) {
		try {
			int lower = input.getInt("lower");
			int count = input.getInt("count");
		
			JSONArray messages = gc.getMessages(lower, count);
		
			JSONObject output = new JSONObject();
			output.put("messages", messages);
		
			out.println(output.toString());
			out.flush();
		} catch (Exception e) {
			System.err.println("Error in getMessages: " + e);
		}
	}
	
	public void sendMessage(JSONObject input) {
		try {
			JSONObject message = input.getJSONObject("message");
			PushHandler.pushMessageToDevice(email, message);
		} catch (Exception e) {
			System.out.println("Error in sendMessage: " + e);
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
			System.out.println("Error in pushMessage: " + e);
		}
	}
}
