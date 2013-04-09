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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class will be the controller class for the JSF page home.xhtml
 * This is were the access token, code token, message and number will be saved
 * for later use (send via socket to Hustler).
 * @author Philip
 */
@ManagedBean
@SessionScoped
public class HomeController {
    
    private String accessCode;
    private String accessToken, message, number;
    
    /**
     * This constructor will fetch the 2 session parameters. After that create a Socket Connection
     * with Hustler.
     */
    public HomeController () {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);  
        accessCode = (String) httpSession.getAttribute("ACCESS_CODE");
        accessToken = (String) httpSession.getAttribute("ACCESS_TOKEN");
    }
    
    @PostConstruct
    public void init () {
        SocketHandler.openSocketCommunication();
        SocketHandler.sendInitializeViaSocket(accessToken);
    }
    
    /**
     * This method corresponds to the Send Message button.
     */
    public void sendMessageButton () {
        Message messObject = new Message(message, number);
        ConversationHandler.sendMessage(messObject);
    }
    
    
    //Setters & Getters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    } 
}
