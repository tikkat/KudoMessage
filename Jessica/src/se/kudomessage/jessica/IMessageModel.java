package se.kudomessage.jessica;

public interface IMessageModel {
	
	void sendMessage(KudoMessage m);
	void receivedMessage(KudoMessage m);
	void addMessage(KudoMessage m);

}
