/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.hustler;

import java.util.HashMap;
import java.util.Map;

public class GmailModelList {
    private static Map<String, GmailModel> gms = new HashMap<String, GmailModel>();
    
    public static GmailModel getGmailModel(String token, String email) {
        if (!gms.containsKey(email))
            gms.put(email, new GmailModel(token, email));
        
        return gms.get(email);
    }
}
