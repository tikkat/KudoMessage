package se.kudomessage.torsken;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SessionScoped
public class KudoMessage implements Serializable {
    protected String id, content, origin;
    protected ArrayList<String> receivers = new ArrayList<String>();

    public KudoMessage() {
        //Empty message
    }

    public KudoMessage(String content, String origin, String receiver, String id) {
        this(content, origin, receiver);
        this.id = id;
    }

    public KudoMessage(String content, String origin, String receiver) {
        this.content = content;
        this.origin = origin;
        this.receivers.add(receiver);
    }

    public KudoMessage(String id) {
        this.id = id;
    }

    public String getFirstReceiver() {
        return receivers.get(0);
    }

    public void addReceiver(String receiver) {
        this.receivers.add(receiver);
    }

    public String getOrigin() {
        return origin;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    @Override
    public String toString() {
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
            for (String r : receivers) {
                rl.put(r);
            }

            json.put("receivers", rl);

        } catch (JSONException e) {}

        return json;
    }
}