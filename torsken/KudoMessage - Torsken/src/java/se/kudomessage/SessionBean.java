/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class will handle Sessions, you will be able to set sessions attributes and set session
 * attributes.
 * @author Philip
 */
@ManagedBean
@SessionScoped
@RequestScoped
public class SessionBean {
    //This line of code will save the GET parameter "?code" to the variable accessCode in HomeController.
    //The Home page will be loaded every time you login with OAuth, google will send the accessCode
    //in the URL
    @ManagedProperty("#{param.code}")
    private String accessCode;
    private String accessToken;
    
    
    
    public SessionBean () {
        
    }
    
    /**
     * This method will be called directly when the user leaves the page i.e it will be called 
     * directly when the page is redirected in the header. It will create 2 session attributes
     * (accessCode and accessToken).
     */
    @PostConstruct
    public void init () {
        try {
            createSession();
        } catch (IOException ex) {
            Logger.getLogger(SessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createSession () throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);
        
        //GET THE ACCESS TOKEN FROM THE ACCESS CODE
        accessToken = Utils.getTokenFromCode(accessCode);
        
        httpSession.setAttribute("ACCESS_TOKEN", accessToken);
    }
    
    public String retreiveSessionAttribute (String attribute) {
        
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);  
        return (String) httpSession.getAttribute(attribute); 
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
