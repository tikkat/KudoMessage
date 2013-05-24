package se.kudomessage.hustler.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import se.kudomessage.hustler.CONSTANTS;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class PushHandler {
	private static Map<String, List<ClientHandler>> registeredClients = new HashMap<String, List<ClientHandler>>();
	private static Map<String, String> registeredDevices = new HashMap<String, String>();
	
	public static void registerDevice(String email, String GCMKey) {
		registeredDevices.put(email, GCMKey);
		
		System.out.println("Registered " + email + " with GCMKey " + GCMKey);
	}
	
	public static void registerClient(String email, ClientHandler ch) {
		if (!registeredClients.containsKey(email))
			registeredClients.put(email, new ArrayList<ClientHandler>());
		
		registeredClients.get(email).add(ch);
		
		System.out.println("Registered a new client for " + email);
	}
	
	public static void pushMessageToDevice(String email, JSONObject message) {
		System.out.println("Supposed to deliver a message to the gateway.");
		
		String GCMKey = registeredDevices.get(email);
		Sender sender = new Sender(CONSTANTS.API_KEY);
        
        Message messageObject = new Builder()
        		.addData("action", "send-sms")
                .addData("json", message.toString())
                .build();
        
        try {
        	Result result = sender.send(messageObject, GCMKey, 5);
        	System.out.println("GCM messaging status: " + result.getErrorCodeName());
        } catch (Exception ex) {
            System.out.println("Unable to deliver the message via GCM: " + ex.toString());
        }
	}
	
	public static void pushMessageToClients(String email, JSONObject message) {
		System.out.println("Supposed to deliver a message to the clients.");
		
		if (email != null && !email.isEmpty() && registeredClients.containsKey(email)) {	
			for (ClientHandler ch : registeredClients.get(email)) {
				ch.pushMessage(message);
			}
		}
	}
}
