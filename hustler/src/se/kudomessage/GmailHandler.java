package se.kudomessage;

import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;

import com.sun.mail.imap.IMAPSSLStore;

public class GmailHandler {
	private Session session;
	
	private Folder standardFolder;
	private Folder pendingFolder;
	private Folder errorFolder;
	
	private String token;
	private String username;

	public GmailHandler(String token, String username) throws MessagingException {
		this.token = token;
		this.username = username;
		
		Security.addProvider(new OAuth2Provider());
		IMAPSSLStore store = connectByOAuth();
		
		Folder defaultFolder = store.getDefaultFolder();
		createAllFolders(defaultFolder);
		
		System.out.println("Skapade en Gmail-hanterare");
	}
	
	public static final class OAuth2Provider extends Provider {
		private static final long serialVersionUID = 1L;

		public OAuth2Provider() {
			super("Google OAuth2 Provider", 1.0,
					"Provides the XOAUTH2 SASL Mechanism");
			put("SaslClientFactory.XOAUTH2",
					"se.kudomessage.OAuth2SaslClientFactory");
		}
	}
	
	private IMAPSSLStore connectByOAuth() throws MessagingException {
		Properties properties = new Properties();
		properties.put("mail.imaps.sasl.enable", "true");
		properties.put("mail.imaps.sasl.mechanisms", "XOAUTH2");
		properties.put(OAuth2SaslClientFactory.OAUTH_TOKEN_PROP, token);
		
	    Session session = Session.getInstance(properties);
	    
	    IMAPSSLStore store = new IMAPSSLStore(session, null);
	    
	    System.out.println("Trying to connect to Gmail with username " + username + " and token " + token + ".");
	    store.connect(Constants.HOST, 993, username, "");
	    
	    return store;
	}
	
	private void createAllFolders(Folder defaultFolder) throws MessagingException {
		Folder tmpFolder = defaultFolder.getFolder("KudoMessage");
		if (!tmpFolder.exists())
			tmpFolder.create(Folder.HOLDS_MESSAGES);
		
		tmpFolder = tmpFolder.getFolder("SMS");
		if (!tmpFolder.exists())
			tmpFolder.create(Folder.HOLDS_MESSAGES);
		
		standardFolder = tmpFolder.getFolder("Standard");
		if (!standardFolder.exists())
			standardFolder.create(Folder.HOLDS_MESSAGES);
		
		pendingFolder = tmpFolder.getFolder("Pending");
		if (!pendingFolder.exists())
			pendingFolder.create(Folder.HOLDS_MESSAGES);

		errorFolder = tmpFolder.getFolder("Error");
		if (!errorFolder.exists())
			errorFolder.create(Folder.HOLDS_MESSAGES);
	}

	public void moveMessage(final int id, Labels from, Labels to) throws MessagingException {
		Folder folderFrom;
		Folder folderTo;

		switch (from) {
			case STANDARD:	folderFrom = standardFolder;
			break;
	
			case PENDING:	folderFrom = pendingFolder;
			break;
	
			case ERROR:		folderFrom = errorFolder;
			break;
	
			default:		return;
		}

		switch (to) {
			case STANDARD:	folderTo = standardFolder;
			break;
	
			case PENDING:	folderTo = pendingFolder;
			break;
	
			case ERROR:		folderTo = errorFolder;
			break;
	
			default:		return;
		}
		
		SearchTerm term = new SearchTerm() {
		    private static final long serialVersionUID = 1L;

			public boolean match(Message message) {
		        if (message.hashCode() == id) {
		                return true;
		        }
		        return false;
		    }
		};
		
		Message[] message = folderFrom.search(term);
		
		if (!folderTo.isOpen())
			folderTo.open(Folder.READ_WRITE);
		
		folderTo.appendMessages(message);
		message[0].setFlag(Flag.DELETED, true);
	}

	public int saveMessageToPending(String body, String receiver, String sender) {
		try {
			if (!pendingFolder.isOpen())
				pendingFolder.open(Folder.READ_WRITE);

			MimeMessage message = new MimeMessage(session);
	
			message.setFrom(new InternetAddress(sender));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			message.setSubject("SMS med XXX");
			message.setText(body);
	
			message.setFlag(Flag.DRAFT, true);
	
			MimeMessage draftMessages[] = {message};
			pendingFolder.appendMessages(draftMessages);
			
			Message latestMessage = pendingFolder.getMessage(pendingFolder.getMessageCount());
			
			return latestMessage.hashCode();
		} catch (MessagingException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public enum Labels {
		STANDARD, PENDING, ERROR
	}
}
