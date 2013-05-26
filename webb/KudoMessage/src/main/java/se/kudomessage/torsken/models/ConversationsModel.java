package se.kudomessage.torsken.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import se.kudomessage.torsken.Globals;
import se.kudomessage.torsken.KudoMessage;

@SessionScoped
public class ConversationsModel implements Serializable {
    @Inject
    private Globals globals;
    
    private Map<String, Conversation> conversations = new HashMap<String, Conversation>();
    private LinkedList<String> conversationNames = new LinkedList<String>();
    
    public ConversationsModel() {
    }
    
    private boolean isSelf(String origin) {
        return origin.equals(globals.getEmail());
    }
    
    public void addMessage(KudoMessage message) {
        String conversationName;
        
        if (isSelf(message.getOrigin()))
            conversationName = message.getFirstReceiver();
        else
            conversationName = message.getOrigin();
        
        if (conversationNames.contains(conversationName)) {
            conversationNames.remove(conversationName);
        }
        
        conversationNames.addFirst(conversationName);
        
        if (!conversations.containsKey(conversationName)) {
            conversations.put(conversationName, new Conversation());
        }
        
        conversations.get(conversationName).addMessage(message);
    }

    public Map<String, Conversation> getConversations() {
        return conversations;
    }

    public LinkedList<String> getConversationNames() {
        return conversationNames;
    }
    
    public Conversation getNewConversation() {
        return new Conversation();
    }
    
    public class Conversation {
        private List<KudoMessage> messages= new ArrayList<KudoMessage>(); 
        private int numUnreadMessages = 0;
        
        public void addMessage(KudoMessage message) {
            messages.add(message);
            numUnreadMessages++;
        }
        
        public List<KudoMessage> getMessages() {
            numUnreadMessages = 0;
            return messages;
        }
        
        public int getNumUnreadMessages() {
            return numUnreadMessages;
        }
    }
}