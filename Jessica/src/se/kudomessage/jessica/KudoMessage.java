package se.kudomessage.jessica;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KudoMessage {
	protected String id, 
		content, 
		origin;
	protected ArrayList<String> receivers;
	
	public KudoMessage(){
		//Empty message
	}

	public KudoMessage(String id, String content, String origin, String receiver){
		this.id = id;
		this.content = content;
		this.origin = origin;
		this.receivers.add(receiver);
	}
	
	public KudoMessage(String id, String content, String origin){
		this.id = id;
		this.content = content;
		this.origin = origin;
	}
	
	public KudoMessage(String id){
		this.id = id;
	}	
	
	public String getFirstReceiver(){
		return receivers.get(0);
	}
	
	public void addReceiver(String receiver){
		this.receivers.add(receiver);
	}
	
	
	public String toString(){
		return toJSON().toString();
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		
		try {
			json.put("protocol", "SMS");
			json.put("id", this.id);
			json.put("origin", this.origin);
			json.put("content", this.content);
			
			JSONArray rl = new JSONArray();
			for( String r : receivers){
				rl.put(r);
			}
			
			json.put("receivers", rl);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
}
