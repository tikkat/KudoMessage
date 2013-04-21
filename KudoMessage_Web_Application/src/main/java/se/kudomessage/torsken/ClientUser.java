package se.kudomessage.torsken;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@SessionScoped
public class ClientUser {
    private static ClientUser instance = null;
    private String accessToken;
    private final String ID;
   
    protected ClientUser () {
        FacesContext fcontext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fcontext.getExternalContext().getSession(false);
        ID = session.getId();
    }
    
    public static ClientUser getInstance () {
        if ( instance == null ) {
            instance = new ClientUser();
        }
        return instance;
    }
    
    public String getID () {
        return ID;
    }
}
