/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Philip
 */
@ManagedBean
@SessionScoped
public class RedirectController {
    
    
    public void redirectToGoogleOAuth () throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("https://accounts.google.com/o/oauth2/auth?redirect_uri=http://localhost:8080/KudoMessage_-_Torsken/&response_type=code&client_id=509205524891-milcd02622075pccioirpfo0ba8qfeg4.apps.googleusercontent.com&approval_prompt=force&scope=https%3A%2F%2Fmail.google.com+https%3A%2F%2Fwww.google.com%2Fm8%2Ffeeds%2F+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
    }
}
