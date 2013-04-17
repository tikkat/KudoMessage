/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Philip
 */
public class Utils {
    public static Map<String, String> getUserInfo (String accessToken) {
        String url = "http://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken;
        String result = getContentOfURL(url);
        
        Map<String, String> m = new HashMap<String, String>();
        
        JSONObject info = null;
        try {
            info = (JSONObject) new JSONParser().parse(result);
            
            m.put("email", (String)info.get("email"));
            m.put("userID", (String)info.get("email"));
        } catch (ParseException ex) {
        }
        
        return m;
    }

    private static String getContentOfURL(String url) {
        try {
            InputStream is = new URL(url).openStream();
            Scanner scanner = new Scanner(is, "UTF-8");
            
            String result = scanner.useDelimiter("\\A").next();
            
            scanner.close();
            is.close();
            
            return result;
        } catch (IOException e) {
        }
        
        return "";
    }
}
