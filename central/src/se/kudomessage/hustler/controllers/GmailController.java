package se.kudomessage.hustler.controllers;

import com.sun.mail.imap.IMAPSSLStore;
import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.kudomessage.hustler.oauth.OAuth2SaslClientFactory;

public class GmailController {
    private IMAPSSLStore store;
    private Session session;
    private Folder rootFolder, standardFolder, pendingFolder, errorFolder;
    
    private static Map<String, GmailController> instances = new HashMap<String, GmailController>();
    
    public static GmailController getInstance(String email, String token) {
    	if (!instances.containsKey(email)) {
    		instances.put(email, new GmailController(email, token));
    	}
    	
    	return instances.get(email);
    }
    
    private GmailController(String email, String token) {
    	Security.addProvider(new OAuth2Provider());

        try {
            setupStore(email, token);
            createAllFolders();
            
            System.out.println("Created a new GmailController for " + email);
        } catch (MessagingException ex) {
            System.out.println("Error in GmailController: " + ex.toString());
        } 
    }

    public static final class OAuth2Provider extends Provider {
        public OAuth2Provider() {
            super("Google OAuth2 Provider", 1.0,
                    "Provides the XOAUTH2 SASL Mechanism");
            put("SaslClientFactory.XOAUTH2",
                    "se.kudomessage.hustler.oauth.OAuth2SaslClientFactory");
        }
    }

    private void setupStore(String email, String token) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.imaps.sasl.enable", "true");
        properties.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
        properties.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, token);

        session = Session.getInstance(properties);

        store = new IMAPSSLStore(session, null);
        store.connect("imap.gmail.com", 993, email, "");
    }

    private void createAllFolders() throws MessagingException {
        rootFolder = store.getDefaultFolder().getFolder("KudoMessage");

        if (!rootFolder.exists()) {
            rootFolder.create(Folder.HOLDS_MESSAGES);
        }

        Folder tmpFolder = rootFolder.getFolder("SMS");
        if (!tmpFolder.exists()) {
            tmpFolder.create(Folder.HOLDS_MESSAGES);
        }

        standardFolder = tmpFolder.getFolder("Standard");
        if (!standardFolder.exists()) {
            standardFolder.create(Folder.HOLDS_MESSAGES);
        }

        pendingFolder = tmpFolder.getFolder("Pending");
        if (!pendingFolder.exists()) {
            pendingFolder.create(Folder.HOLDS_MESSAGES);
        }

        errorFolder = tmpFolder.getFolder("Error");
        if (!errorFolder.exists()) {
            errorFolder.create(Folder.HOLDS_MESSAGES);
        }
        
        standardFolder.open(Folder.READ_WRITE);
    }
    
    public void saveMessage(Label label, JSONObject message) {
        Folder folder;
        
        if (label == Label.PENDING) {
            folder = pendingFolder;
        } else if (label == Label.STANDARD) {
            folder = standardFolder;
        } else {
            folder = errorFolder;
        }
        
        try {
            if (!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }
            
            try {
	            MimeMessage emailMessage = new MimeMessage(session);
	
	            emailMessage.setFrom(new InternetAddress(message.getString("origin")));
	            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(message.getString("receiver")));
	            emailMessage.setSubject("SMS med XXX");
	            emailMessage.setText(message.getString("content"));
	
	            emailMessage.setFlag(Flag.DRAFT, true);
	
	            MimeMessage draftMessages[] = {emailMessage};
	            folder.appendMessages(draftMessages);
            } catch (Exception e) {
    			System.err.println("Error in saveMessage: " + e);
    		}
        } catch (MessagingException e) {}
    }
    
    public JSONArray getMessages(int lower, int count) {
    	JSONArray messages = new JSONArray();
    	
    	int totalNumMessages;
		try {
			totalNumMessages = standardFolder.getMessageCount();
		} catch (MessagingException e) {
			return messages;
		}
    	
    	int a = totalNumMessages - lower;
    	int b = a - count < 1 ? 1 : a - count;
    	
    	if (a < 1 || b < 1 || b > a)
    		return messages;
    		
    	
        for (int i = a; i > b; i--) {
           try {
            	Message message = standardFolder.getMessage(i);
            	
                String receviver = parseEmail(message.getAllRecipients()[0].toString());   
                String origin = parseEmail(message.getFrom()[0].toString());
                
                JSONObject m = new JSONObject();
                m.put("content", message.getContent().toString());
                m.put("origin", origin);
                m.put("receiver", receviver);
                
                messages.put(m);
            } catch (Exception ex) {}
        }
        
        JSONArray tmp = new JSONArray();
        for (int i = messages.length() - 1; i >= 0; i--) {
        	try {
				tmp.put(messages.getJSONObject(i));
			} catch (JSONException e) {}
        }
        
        return tmp;
    }
    
    private String parseEmail(String email) {
        email = email.replace(":", "").replace(";", "");
        
        if (email.contains("<"))
        	email = email.substring(email.indexOf("<") + 1, email.indexOf(">"));
        
        return email;
    }

    public enum Label {
        PENDING,
        STANDARD,
        ERROR
    }
}