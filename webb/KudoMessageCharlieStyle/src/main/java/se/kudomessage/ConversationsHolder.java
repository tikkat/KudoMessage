package se.kudomessage;

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
    private static List<Conversation.Message> currentConversation = new ArrayList<Conversation.Message>();
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

    public void setCurrentConversation(List<Conversation.Message> currentConversation) {
        ConversationsHolder.currentConversation = currentConversation;
    }
    
    public void addViewToPush() {
        PushRenderer.addCurrentSession(Globals.email);
    }

    public List<Conversation.Message> getCurrentConversation() {
        return currentConversation;
    }
    
    public void addMessage(String content, String origin, String receiver) {
        String conversationName = getConversationName(origin, receiver);
        
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

        System.out.println("Adding message " + content + " to " + conversationName);
        conversations.get(conversationName).addMessage(content, origin, receiver);
        
        updateCurrentConversation();
    }
    
    public void updateCurrentConversation() {
        if (!conversations.containsKey(currentConversationName))
            currentConversation = new ArrayList<Conversation.Message>();
        
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
        private List<Message> messages;
        
        public Conversation(String name) {
            this.name = name;
            
            messages = new ArrayList<Message>();
        }
        
        public void addMessage(String content, String origin, String receiver) {
            messages.add(new Message(content, origin, receiver));
        }
        
        public List<Message> getMessages() {
            return messages;
        }
                
        public class Message {
            private String content;
            private String origin;
            private String receiver;
            
            public Message(String content, String origin, String receiver) {
                this.content = content;
                this.origin = origin;
                this.receiver = receiver;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }
        }
    }
}