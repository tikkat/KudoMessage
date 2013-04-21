package se.kudomessage.torsken;

import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.icefaces.application.PushRenderer;

@ManagedBean
@SessionScoped
public class MessageHandler {
    
    private String PUSH_GROUP;
    private String message, number;
    private ClientUser client;
    private ArrayList<ConversationList> list = new ArrayList<ConversationList>();
    private KudoMessage tmpMess;
    private ConversationList tmpList;
    
    public MessageHandler () {
        client = ClientUser.getInstance();
        this.PUSH_GROUP = client.getID();
        PushRenderer.addCurrentSession(PUSH_GROUP);
    }
    
    public void sendMessage () {
        if (!message.isEmpty() && !number.isEmpty()) {
            tmpMess = new KudoMessage(message, number);
        
            if (conversationExists(number) >= 0) {
                list.get(conversationExists(number)).addToList(tmpMess);
            } else {
                tmpList = new ConversationList();
                tmpList.setConversationNumber(number);
                tmpList.addToList(tmpMess);
                list.add(tmpList);
            }

            PushRenderer.render(PUSH_GROUP);
            message = "";
            number = "";
        }
    }
    
    public int conversationExists ( String num ) {
        for ( int i = 0; i < list.size(); i++) {
            if (list.get(i).getConversationNumber().equals(num)) {
                return i;
            }
        }
        return -1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<ConversationList> getList() {
        return list;
    }

    public void setList(ArrayList<ConversationList> list) {
        this.list = list;
    }
    
}
