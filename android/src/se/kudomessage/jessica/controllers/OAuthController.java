package se.kudomessage.jessica.controllers;

import se.kudomessage.jessica.Globals;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.os.Bundle;

public class OAuthController {
	private final static String OAUTH_SCOPE = "oauth2:https://mail.google.com https://www.google.com/m8/feeds https://www.googleapis.com/auth/userinfo.email";

	public static void requestAccessToken() {
		AccountManager am = AccountManager.get(Globals.getActivity());
		am.getAuthTokenByFeatures("com.google", OAUTH_SCOPE, null, Globals.getActivity(), null, null, new OAuthCallback(), null);
	}

	public static void revokeAccessToken() {
		AccountManager am = AccountManager.get(Globals.getActivity());
		am.invalidateAuthToken("com.google", Globals.getAccessToken());
	}

	private static class OAuthCallback implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			try {
				String accessToken = result.getResult().getString(AccountManager.KEY_AUTHTOKEN);
				String email = result.getResult().getString(AccountManager.KEY_ACCOUNT_NAME);
				Globals.setAccessToken(accessToken);
				Globals.setEmail(email);

				((MainController) Globals.getActivity()).init();
			} catch (OperationCanceledException e) {
				// Was denied the access, didn't get an access token.
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}