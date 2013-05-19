package se.kudomessage.hustler;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleContactsController {
	public static JSONArray getContacts(String email, String token) {
		JSONArray contacts = new JSONArray();
		
		String url = "https://www.google.com/m8/feeds/contacts/" + email + "/base?alt=json&oauth_token=" + token;
		String content = Utils.getContentOfURL(url);
		
		JSONObject a = new JSONObject(content);
		JSONObject b = a.getJSONObject("feed");
		JSONArray c = b.getJSONArray("entry");
		
		for (int i = 0; i < c.length(); i++) {
			JSONObject entry = c.getJSONObject(i);
			
			String name = entry.getJSONObject("title").getString("$t");
			String number = entry.getJSONArray("gd$phoneNumber").getJSONObject(0).getString("$t");
			
			JSONObject contact = new JSONObject();
			contact.put("name", name);
			contact.put("number", number);
			
			contacts.put(contact);
		}
		
		return contacts;
	}
}