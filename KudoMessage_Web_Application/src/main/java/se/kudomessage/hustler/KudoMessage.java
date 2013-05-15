package se.kudomessage.hustler;

import org.json.JSONException;
import org.json.JSONObject;

public class KudoMessage {

    protected String id,
            content,
            origin,
            receiver;

    public KudoMessage(String content, String origin, String receiver) {
        this.content = content;
        this.origin = origin;
        this.receiver = receiver;
    }

    public KudoMessage(String id, String content, String origin, String receiver) {
        this.id = id;
        this.content = content;
        this.origin = origin;
        this.receiver = receiver;
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
                    json.put("receiver", this.receiver);
                    json.put("content", this.content);
            } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }

            return json;
    }
}