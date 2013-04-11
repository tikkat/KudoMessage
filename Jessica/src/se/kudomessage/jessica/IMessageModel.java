package se.kudomessage.jessica;

public interface IMessageModel {
	
	void sendMessage(KudoMessage m);
	void reveivedMessage(KudoMessage m);
	void addMessage(KudoMessage m);

}
