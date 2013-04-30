package se.kudomessage.torsken;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.SessionScoped;
import org.json.JSONException;
import org.json.JSONObject;

@SessionScoped
public class ClientUser {
    private static ClientUser instance;
    private String accessToken = "";
    private String email = "";
    
    protected ClientUser() {
    }
    
    public static ClientUser getInstance() {
        if (instance == null)
            instance = new ClientUser();
          
        return instance;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
