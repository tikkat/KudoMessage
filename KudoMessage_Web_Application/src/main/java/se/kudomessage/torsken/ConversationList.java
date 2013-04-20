package se.kudomessage.torsken;

import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ConversationList {

    private String conversationNumber;
    private ArrayList<KudoMessage> messageList;
    
    public ConversationList () {
        messageList = new ArrayList<KudoMessage>();
    }
    
    public void addToList ( KudoMessage mess ) {
        messageList.add(mess);
    }

    public String getConversationNumber() {
        return conversationNumber;
    }

    public void setConversationNumber(String conversationNumber) {
        this.conversationNumber = conversationNumber;
    }

    public ArrayList<KudoMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<KudoMessage> messageList) {
        this.messageList = messageList;
    }
}
