package se.kudomessage;

import java.io.IOException;

import javax.mail.MessagingException;

public class Hustler {

	/**
	 * @param args
	 * @throws MessagingException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		GmailHandler gh = new GmailHandler();
		
		// Ladda upp ett sms till pending på gmail och behåll id't
		int id = gh.saveMessageToPending("Mejl att flytta!", "receiver", "sender");
		
		// Flytta smset med id't till standard
		gh.moveMessage(id, GmailHandler.Labels.PENDING, GmailHandler.Labels.STANDARD);
		System.out.println("Flyttade mejlet..");
	}

}
