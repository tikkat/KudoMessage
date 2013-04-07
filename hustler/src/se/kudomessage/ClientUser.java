package se.kudomessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import javax.mail.MessagingException;

import org.json.*;

public class ClientUser {
	private String token;
	private String username;
	private String userID;
	
	private PrintWriter out;
	private BufferedReader in;
	private Socket socket;
	
	private GmailHandler gmailHandler;

	public ClientUser(BufferedReader in, PrintWriter out, Socket socket) {
		this.in = in;
		this.out = out;
		this.socket = socket;
		
		try {
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void run() throws IOException {
		JSONObject input;
		
		while (true) {
			String inputString = in.readLine();
			
			if (inputString.equals("CLOSE")) {
				in.close();
				out.close();
				socket.close();
				
				break;
			}
			
			input = new JSONObject(inputString);
			
			switch(input.getString("action")) {
				case "init":					init(input);
				break;
				case "registerAndroidDevice":	registerAndroidDevice(input);
				break;
				case "sendMessage":				sendMessage(input);
				break;
				case "sentMessage":				sentMessage(input);
				break;
				case "receivedMessage":			receivedMessage(input);
				break;
				case "getMessages":				getMessages(input);
				break;
			}
		}
	}
	
	private void getMessages(JSONObject input) {
		out.write(gmailHandler.getMessages(input).toString());
	}
	
	private void init(JSONObject input) {
		this.token = input.getString("token");
		
		System.out.println("Creating a new ClientUser with token: " + token);
		
		if (!isTokenValid(token)) {
			out.write("TOKEN_INVALID");
			out.flush();
			
			return;
		}
		
		getUserInfo();
		
		try {
			gmailHandler = new GmailHandler(token, username);
			PushHandler.addClientUser(userID, this);
			
			out.write("OK");
			out.flush();
		} catch (MessagingException e) {
			out.write("ERROR_GMAIL");
			out.flush();
			
			e.printStackTrace();
		}
		
		System.out.println("Made a new ClientUser");
	}

	private boolean isTokenValid(String token) {
		return true;
	}

	public void getUserInfo() {
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token;
		String result = getContentOfUrl(url);

		JSONObject info = new JSONObject(result);
		username = info.getString("email");
		userID = info.getString("id");
		
		System.out.println("Got user info: " + username + ", " + userID);
	}

	private String getContentOfUrl(String url) {
		try {
			InputStream is = new URL(url).openStream();
			Scanner scanner = new Scanner(is, "UTF-8");

			String result = scanner.useDelimiter("\\A").next();

			scanner.close();
			is.close();

			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public void registerAndroidDevice(JSONObject input) {
		PushHandler.registerAndroidDevice(userID, input.getString("gcm"));
	}

	public void sendMessage(JSONObject input) {
		System.out.println("Running sendMessage with indata: " + input.toString());
		
		String message = input.getString("message");
		String receiver = input.getString("receiver");
		
		int messageID = gmailHandler.saveMessageToPending(message, receiver, username);
		
		if (messageID > 0) {
			PushHandler.notifyAndroidDeviceNewMessage(userID, messageID);
			
			out.write("OK");
			out.flush();
		} else {
			out.write("ERROR");
			out.flush();
		}
	}

	public void sentMessage(JSONObject input) {
		try {
			gmailHandler.moveMessage(input.getInt("messageID"), GmailHandler.Labels.PENDING, GmailHandler.Labels.STANDARD);
			out.write("OK");
			out.flush();
		} catch (MessagingException e) {
			out.write("ERROR");
			out.flush();
		}
	}

	public void receivedMessage(JSONObject input) {
		// GET MESSAGE FROM GMAIL WITH ID input.getInt("messageID")
		String message = "";
		String sender = "";
		
		PushHandler.tellClientNewMessage(userID, message, sender);
	}
}