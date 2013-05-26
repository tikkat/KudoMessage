package se.kudomessage.torsken.views;

import se.kudomessage.torsken.models.ConversationsModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import se.kudomessage.torsken.Globals;
import se.kudomessage.torsken.KudoMessage;

@SessionScoped
@Named
public class BackingBean implements Serializable {
    @Inject
    private Globals globals;
    
    @Inject
    private ConversationsModel model;
    
    private List<KudoMessage> currentConversation = new ArrayList<KudoMessage>();
    private String currentConversationName = "";
    
    private String messageContent;
    private String tmpContactName = "";
    private String newConversationTag = "Ny konversation";
    
    private boolean loaded = false;
    
    public boolean loadFirst() {
        if (!loaded) {
            loaded = true;
            return loaded;
        }
        return false;
    }
    
    public List<String> getConversationNames() {
        return model.getConversationNames();
    }

    public List<KudoMessage> getCurrentConversation() {
        return currentConversation;
    }

    public void setCurrentConversation(List<KudoMessage> currentConversation) {
        this.currentConversation = currentConversation;
        globals.getPr().render(globals.getEmail());
    }

    public String getCurrentConversationName() {
        return currentConversationName;
    }

    public void setCurrentConversationName(String currentConversationName) {
        this.currentConversationName = currentConversationName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getTmpContactName() {
        return tmpContactName;
    }

    public void setTmpContactName(String tmpContactName) {
        this.tmpContactName = tmpContactName;
    }

    public String getNewConversationTag() {
        return newConversationTag;
    }

    public void setNewConversationTag(String newConversationTag) {
        this.newConversationTag = newConversationTag;
    }
}