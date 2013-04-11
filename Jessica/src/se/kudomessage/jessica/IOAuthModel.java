package se.kudomessage.jessica;

public interface IOAuthModel {
	void requestAccessToken();
	void revokeAccessToken();
	void renewAccessToken();
}
