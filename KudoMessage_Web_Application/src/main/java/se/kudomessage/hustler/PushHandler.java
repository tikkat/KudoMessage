/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.hustler;

import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.icefaces.application.PushRenderer;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Message.Builder;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import java.io.IOException;

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
    
    public static void notifyAllServers(String userID, KudoMessage m) {
        System.out.println("Try to notify userID " + userID);
        
        if (userIDToGCM.containsKey(userID)) {
            // The user has a registered Android device server, lets notify it!
            notifyAndroidServer(userID, m);
        }
    }
    
    public static void notifyAndroidServer(String userID, KudoMessage m) {
        String GCMKey = getGCMKey(userID);
        
        Sender sender = new Sender(Constants.APIKEY);
        
        Message messageObject = new Builder()
                .addData("action", "send_sms")
                .addData("message", m.toString())
                .build();
        
        try {
            Result result = sender.send(messageObject, GCMKey, 5);
            System.out.println("Skickade GCM grejen med status " + result.getErrorCodeName() + " till " + userID +  " med gcm " + GCMKey + ".");
        } catch (IOException ex) {
            System.out.println("Något gick snett, inte rakt.");
        }
    }
    
    public static void notifyAllClients(String userID) {
        PushRenderer.render(userID);
    }
}
