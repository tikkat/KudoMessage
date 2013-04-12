package se.kudomessage.jessica;

public class MessageModel {
	private static IGmailModel _gmailModel;
	
	public static void setGmailModel(IGmailModel gmailModel) {
		_gmailModel = gmailModel;
	}

	public void sendMessage(String id) {
		KudoMessage m = _gmailModel.getMessage(id);
		SMSModel.sendSMS(m);
		PushModel.pushMessage(m);
	}

	public void receivedMessage(KudoMessage m) {
		_gmailModel.addReceivedMessage(m);
		PushModel.pushMessage(m);
	}

	public void addMessage(KudoMessage m) {
		// TODO Auto-generated method stub
		
	}

}
