/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.hustler;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.icefaces.application.PushRenderer;
import org.json.simple.JSONObject;

@ManagedBean
@ApplicationScoped
public class PushHandler {
    
    private static Map<String, String> userIDToGCM = new HashMap<String, String>();
    
    public static String getGCMKey(String userID) {
        return userIDToGCM.get(userID);
    }
    
    public static void registerAndroidServer(String token, String GCM) {
        System.out.println("Reggar en Androids server med token " + token + " och gcm " + GCM);
        
        String userID = Utils.getUserInfo(token).get("userID");
        userIDToGCM.put(userID, GCM);
    }
    
    public static void notifyAllServers(String userID, String messageID) {
        System.out.println("Try to notify userID " + userID);
        
        if (userIDToGCM.containsKey(userID)) {
            // The user has a registered Android device server, lets notify it!
            notifyAndroidServer(userID, messageID);
        }
    }
    
    public static void notifyAndroidServer(String userID, String messageID) {
        String GCMKey = getGCMKey(userID);
        System.out.println("DEN JÄVLA GCMNYCKELN ÄR FÖR FAN " + GCMKey + "!!!!!!");
        
        Message messageObject = new Message.Builder().build();
                                //addData("action", "send_sms").
                                //addData("message_id", messageID).
                                //build();
        
        try {
            Sender sender = new Sender(Constants.APIKEY);
            Result result = sender.send(messageObject, GCMKey, 5);
        } catch (IOException e) {
        }
    }
    
    public static void notifyAllClients(String userID) {
        PushRenderer.render(userID);
    }
}
