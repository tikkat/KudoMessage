package se.kudomessage.jessica;

import java.util.List;

public interface IGmailModel {
	void addReceivedMessage(KudoMessage m);
	void addSentMessage(KudoMessage m);
	KudoMessage getMessage(String id);
	List<KudoMessage> findMessage(String query);
	void moveMessage(KudoMessage m, Label target);
}
