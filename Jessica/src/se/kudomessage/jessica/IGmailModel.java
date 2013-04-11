package se.kudomessage.jessica;

import java.util.List;

public interface IGmailModel {
	KudoMessage addReceivedMessage(KudoMessage m);
	KudoMessage addSentMessage(KudoMessage m);
	KudoMessage getMessage(KudoMessage m);
	List<KudoMessage> findMessage(String query);
	void moveMessage(KudoMessage m, Label target);
}
