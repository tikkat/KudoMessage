package se.kudomessage.torsken;

import javax.faces.bean.ManagedBean;
import org.json.JSONObject;

@ManagedBean
public class MessageModel {
    public static String messageContent = "";

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageModel.messageContent = messageContent;
    }
    
    public void sendMessage() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject output = new JSONObject();
                    output.put("action", "send-message");
                    
                    KudoMessage message = new KudoMessage(messageContent, "", ConversationsHolder.getCurrentConversationName());
                    output.put("message", message.toJSON());

                    SocketHandler.getOut().println(output.toString());
                    SocketHandler.getOut().flush();
                } catch (Exception e) {}
                
                messageContent = "";
            }
        }).start();
    }
}