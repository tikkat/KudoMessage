package se.kudomessage.torsken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.icefaces.application.PushRenderer;

@SessionScoped
@ManagedBean
public class ConversationsHolder {
    private static Map<String, Conversation> conversations = new HashMap<String, Conversation>();;
    private static List<KudoMessage> currentConversation = new ArrayList<KudoMessage>();
    private static LinkedList<String> allConversationsNames = new LinkedList<String>();
            
    private static String currentConversationName = "";
    
    private static ConversationsHolder instance = null;
    
    public static ConversationsHolder getInstance() {
        if (instance == null) {
            instance = new ConversationsHolder();
        }
        
        return instance;
    }
    
    public ConversationsHolder() {
    }

    public static String getCurrentConversationName() {
        return currentConversationName;
    }
    
    public void setActiveConversation(String name) {
        currentConversationName = name;
        updateCurrentConversation();
    }
    
    public boolean isSelf(String origin) {
        return origin.equals(Globals.email);
    }
    
    public String getNameOfContact(String number) {
        if (Globals.contacts.containsKey(number)) {
            return Globals.contacts.get(number);
        } else {
            return "No match: " + number;
        }
    }

    public void setCurrentConversation(List<KudoMessage> currentConversation) {
        ConversationsHolder.currentConversation = currentConversation;
    }
    
    public void addViewToPush() {
        PushRenderer.addCurrentSession(Globals.email);
    }

    public List<KudoMessage> getCurrentConversation() {
        return currentConversation;
    }
    
    public void addMessage(KudoMessage message) {
        String conversationName = getConversationName(message.origin, message.getFirstReceiver());
        
        if (allConversationsNames.size() < 1)
            currentConversationName = conversationName;
        
        // If the conversation exist
        if (allConversationsNames.contains(conversationName)) {
            // Remove it
            allConversationsNames.remove(conversationName);
        }
        
        allConversationsNames.addFirst(conversationName);
        
        // Add the message to the conversation
        if (!conversations.containsKey(conversationName)) {
            conversations.put(conversationName, new Conversation(conversationName));
        }

        System.out.println("Adding message " + message.content + " to " + conversationName);
        conversations.get(conversationName).addMessage(message);
        
        updateCurrentConversation();
    }
    
    public void updateCurrentConversation() {
        if (!conversations.containsKey(currentConversationName))
            currentConversation = new ArrayList<KudoMessage>();
        
        currentConversation = conversations.get(currentConversationName).getMessages();
        
        Globals.pr.render(Globals.email);
    }

    public List<String> getAllConversationsNames() {
        return allConversationsNames;
    }
    
    private String getConversationName(String origin, String receiver) {
        return origin.equals(Globals.email) ? receiver : origin;
    }
    
    public class Conversation {
        private String name;
        private List<KudoMessage> messages;
        
        public Conversation(String name) {
            this.name = name;
            
            messages = new ArrayList<KudoMessage>();
        }
        
        public void addMessage(KudoMessage message) {
            messages.add(message);
        }
        
        public List<KudoMessage> getMessages() {
            return messages;
        }
    }
}