package se.kudomessage.jessica;

public class MessageModel {
	private MessageModel(){}
	
	public static void sendMessage(KudoMessage message) {
		SMSModel.sendSMS(message);
		// Message shouldn't be pushed up on incoming?
		//PushModel.pushMessage(message);
	}

	public static void receivedMessage(KudoMessage m) {
		PushModel.pushMessage(m);
	}

	public void addMessage(KudoMessage m) {
		// TODO Auto-generated method stub
		
	}

}
