package se.kudomessage.jessica;

public class MessageModel {
	private static IGmailModel _gmailModel;
	
	private MessageModel(){}
	
	public static void setGmailModel(IGmailModel gmailModel) {
		_gmailModel = gmailModel;
	}

	public void sendMessage(KudoMessage message) {
		SMSModel.sendSMS(message);
		PushModel.pushMessage(message);
	}

	public void receivedMessage(KudoMessage m) {
		_gmailModel.addReceivedMessage(m);
		PushModel.pushMessage(m);
	}

	public void addMessage(KudoMessage m) {
		// TODO Auto-generated method stub
		
	}

}
