package se.kudomessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.mail.MessagingException;

import org.json.*;

public class ClientUser {
	private String token;
	private String username;
	private String userID;
	
	private GmailHandler gmailHandler;

	public ClientUser(String token, PrintWriter out) {
		if (!isTokenValid(token)) {
			out.write("TOKEN_INVALID");
			out.flush();
			
			return;
		}
		
		this.token = token;
		getUserInfo();
		
		try {
			gmailHandler = new GmailHandler(token, username);
			
			out.write("OK");
			out.flush();
		} catch (MessagingException e) {
			out.write("ERROR_GMAIL");
			out.flush();
			
			e.printStackTrace();
		}
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

	public void registerServer(String readLine, PrintWriter out) {
		JSONObject input = new JSONObject(readLine);
	}

	public void sendMessage(String readLine, PrintWriter out) {
		JSONObject input = new JSONObject(readLine);
		String message = input.getString("message");
		String receiver = input.getString("receiver");
		
		int messageID = gmailHandler.saveMessageToPending(message, receiver, username);
		
		if (messageID > 0) {
			out.write("OK");
			out.flush();
		} else {
			out.write("ERROR");
			out.flush();
		}
	}

	public void sentMessage(String readLine, PrintWriter out) {
		try {
			gmailHandler.moveMessage(Integer.parseInt(readLine), GmailHandler.Labels.PENDING, GmailHandler.Labels.STANDARD);
			out.write("OK");
			out.flush();
		} catch (MessagingException e) {
			out.write("ERROR");
			out.flush();
		}
	}

	public void receivedMessage(String readLine, PrintWriter out) {
		JSONObject input = new JSONObject(readLine);
		String message = input.getString("message");
		String sender = input.getString("sender");
		
		int messageID = gmailHandler.saveMessageToPending(message, username, sender);
		
		if (messageID > 0) {
			out.write("OK");
			out.flush();
		} else {
			out.write("ERROR");
			out.flush();
		}
	}
}