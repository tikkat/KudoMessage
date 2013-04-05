/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

/**
 * This class will be the controller class for the JSF page home.xhtml
 * This is were the access token, code token, message and number will be saved
 * for later use (send via socket to Hustler).
 * @author Philip
 */
@ManagedBean
@SessionScoped
@RequestScoped
public class HomeController {
    
    //This line of code will save the GET parameter "?code" to the variable accessCode in HomeController.
    //The Home page will be loaded every time you login with OAuth, google will send the accessCode
    //in the URL
    @ManagedProperty("#{param.code}")
    private String accessCode;
    
    //The accessToken will be received by sending the accessCode to the Utils class.
    private String accessToken, message, number;
    
    
    
    public boolean generateAccessToken () {
        
        //DONT FORGET!
        //Check if token is allready generated and then check if its valid
        //if it has expired request a refresh token
        //access token will expire in 3600sec (1hr)
        //RETHINK, do we need a boolean here?
        try {
            if (!accessCode.isEmpty()) {
                accessToken = Utils.getTokenFromCode(accessCode);
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
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
