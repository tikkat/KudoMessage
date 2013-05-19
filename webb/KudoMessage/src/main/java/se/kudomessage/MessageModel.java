package se.kudomessage;

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
                    
                    JSONObject message = new JSONObject();
                    message.put("receiver", ConversationsHolder.getCurrentConversationName());
                    message.put("content", messageContent);
                    
                    output.put("message", message);

                    SocketHandler.getOut().println(output.toString());
                    SocketHandler.getOut().flush();
                } catch (Exception e) {}
                
                messageContent = "";
            }
        }).start();
    }
}
