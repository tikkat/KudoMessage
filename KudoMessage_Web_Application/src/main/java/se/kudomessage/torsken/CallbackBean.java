package se.kudomessage.torsken;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class CallbackBean {
    public void init () {
        if (ClientUser.getInstance().getAccessToken().equals("")) {  
            Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

            try {
                String accessToken = OAuthController.getTokenFromCode(params.get("code"));
                ClientUser.getInstance().setAccessToken(accessToken);
                System.out.println("Got access token " + accessToken);
            } catch (IOException ex) {
                Logger.getLogger(CallbackBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        redirectToHomePage();
    }
    
    public void redirectToHomePage (){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8080/KudoMessage_Web_Application/faces/home/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
