package se.kudomessage.jessica;

public class Globals implements IGlobals{
	
	private IGmailModel gmailModel;
	private String accessToken;
	private String email;
	
	@Override
	public IGmailModel getGmailModel() {
		return this.gmailModel;
	}

	@Override
	public void setGmailModel(IGmailModel gmailModel) {
		this.gmailModel = gmailModel;		
	}

	@Override
	public String getAccessToken() {
		return this.accessToken;
	}

	@Override
	public void setAccessToken(String token) {
		this.accessToken = token;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

}
