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
        JSONObject json = new JSONObject();
        try {
            json.put("token", ClientUser.getInstance().getAccessToken());
        } catch (JSONException ex) {
            return;
        }
        
        String result = RESTHandler.post("get-email", json.toString());
        try {
            email = new JSONObject(result).getString("email");
        } catch (JSONException ex) {
            Logger.getLogger(ClientUser.class.getName()).log(Level.SEVERE, null, ex);
        }
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
