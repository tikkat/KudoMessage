package se.kudomessage.torsken.oauth;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import java.io.IOException;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import se.kudomessage.torsken.CONSTANTS;
import se.kudomessage.torsken.Globals;

@RequestScoped
@Named("oAuthController")
public class OAuthController {

    @Inject
    private Globals globals;
    
    private final HttpTransport TRANSPORT = new NetHttpTransport();
    private final JsonFactory JSON_FACTORY = new JacksonFactory();

    public void callback() {
        if (globals.getAccessToken().equals("")) {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            try {
                String accessToken = getTokenFromCode(params.get("code"));
                globals.setAccessToken(accessToken);

                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/KudoMessage/app/");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void redirectToGoogleOAuth() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("https://accounts.google.com/o/oauth2/auth?redirect_uri=" + CONSTANTS.REDIRECT_URI + "&response_type=code&client_id=" + CONSTANTS.CLIENT_ID + "&approval_prompt=force&scope=https%3A%2F%2Fmail.google.com+https%3A%2F%2Fwww.google.com%2Fm8%2Ffeeds%2F+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
    }

    public String getTokenFromCode(String code) throws IOException {
        GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant authRequest = new GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant(
                TRANSPORT, JSON_FACTORY, CONSTANTS.CLIENT_ID, CONSTANTS.CLIENT_SECRET, code, CONSTANTS.REDIRECT_URI);

        authRequest.useBasicAuthorization = false;

        try {
            AccessTokenResponse authResponse = authRequest.execute();
            return authResponse.accessToken;
        } catch (HttpResponseException e) {
            return "";
        }
    }
}