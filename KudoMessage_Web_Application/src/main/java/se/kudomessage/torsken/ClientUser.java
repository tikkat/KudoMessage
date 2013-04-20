package se.kudomessage.torsken;

public class ClientUser {
    private static ClientUser instance = null;
    private String accessToken;
   
    protected ClientUser () {
        
    }
    
    public static ClientUser getInstance () {
        if ( instance == null ) {
            instance = new ClientUser();
        }
        return instance;
    }
}
