/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Philip
 */
@ManagedBean
@SessionScoped
public class Utils {
    
    //This line of code will save the GET parameter "?code" to the variable accessCode in HomeController.
    //The Home page will be loaded every time you login with OAuth, google will send the accessCode
    //in the URL
    @ManagedProperty("#{param.code}")
    private String accessCode;
    private String accessToken;
    
    private static final String CALLBACK_URL = Globals.REDIRECT_URI;
    private static final HttpTransport TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String CLIENT_ID = Globals.CLIENT_ID;
    private static final String CLIENT_SECRET = Globals.CLIENT_SECRET;
    
    public Utils () {
        
    }
    
    @PostConstruct
    public void init () {
        try {
            accessToken = getTokenFromCode(accessCode);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static String getTokenFromCode(String code) throws IOException {
            
        if (code == null)
            return "The code is null";
        
        GoogleAuthorizationCodeGrant authRequest = new GoogleAuthorizationCodeGrant(TRANSPORT,
                        JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, CALLBACK_URL);
        authRequest.useBasicAuthorization = false;
        AccessTokenResponse authResponse = authRequest.execute();
        String accessToken = authResponse.accessToken;
        GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(accessToken,
                        TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, authResponse.refreshToken);
        TRANSPORT.createRequestFactory(access);
        
        return authResponse.accessToken;
    }
    
    public static void setAccessTokenAsGlobal () {
        
    }

    
    
    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
