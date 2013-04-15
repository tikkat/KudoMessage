package se.kudomessage.jessica;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.os.Bundle;

public class OAuthModel implements IOAuthModel{
	private String oauthScope = "oauth2:https://mail.google.com https://www.google.com/m8/feeds https://www.googleapis.com/auth/userinfo.email";
	
	@Override
	public void requestAccessToken() {
		AccountManager am = AccountManager.get(Globals.getActivity());
		am.getAuthTokenByFeatures("com.google", oauthScope, null, Globals.getActivity(), null, null, new OAuthCallback(), null);
	}

	@Override
	public void revokeAccessToken() {
		AccountManager am = AccountManager.get(Globals.getActivity());
		am.invalidateAuthToken("com.google", Globals.getAccessToken());
	}

	@Override
	public void renewAccessToken() {
		AccountManager am = AccountManager.get(Globals.getActivity());
		am.invalidateAuthToken("com.google", Globals.getAccessToken());
		am.getAuthTokenByFeatures("com.google", oauthScope, null, Globals.getActivity(), null, null, new OAuthCallback(), null);
	}
	
	private class OAuthCallback implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			try {
				Globals.setAccessToken(result.getResult().getString(AccountManager.KEY_AUTHTOKEN));
			} catch (OperationCanceledException e) {
				// Was denied the access, didn't get an access token.
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
