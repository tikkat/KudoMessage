package se.kudomessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static String getEmailByToken(String accessToken) {
        try {
            String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken;
            String result = getContentOfURL(url);
            
            JSONObject info = new JSONObject(result);
            
            return info.getString("email");
        } catch (JSONException ex) {
            return "";
        }
    }

    public static String getContentOfURL(String url) {
        try {
            InputStream is = new URL(url).openStream();
            Scanner scanner = new Scanner(is, "UTF-8");
            
            String result = scanner.useDelimiter("\\A").next();
            
            scanner.close();
            is.close();
            
            return result;
        } catch (IOException e) {}
        
        return "";
    }
}