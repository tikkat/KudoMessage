package se.kudomessage.jessica;

public class MessageModel {

	public void sendMessage(KudoMessage m) {
		m = Globals.getGmailModel().getMessage(m);
		SMSModel.sendSMS(m);
		PushModel.pushMessage(m);
	}

	public void receivedMessage(KudoMessage m) {
		m = Globals.getGmailModel().addReceivedMessage(m);
		PushModel.pushMessage(m);
	}

	public void addMessage(KudoMessage m) {
		// TODO Auto-generated method stub
		
	}

}
