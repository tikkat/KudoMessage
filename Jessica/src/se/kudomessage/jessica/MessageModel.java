package se.kudomessage.jessica;

public class MessageModel {
	private MessageModel(){}
	
	public static void sendMessage(KudoMessage message) {
		SMSModel.sendSMS(message);
		PushModel.pushMessage(message);
	}

	public void receivedMessage(KudoMessage m) {
		PushModel.pushMessage(m);
	}

	public void addMessage(KudoMessage m) {
		// TODO Auto-generated method stub
		
	}

}
