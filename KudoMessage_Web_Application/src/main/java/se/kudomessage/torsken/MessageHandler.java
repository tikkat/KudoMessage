package se.kudomessage.torsken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.json.JSONException;
import org.json.JSONObject;

@ManagedBean
@SessionScoped
public class MessageHandler {
    private String content, receiver;
    private Map<String, ArrayList> messages;
    private String conversationName;
    
    public MessageHandler () {
        messages = new HashMap<String, ArrayList>();
    }
    
    public void sendMessage () {
        JSONObject message = new JSONObject();
        try {
            message.put("token", ClientUser.getInstance().getAccessToken());
            message.put("receiver", receiver);
            message.put("content", content);
        } catch (JSONException ex) {
            return;
        }
        
        addMessageToConversation(new KudoMessage(content, ClientUser.getInstance().getEmail(), receiver));
        RESTHandler.post("send-message", message.toString());
    }
    
    private void addMessageToConversation(KudoMessage m) {
        conversationName = m.origin.equals(ClientUser.getInstance().getEmail()) ? m.receiver : m.origin;
        
        if (!messages.containsKey(conversationName))
            messages.put(conversationName, new ArrayList<KudoMessage>());
        
        messages.get(conversationName).add(m);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
