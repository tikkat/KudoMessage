/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Philip
 */
@ManagedBean
@SessionScoped
public class OAuthController {
    
    public OAuthController () {
        
    }
    
    public void redirectToGoogleOAuth () {
        FacesContext.getCurrentInstance().getExternalContext().redirect("https://accounts.google.com/o/oauth2/auth?redirect_uri="+ Globals.REDIRECT_URI +"&response_type=code&client_id=" + Globals.CLIENT_ID + "&approval_prompt=force&scope=https%3A%2F%2Fmail.google.com+https%3A%2F%2Fwww.google.com%2Fm8%2Ffeeds%2F+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
    }
    
}
