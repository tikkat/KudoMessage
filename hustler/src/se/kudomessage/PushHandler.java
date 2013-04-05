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
	
	public static void addClientUser(String id, ClientUser cu) {
		if (!clientUsers.containsKey(id))
			clientUsers.put(id, new ArrayList<ClientUser>());
		
		clientUsers.get(id).add(cu);
		
		System.out.println("Added a new ClientUser to the PushHandler, with id: " + id);
	}
	
	public static void registerAndroidDevice(String userID, String GCM) {
		// REGISTER THE DEVICE
	}
	
	public static void notifyAndroidDeviceNewMessage(String userID, int messageID) {
		Sender sender = new Sender("API-KEY");
		Message message = new Message.Builder().addData("action", "SEND_SMS").addData("messageID", String.valueOf(messageID)).build();
		
		try {
			Result result = sender.send(message, "GCM-KEY", 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void tellClientNewMessage(String userID, String message, String sender) {
		// GET MESSAGE TO ClIENTS WITH THE USER_ID
	}
}
