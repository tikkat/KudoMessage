package se.kudomessage.torsken;

import java.io.Serializable;
import javax.enterprise.context.Dependent;

@Dependent
public class CONSTANTS implements Serializable {
    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 5002;
    
    public static final String CLIENT_ID = "509205524891-joksn0omcchbae38krmc1nnjmaqavc2n.apps.googleusercontent.com";
    public static final String CLIENT_SECRET = "pao6aVAL0JTkeAohEWDR2dXP";
    public static final String REDIRECT_URI = "http://localhost:8080/KudoMessage/oauthcallback/index.xhtml";
}