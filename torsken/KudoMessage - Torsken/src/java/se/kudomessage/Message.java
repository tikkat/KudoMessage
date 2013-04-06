/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Philip
 */
@ManagedBean
@SessionScoped
public class Message {
    
    private String msg, nbr;
    
    public Message (String msg, String nbr) {
        this.msg = msg;
        this.nbr = nbr;
    }

    
    //Setters & Getters
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNbr() {
        return nbr;
    }

    public void setNbr(String nbr) {
        this.nbr = nbr;
    }
}
