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

/**
 * A class for handling a client connected via a socket.
 * The client sends a JSON object which triggers functions in this class.
 */
public class ClientUser {
	private String token;
	private String username;
	private String userID;
	
	private PrintWriter out;
	private BufferedReader in;
	private Socket socket;
	
	private GmailHandler gmailHandler;

	/**
	 * Sets some local variables and start the function run().
	 * 
	 * @param in a BufferedReader connected to the socket 
	 * @param out a PrintWriter connected to the socket 
	 * @param socket the socket to which a client is connected
	 */
	public ClientUser(BufferedReader in, PrintWriter out, Socket socket) {
		System.out.println("Creating a new ClientUser.");
		
		this.in = in;
		this.out = out;
		this.socket = socket;
		
		try {
			run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The functions which listens to the socket stream and runs the functions triggered by the clients message.
	 * 
	 * @throws IOException
	 */
	private void run() throws IOException {
		JSONObject input;
		
		while (true) {
			String inputString = in.readLine();
			
			if (inputString == null || inputString.isEmpty())
				continue;
			
			System.out.println("### In from socket: " + inputString);
			
			if (inputString.equals("CLOSE")) {
				System.out.println("Closed the connection with " + socket.getRemoteSocketAddress().toString());
				
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
	
	/**
	 * Downloads emails from the Gmail account, given a specific range and output them to the socket.
	 * 
	 * @param input a JSON object with the range of which the client wants emails.
	 */
	private void getMessages(JSONObject input) {
		out.write(gmailHandler.getMessages(input).toString());
	}
	
	/**
	 * Gets the user info and creates a new instance of GmailHandler.
	 * 
	 * @param input a JSON object containing a valid access token
	 */
	private void init(JSONObject input) {
		System.out.println("Running init() with access token " + input.getString("token"));
		
		if (!isTokenValid(token)) {
			return;
		}
		
		this.token = input.getString("token");
		
		getUserInfo();
		
		try {
			gmailHandler = new GmailHandler(token, username);
			PushHandler.addClientUser(userID, this);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the given access token is valid.
	 * 
	 * @param token the access token to check
	 * @return true if the access token is valid, false otherwise.
	 */
	private boolean isTokenValid(String token) {
		return true;
	}

	/**
	 * Gets the user's username (email address) and user id from Google.
	 */
	public void getUserInfo() {
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token;
		String result = getContentOfUrl(url);

		JSONObject info = new JSONObject(result);
		username = info.getString("email");
		userID = info.getString("id");
		
		System.out.println("Got user info: " + username + ", " + userID);
	}

	/**
	 * Simply downloads the content of given url.
	 * 
	 * @param url the url from which to download the content
	 * @return the content of the url
	 */
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

	/**
	 * Registers an Android device to be associated with this user.
	 * The users id is to be associated with a Google Cloud Messaging id.
	 * 
	 * @param input a JSON object containing the Google Cloud Messaging id
	 */
	public void registerAndroidDevice(JSONObject input) {
		PushHandler.registerAndroidDevice(userID, input.getString("gcm"));
	}

	/**
	 * Uploads a message to Gmail and notifies the server application (gateway) about it.
	 * 
	 * @param input a JSON object containing the message data
	 */
	public void sendMessage(JSONObject input) {
		System.out.println("Running sendMessage with indata: " + input.toString());
		
		String message = input.getString("message");
		String receiver = input.getString("receiver");
		
		String messageID = gmailHandler.saveMessageToPending(message, receiver, username);
		
		if (!messageID.isEmpty()) {
			PushHandler.notifyAndroidDeviceNewMessage(userID, messageID);
			
			System.out.println("Uploaded a new message with id " + messageID);
		} else {
			System.out.println("Couldn't upload the message.");
		}
	}

	/**
	 * When the server application (gateway) sends a message it tells Hustler about it via this function.
	 * This function then moves the message from the label "pending" to the label "standard".
	 * 
	 * @param input a JSON object containing the message id of the sent message
	 */
	public void sentMessage(JSONObject input) {
		try {
			gmailHandler.moveMessage(input.getString("messageID"), GmailHandler.Labels.PENDING, GmailHandler.Labels.STANDARD);
		} catch (MessagingException e) {
		}
	}

	/**
	 * When the server application (gateway) receives a message it tells Hustler about it via this function.
	 * This function then gets the message form Gmail and get it to all the clients associated with the user.
	 * 
	 * @param input a JSON object containing the message id of the sent message
	 */
	public void receivedMessage(JSONObject input) {
		// GET MESSAGE FROM GMAIL WITH ID input.getInt("messageID")
		String message = "";
		String sender = "";
		
		PushHandler.tellClientNewMessage(userID, message, sender);
	}
}