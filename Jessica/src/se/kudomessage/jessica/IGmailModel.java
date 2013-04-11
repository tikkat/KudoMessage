package se.kudomessage.jessica;

public interface IGmailModel {
	void addReceivedMessage(KudoMessage m);
	void addSentMessage(KudoMessage m);
	void getMessage(KudoMessage m);
	void findMessage(String query);
	void moveMessage(KudoMessage m, Label target);
}
