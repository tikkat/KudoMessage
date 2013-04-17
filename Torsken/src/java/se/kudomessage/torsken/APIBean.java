/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class APIBean {
    
    private String connectBean = "null";
    private Map<String,String> params;

    public APIBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        params = fc.getExternalContext().getRequestParameterMap();
        
        if (params.get("action").equals("reg_android_device")) {
            registerAndroidDevice();
        } 
                
    }

    private void registerAndroidDevice() {
        String userID = Utils.getUserInfo(params.get("access_token")).get("userID");
        PushHandler.registerAndroidDevice(userID, params.get("gcm_key"));
        
    }

    public String getConnectBean() {
        return connectBean;
    }
}
