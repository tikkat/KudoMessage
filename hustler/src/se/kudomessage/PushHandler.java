package se.kudomessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class PushHandler {
	private static Map<String, List<ClientUser>> clientUsers = new HashMap<String, List<ClientUser>>();
	private static Map<String, String> userIdToGCM = new HashMap<String, String>();
	
	public static void addClientUser(String id, ClientUser cu) {
		if (!clientUsers.containsKey(id))
			clientUsers.put(id, new ArrayList<ClientUser>());
		
		clientUsers.get(id).add(cu);
		
		System.out.println("Added a new ClientUser to the PushHandler, with id: " + id);
	}
	
	public static void registerAndroidDevice(String userID, String GCM) {
		userIdToGCM.put(userID, GCM);
	}
	
	public static String getGCMKey(String userID) {
		return userIdToGCM.get(userID);
	}
	
	public static void notifyAndroidDeviceNewMessage(String userID, String message, String receiver) {
		String GCMKey = getGCMKey(userID);
		
		Sender sender = new Sender(Constants.APIKEY);
		Message messageObject = new Message.Builder().
				addData("action", "sendSMS").
				addData("message", message).
				addData("receiver", receiver).
				build();
		try {
			Result result = sender.send(messageObject, GCMKey, 5);
			System.out.println("Sent " + messageObject + " with the result " + result);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed in notifyAndroidDeviceNewMessage()");
		}
	}
	
	public static void tellClientNewMessage(String userID, String message, String sender) {
		// GET MESSAGE TO ClIENTS WITH THE USER_ID
	}
}
