package se.kudomessage.torsken.controllers;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import se.kudomessage.torsken.AsyncBean;
import se.kudomessage.torsken.views.BackingBean;
import se.kudomessage.torsken.models.ConversationsModel;
import se.kudomessage.torsken.Globals;
import se.kudomessage.torsken.KudoMessage;
import se.kudomessage.torsken.MessageUtilities;
import se.kudomessage.torsken.TmpMessages;

@RequestScoped
@Named
public class ConversationsController {
    @Inject
    private Globals globals;
    
    @Inject
    private ConversationsModel model;
    
    @Inject
    private TmpMessages tmpMessages;
    
    @Inject
    private AsyncBean asyncBean;
    
    @Inject
    private BackingBean view;
    
    public ConversationsController() {
    }
    
    public void loadFirst() {
        for (ConversationsModel.Conversation c : model.getConversations().values()) {
            c.getMessages();
        }
        
        setCurrentConversation(model.getConversationNames().getFirst());
    }
    
    public String checkNewMessages() {
        KudoMessage m = tmpMessages.getMessage();
        
        if (m != null) {
            addMessage(m);
            setCurrentConversation(view.getCurrentConversationName());
        }
        
        return "";
    }
    
    public boolean isNewConversation() {
        return view.getCurrentConversationName().equals(view.getNewConversationTag());
    }
    
    public void createNewConversation() {
        model.getConversationNames().addFirst(view.getNewConversationTag());
        model.getConversations().put(view.getNewConversationTag(), model.getNewConversation());
        setCurrentConversation(view.getNewConversationTag());
    }
    
    private void finishNewConversation() {
        if (model.getConversationNames().contains(view.getNewConversationTag())) {
            model.getConversationNames().remove(view.getNewConversationTag());
            model.getConversations().remove(view.getNewConversationTag());

            model.getConversationNames().addFirst(view.getCurrentConversationName());
            model.getConversations().put(view.getCurrentConversationName(), model.getNewConversation());
        }
    }
    
    public void deleteConversation(String name) {
        model.getConversationNames().remove(name);
        model.getConversations().remove(name);
        
        setCurrentConversation(model.getConversationNames().getFirst());
    }
    
    public void sendMessage() {
        finishNewConversation();
        
        String messageContent = view.getMessageContent();
        String messageReceiver = view.getCurrentConversationName();
        
        if (messageContent.equals("/random")) {
            messageContent = MessageUtilities.generateRandomMessage();
        }
        
        KudoMessage message = new KudoMessage(messageContent, "", messageReceiver);
        asyncBean.sendMessage(globals.getOut(), message);
        view.setMessageContent("");
    }
    
    public boolean isSelf(String origin) {
        return origin.equals(globals.getEmail());
    }
    
    public String getNumUnreadMessages(String number) {
        int num = model.getConversations().get(number).getNumUnreadMessages();
        
        if (num > 0)
            return " (" + num + ")";
        else
            return "";
    }
    
    public void setCurrentConversation(String number) {
        try {
            view.setCurrentConversationName(number);
            view.setCurrentConversation(model.getConversations().get(number).getMessages());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addMessage(KudoMessage message) {
        model.addMessage(message);
    }
}