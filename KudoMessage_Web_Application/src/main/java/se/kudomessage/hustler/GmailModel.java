/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.hustler;

import com.sun.mail.imap.IMAPSSLStore;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import se.kudomessage.hustler.oauth.OAuth2SaslClientFactory;

public class GmailModel {
    private IMAPSSLStore store;
    private Session session;
    private Folder rootFolder, standardFolder, pendingFolder, errorFolder;
    
    public GmailModel(String token, String email) {
        System.out.println("##### Skapade GmailModel");
        Security.addProvider(new OAuth2Provider());

        try {
            setupStore(token, email);
            createAllFolders();
        } catch (MessagingException ex) {
            // Something went wrong.
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

    private void setupStore(String token, String email) throws MessagingException {
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
    }

    private void getMessagesNewerThan(final KudoMessage m) {
        SearchTerm term = new SearchTerm() {
            public boolean match(Message message) {
                if (message.isExpunged()) {
                    return false;
                }

                if (getMessageId(message).equals(m.id)) {
                    return true;
                }
                return false;
            }
        };

        try {
            Message[] messages = rootFolder.search(term);

            UIDFolder a = (UIDFolder) rootFolder;
            long firstUID = a.getUID(messages[0]);

            Message[] newMessages = a.getMessagesByUID(firstUID + 1, UIDFolder.LASTUID);

            // TODO: Check if this works.
        } catch (MessagingException ex) {
        }
    }

    private String getMessageId(Message m) {
        if (m.isExpunged()) {
            return "";
        }

        MimeMessage message = (MimeMessage) m;

        try {
            return message.getMessageID();
        } catch (MessagingException e) {
            return "";
        }
    }

    public void addReceivedMessage(KudoMessage m) {
        // TODO Auto-generated method stub
    }

    public void addSentMessage(KudoMessage m) {
        // TODO Auto-generated method stub
    }

    public KudoMessage getMessage(final String id) {
        SearchTerm term = new SearchTerm() {
            public boolean match(Message message) {
                if (message.isExpunged()) {
                    return false;
                }

                if (getMessageId(message).equals(id)) {
                    return true;
                }
                return false;
            }
        };

        Message message = null;
        try {
            Message[] messages = rootFolder.search(term);
            message = messages[0];
        } catch (MessagingException ex) {
            return null;
        }

        try {
            return new KudoMessage(
                    getMessageId(message),
                    message.getContent().toString(),
                    message.getFrom()[0].toString(),
                    message.getAllRecipients()[0].toString());
        } catch (MessagingException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public String saveMessageToPending(KudoMessage m) {
        try {
            if (!pendingFolder.isOpen()) {
                pendingFolder.open(Folder.READ_WRITE);
            }

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(m.origin));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(m.receiver));
            message.setSubject("SMS med XXX");
            message.setText(m.content);

            message.setFlag(Flag.DRAFT, true);

            MimeMessage draftMessages[] = {message};
            pendingFolder.appendMessages(draftMessages);

            Message latestMessage = pendingFolder.getMessage(pendingFolder.getMessageCount());

            return getMessageId(latestMessage);
        } catch (MessagingException e) {
            return "";
        }
    }

    public List<KudoMessage> findMessage(final String query) {
        ArrayList<KudoMessage> result = new ArrayList<KudoMessage>();

        SearchTerm term = new SearchTerm() {
            public boolean match(Message message) {
                if (message.isExpunged()) {
                    return false;
                }

                try {
                    if (message.getContent().toString().toLowerCase().contains(query.toLowerCase())) {
                        return true;
                    }
                } catch (IOException e) {
                } catch (MessagingException e) {
                }

                return false;
            }
        };

        Message[] messages = null;

        try {
            messages = rootFolder.search(term);
        } catch (MessagingException e) {
        }

        for (Message message : messages) {
            try {
                KudoMessage tmp = new KudoMessage(
                        getMessageId(message),
                        message.getContent().toString(),
                        message.getFrom()[0].toString(),
                        message.getAllRecipients()[0].toString());

                result.add(tmp);
            } catch (IOException e) {
            } catch (MessagingException e) {
            }
        }

        return result;
    }

    public void moveMessage(KudoMessage m, Label target) {
        // TODO Auto-generated method stub
    }
    
    public List<KudoMessage> getMessages(int lower, int upper) throws MessagingException {
        List<KudoMessage> messages =  new ArrayList<KudoMessage>();
        
        int totalNumMessages = standardFolder.getMessageCount();
        upper = upper > totalNumMessages ? totalNumMessages : upper;
        
        if (totalNumMessages < lower || upper < lower)
            return messages;
        
        for (int i = lower; i < upper + 1; i++) {
            Message message = standardFolder.getMessage(i);
            
            try {
                KudoMessage tmp = new KudoMessage(
                            getMessageId(message),
                            message.getContent().toString(),
                            message.getFrom()[0].toString(),
                            message.getAllRecipients()[0].toString());
                
                messages.add(tmp);
            } catch (IOException ex) {
            }
        }
        
        return messages;
    }

    public enum Label {
        PENDING,
        SENT,
        ERROR
    }
}
