package se.kudomessage.jessica.models;

import se.kudomessage.jessica.KudoMessage;

public class MessageModel {
	public static void sendMessage(KudoMessage m) {
		SMSModel.sendSMS(m);
	}

	/*public static void receivedMessage(KudoMessage m) {
		PushController.pushMessage(m);
	}
	
	public static void sentMessage(KudoMessage m) {
		PushController.pushMessage(m);
	}

	public void addMessage(KudoMessage m) {
		// TODO Auto-generated method stub
		
	}*/
}