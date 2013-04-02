import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.gimap.*;

public class GmailHandler {
	private Session session;
	
	private GmailFolder standardFolder;
	private GmailFolder pendingFolder;
	private GmailFolder errorFolder;

	public GmailHandler() throws MessagingException {
		Properties properties = new Properties();
		properties.setProperty("mail.store.protocol", "gimaps");

		session = Session.getDefaultInstance(properties);
		GmailSSLStore store = (GmailSSLStore) session.getStore("gimaps");
		store.connect(Constants.HOST, Constants.USERNAME, Constants.PASSWORD);
		
		Folder defaultFolder = store.getDefaultFolder();
		createAllFolders(defaultFolder);
	}
	
	private void createAllFolders(Folder defaultFolder) throws MessagingException {
		Folder tmpFolder = defaultFolder.getFolder("KudoMessage");
		if (!tmpFolder.exists())
			tmpFolder.create(Folder.HOLDS_MESSAGES);
		
		tmpFolder = tmpFolder.getFolder("SMS");
		if (!tmpFolder.exists())
			tmpFolder.create(Folder.HOLDS_MESSAGES);
		
		standardFolder = (GmailFolder) tmpFolder.getFolder("Standard");
		if (!standardFolder.exists())
			standardFolder.create(Folder.HOLDS_MESSAGES);
		
		pendingFolder = (GmailFolder) tmpFolder.getFolder("Pending");
		if (!pendingFolder.exists())
			pendingFolder.create(Folder.HOLDS_MESSAGES);

		errorFolder = (GmailFolder) tmpFolder.getFolder("Error");
		if (!errorFolder.exists())
			errorFolder.create(Folder.HOLDS_MESSAGES);
	}

	public void moveMessage(long id, Labels from, Labels to) throws MessagingException, IOException {
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
		
		GmailMsgIdTerm term = new GmailMsgIdTerm(id);
		Message[] messagesInThread = folderFrom.search(term);
		
		if (!folderTo.isOpen())
			folderTo.open(Folder.READ_WRITE);
		
		folderTo.appendMessages(messagesInThread);
		messagesInThread[0].setFlag(Flag.DELETED, true);
	}

	public long saveMessageToPending(String body, String receiver, String sender) throws IOException {
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
			
			GmailMessage latestMessage = (GmailMessage) pendingFolder.getMessage(pendingFolder.getMessageCount());
			
			return latestMessage.getMsgId();
		} catch (MessagingException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public enum Labels {
		STANDARD, PENDING, ERROR
	}
}
