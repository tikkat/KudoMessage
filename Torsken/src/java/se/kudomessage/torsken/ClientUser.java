/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.SessionScoped;

@SessionScoped
public class ClientUser {
    private static String accessToken;
    private Map<String, ArrayList<KudoMessage>> conversationMap;
   
    /**
     * Initializes the map which holds all of the conversations.
     */
    public ClientUser () {
        conversationMap = new HashMap<String, ArrayList<KudoMessage>>();
    }
    
    public void addMessageToConversation (KudoMessage m, String conversationNumber) {
        if (conversationExists(conversationNumber)) {
            conversationMap.get(conversationNumber).add(m);
        } else {
            ArrayList<KudoMessage> tmp = new ArrayList<KudoMessage>();
            tmp.add(m);
            conversationMap.put(conversationNumber, tmp);
        }
    }
    
    /**
     * This method will return true if the conversation exists in the map i.e
     * if the number is a key in the map. Otherwise it will return false.
     * It loops through all the keys in the map.
     * @param conversationNumber The number of the conversation
     * @return true if conversationNumber exists otherwise false
     */
    public boolean conversationExists (String conversationNumber) {
        for (String key: conversationMap.keySet()) {
            if ( key.equals(conversationNumber) ) {
                return true;
            }
        }
        return false;
    }

    public static void setAccessToken(String token) {
        accessToken = token;
    }
}
