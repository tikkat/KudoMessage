/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage;

import org.json.JSONObject;

/**
 *
 * @author Philip
 */
public class ConversationHandler {
    
    /**
     * This method will via the SockeHandler send messages as JSON objects.
     * @param accessToken The access token from Google.
     * @param message The message that will be sent.
     * @param receiver The receiving number.
     */
    public static void sendMessage (Message message) {
        JSONObject msg = new JSONObject();
        msg.put("action", "sendMessage");
        msg.put("message", message.getMsg());
        msg.put("receiver", message.getNbr());
        
        SocketHandler.sendObjectViaSocket(msg);
    }
    
}
