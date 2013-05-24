package se.kudomessage.torsken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import org.icefaces.application.PortableRenderer;

@ApplicationScoped
public class TmpMessages implements Serializable {
    private PortableRenderer pr;
    
    // TODO: Make the list a map, where the key is the users email or session id
    private List<KudoMessage> tmp_messages = new ArrayList<KudoMessage>();
    
    public void setPr(PortableRenderer pr) {
        this.pr = pr;
    }
    
    public void addMessage(KudoMessage message, String pushGroup) {
        tmp_messages.add(message);
        pr.render(pushGroup);
    }
    
    public KudoMessage getMessage() {
        if (tmp_messages.size() > 0) {
            KudoMessage m = tmp_messages.get(0);
            tmp_messages.remove(0);
            return m;
        } else {
            return null;
        }
    }
}
