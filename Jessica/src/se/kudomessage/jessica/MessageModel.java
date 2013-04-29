package se.kudomessage.jessica;

public class MessageModel {
	private MessageModel(){}

<<<<<<< HEAD
	public void sendMessage(KudoMessage message) {
		SMSModel.sendSMS(message);
		PushModel.pushMessage(message);
=======
	public static void sendMessage(String id) {
		KudoMessage m = new KudoMessage();
		SMSModel.sendSMS(m);
		PushModel.pushMessage(m);
>>>>>>> 5525e606ef1466c801705e13ce50a3a9c0c0cc95
	}

	public void receivedMessage(KudoMessage m) {
		PushModel.pushMessage(m);
	}

	public void addMessage(KudoMessage m) {
		// TODO Auto-generated method stub
		
	}

}
