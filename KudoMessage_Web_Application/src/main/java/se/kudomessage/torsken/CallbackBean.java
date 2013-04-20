package se.kudomessage.torsken;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class CallbackBean {
    
    private String accessCode;
    private String accessToken;
    private Map<String,String> params;
    
    
    @PostConstruct
    public void init () {
        params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        accessCode = params.get("code");
        //GET TOKEN FROM CODE HERE
        setAccessTokenAsGlobal();
        redirectToHomePage();
    }
    
    public void redirectToHomePage (){
        
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/KudoMessage_Web_Application/faces/home/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void setAccessTokenAsGlobal () {
        //TODO
    }

    public String getAccessCode() {
        return accessCode;
    }
    
}
