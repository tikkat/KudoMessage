/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Philip
 */
@ManagedBean
@SessionScoped
public class OAuthController {
    
    //This line of code will save the GET parameter "?code" to the variable accessCode in HomeController.
    //The Home page will be loaded every time you login with OAuth, google will send the accessCode
    //in the URL
    @ManagedProperty("#{param.code}")
    private String accessCode;
    private String accessToken;
    
    private static final String CALLBACK_URL = Constants.REDIRECT_URI;
    private static final HttpTransport TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String CLIENT_ID = Constants.CLIENT_ID;
    private static final String CLIENT_SECRET = Constants.CLIENT_SECRET;
    
    public OAuthController () {
        
    }
    
    @PostConstruct
    public void init () {
        try {
            accessToken = getTokenFromCode(accessCode);
            setAccessTokenAsGlobal();
        } catch (IOException ex) {
            Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static String getTokenFromCode(String code) throws IOException {
            
        if (code == null)
            return "The code is null";
        
        GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant authRequest = new GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant(TRANSPORT,
                        JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, CALLBACK_URL);
        authRequest.useBasicAuthorization = false;
        AccessTokenResponse authResponse = authRequest.execute();
        String accessToken = authResponse.accessToken;
        GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(accessToken,
                        TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, authResponse.refreshToken);
        TRANSPORT.createRequestFactory(access);
        
        return authResponse.accessToken;
    }
    
    public void redirectToGoogleOAuth () throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("https://accounts.google.com/o/oauth2/auth?redirect_uri="+ Constants.REDIRECT_URI +"&response_type=code&client_id=" + Constants.CLIENT_ID + "&approval_prompt=force&scope=https%3A%2F%2Fmail.google.com+https%3A%2F%2Fwww.google.com%2Fm8%2Ffeeds%2F+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
    }
    
    public void setAccessTokenAsGlobal () {
        Globals.setAccessToken(accessToken);
    }
    
    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    
}
