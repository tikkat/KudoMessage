/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Philip
 */
@ManagedBean
@SessionScoped
public class Conversation {
    
    private String phoneNumber;
    private List<Message> messageList;
    
    public Conversation () {
        
    }
    
    public Conversation (String phoneNumber, Message msg) {
        this.phoneNumber = phoneNumber;
        
        messageList = new ArrayList<Message>();
        messageList.add(msg);
    }
    
    public void addMessageToMessageList (Message msg) {
        messageList.add(msg);
    }

    
    
    //Setters & Getters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
    
}
