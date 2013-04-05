/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage;

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

/**
 *
 * @author Philip
 */
@ManagedBean
@SessionScoped
public class Utils {
    
    private static final String CALLBACK_URL = "http://localhost:8080/KudoMessage_-_Torsken/";
    private static final HttpTransport TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    // FILL THESE IN WITH YOUR VALUES
    private static final String CLIENT_ID = "509205524891-milcd02622075pccioirpfo0ba8qfeg4.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "n6H0Krn3BgCn8_KmDilsyPAR";
    
    public String getTokenFromCode(String code) throws IOException {
        if (code.isEmpty())
            return "Ingen code.";
            
        GoogleAuthorizationCodeGrant authRequest = new GoogleAuthorizationCodeGrant(TRANSPORT,
                        JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, code, CALLBACK_URL);
        authRequest.useBasicAuthorization = false;
        AccessTokenResponse authResponse = authRequest.execute();
        String accessToken = authResponse.accessToken;
        GoogleAccessProtectedResource access = new GoogleAccessProtectedResource(accessToken,
                        TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, authResponse.refreshToken);
        TRANSPORT.createRequestFactory(access);
        
        return authResponse.accessToken;
        
        //System.out.println("Access token: " + authResponse.accessToken);
    }
}
