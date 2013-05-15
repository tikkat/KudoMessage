package se.kudomessage.torsken;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.icefaces.application.PushRenderer;

@ManagedBean
@SessionScoped
public class BackendBean {
    private String PUSH_GROUP = ClientUser.getInstance().getEmail();
    
    public BackendBean() {
        PushRenderer.addCurrentSession(PUSH_GROUP);
        
        System.out.println("NU LADDADES JAOG!");
    }
    
    public void test() {
        
    }
}
