/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import com.sun.mail.imap.IMAPSSLStore;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import se.kudomessage.torsken.oauth.OAuth2SaslClientFactory;

public class GmailModel {

    private IMAPSSLStore store;
    private Folder rootFolder, standardFolder, pendingFolder, errorFolder;

    public GmailModel() {
        Security.addProvider(new OAuth2Provider());

        try {
            setupStore();
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
                    "se.kudomessage.torsken.oauth.OAuth2SaslClientFactory");
        }
    }

    private void setupStore() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.imaps.sasl.enable", "true");
        properties.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
        properties.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, Globals.getAccessToken());

        Session session = Session.getInstance(properties);

        store = new IMAPSSLStore(session, null);
        store.connect("imap.google.com", 993, Globals.getEmail(), "");
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

    public List<KudoMessage> findMessage(String query) {
        // TODO Auto-generated method stub

        return null;
    }

    public void moveMessage(KudoMessage m, Label target) {
        // TODO Auto-generated method stub
    }
}
