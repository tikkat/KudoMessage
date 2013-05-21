package se.kudomessage.hustler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleContactsController {
	public static JSONArray getContacts(String email, String token) {
		JSONArray contacts = new JSONArray();
		
		// TODO: May need a limit to get all contacts(?)
		String url = "https://www.google.com/m8/feeds/contacts/" + email + "/base?alt=json&oauth_token=" + token;
		String content = Utils.getContentOfURL(url);
		
		JSONArray c;
		try {
			JSONObject a = new JSONObject(content);
			JSONObject b = a.getJSONObject("feed");
			c = b.getJSONArray("entry");
		} catch (Exception e) {
			System.out.println("ERROR IN GET-CONTACTS: " + e);
			return contacts;
		}
		
		for (int i = 0; i < c.length(); i++) {
			try {
				JSONObject entry = c.getJSONObject(i);
				
				if (entry.has("gd$phoneNumber")) {
					String name = entry.getJSONObject("title").getString("$t");
					String number = entry.getJSONArray("gd$phoneNumber").getJSONObject(0).getString("$t");
					
					JSONObject contact = new JSONObject();
					contact.put("name", name);
					contact.put("number", number);
					
					contacts.put(contact);
				}
			} catch (JSONException e) {}
		}
		
		return contacts;
	}

	public static void addContact(String email, String token, String name, String number) {
		String postURL = "https://www.google.com/m8/feeds/contacts/" + email + "/full?oauth_token=" + token;
		
		String rawBody = 
		"<atom:entry xmlns:atom='http://www.w3.org/2005/Atom' xmlns:gd='http://schemas.google.com/g/2005'>" +
			"<atom:category scheme='http://schemas.google.com/g/2005#kind' term='http://schemas.google.com/contact/2008#contact' />" +
			"<atom:title type='text'>" +
				name +
			"</atom:title>" +
			"<gd:phoneNumber rel='http://schemas.google.com/g/2005#mobile'>" +
				number +
			"</gd:phoneNumber>" +
		"</atom:entry>";
		
		try {
			HttpClient client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(postURL);
	        
	        StringEntity input = new StringEntity(rawBody);
	        input.setContentType("application/atom+xml");
	        post.setEntity(input);
	        
	        client.execute(post);
		} catch (Exception ex) {
			System.out.println("Something wrong in addContact: " + ex);
		}
	}
}