package se.kudomessage.torsken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ManagedBean
@SessionScoped
public class MessageHandler {

    private String content;
    private Map<String, ArrayList> messages;
    private ArrayList<KudoMessage> activeConversation;
    private String activeNumber;

    public MessageHandler() throws JSONException {
        messages = new HashMap<String, ArrayList>();
        
        JSONObject range = new JSONObject();
        range.put("token", ClientUser.getInstance().getAccessToken());
        range.put("lower", 0);
        range.put("upper", 100);

        String tmp = RESTHandler.post("get-messages", range.toString());
        
        JSONArray array = new JSONObject(tmp).getJSONArray("messages");

        ClientUser.getInstance();
        
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                addMessageToConversation(new KudoMessage(row.getString("content"), row.getString("origin"), row.getString("receiver")));
            }
        } catch (Exception e) {
        }
        Set<String> t = messages.keySet();
        List h = new ArrayList<String>(t);
        
        if (h.size() > 0){
            activeConversation = messages.get(h.get(0));
            if (activeConversation.get(0).getReceiver().equals(ClientUser.getInstance().getEmail())) {
                activeNumber = activeConversation.get(0).getOrigin();
            } else {
                activeNumber = activeConversation.get(0).getReceiver();
            }
        }
    }

    public <T, S> List<Map.Entry<T, S>> mapToList(Map<T, S> map) {
        if (map == null) {
            return null;
        }

        List<Map.Entry<T, S>> list = new ArrayList<Map.Entry<T, S>>();
        list.addAll(map.entrySet());

        return list;
    }
    
    public boolean samma ( String c ) {
        return c.equals(ClientUser.getInstance().getEmail());
    }
    
    public void changeConversation ( String conv ) {
        System.out.println(conv);
        activeNumber = conv;
        activeConversation = messages.get(conv);
    }
    
    public void sendMessage() {
        JSONObject message = new JSONObject();
        try {
            message.put("token", ClientUser.getInstance().getAccessToken());
            message.put("receiver", activeNumber);
            message.put("content", content);
        } catch (JSONException ex) {
            return;
        }

        addMessageToConversation(new KudoMessage(content, ClientUser.getInstance().getEmail(), activeNumber));
        RESTHandler.post("send-message", message.toString());
    }

    private void addMessageToConversation(KudoMessage m) {
        String test = m.origin.equals(ClientUser.getInstance().getEmail()) ? m.receiver : m.origin;
        
        System.out.println("BAAAAJS ::: " + test);
        
        if (!messages.containsKey(test)) {
            messages.put(test, new ArrayList<KudoMessage>());
        }

        messages.get(test).add(m);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, ArrayList> getMessages() {
        return messages;
    }

    public ArrayList<KudoMessage> getActiveConversation() {
        return activeConversation;
    }

    public void setMessages(Map<String, ArrayList> messages) {
        this.messages = messages;
    }

    public void setActiveConversation(ArrayList<KudoMessage> activeConversation) {
        this.activeConversation = activeConversation;
    }

    public String getActiveNumber() {
        return activeNumber;
    }
    
}