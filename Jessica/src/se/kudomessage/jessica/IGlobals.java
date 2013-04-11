package se.kudomessage.jessica;

public interface IGlobals {
	IGmailModel getGmailModel();
	void setGmailModel(IGmailModel gmailModel);
	
	String getAccessToken();
	void setAccessToken(String token);
	
	String getEmail();
	void setEmail(String email);
	
}
