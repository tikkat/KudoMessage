package se.kudomessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public static void notifyAndroidDeviceNewMessage(String userID) {
		// GET GCM-ID AND NOTIFY ANDROID DEVICE
	}
	
	public static void tellClientNewMessage(String userID, String message, String sender) {
		// GET MESSAGE TO ClIENTS WITH THE USER_ID
	}
}
