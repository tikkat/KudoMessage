package se.kudomessage.jessica;

public class MessageModel {
	private MessageModel(){}

	public static void sendMessage(String id) {
		KudoMessage m = new KudoMessage();
		SMSModel.sendSMS(m);
		PushModel.pushMessage(m);
	}

	public void receivedMessage(KudoMessage m) {
		PushModel.pushMessage(m);
	}

	public void addMessage(KudoMessage m) {
		// TODO Auto-generated method stub
		
	}

}
