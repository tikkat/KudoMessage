/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.hustler;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class APIBean {
    private String connectBean = "null";
    private Map<String,String> params;
    
    @ManagedProperty("#{param.action}")
    private String action;
    
    @ManagedProperty("#{param.access_token}")
    private String access_token;
    
    @ManagedProperty("#{param.gcm_key}")
    private String gcm_key;

    public APIBean() {
                
    }
    
    @PostConstruct
    public void init () {
        if (action.equals("reg_android_device")) {
            registerAndroidDevice();
        } 
    }

    private void registerAndroidDevice() {
        String userID = Utils.getUserInfo(access_token).get("userID");
        PushHandler.registerAndroidDevice(userID, gcm_key);
        
        connectBean = "Registered " + userID + " with GCM " + gcm_key + ", ACCESS TOKEN: " + access_token;
    }

    public String getConnectBean() {
        return connectBean;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setGcm_key(String gcm_key) {
        this.gcm_key = gcm_key;
    }   
}
