import java.io.IOException;

import javax.mail.MessagingException;

public class Hustler {

	/**
	 * @param args
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws MessagingException, IOException {
		// TODO Auto-generated method stub
		
		GmailHandler gh = new GmailHandler();
		
		// Ladda upp ett sms till pending på gmail och behåll id't
		long id = gh.saveMessageToPending("Mejl att flytta! 1", "0764292920", "011011");
		
		// Flytta smset med id't till standard
		gh.moveMessage(id, GmailHandler.Labels.PENDING, GmailHandler.Labels.STANDARD);
	}

}
