package se.kudomessage.jessica;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.util.Log;

public class OAuthController {
	private final static String OAUTH_SCOPE = "oauth2:https://mail.google.com https://www.google.com/m8/feeds https://www.googleapis.com/auth/userinfo.email";

	// TODO: THE REQUEST ACCESS TOKEN DOESN'T RENEW, FIX THIS UGLY HAX
	
	public static void requestAccessToken() {
		renewAccessToken();
	}
	
	private static void getNewAccessToken() {
		AccountManager am = AccountManager.get(Globals.activity);
		am.getAuthTokenByFeatures("com.google", OAUTH_SCOPE, null, Globals.activity, null, null, new OAuthCallback(), null);
	}

	public static void revokeAccessToken() {
		AccountManager am = AccountManager.get(Globals.activity);
		am.invalidateAuthToken("com.google", Globals.accessToken);
	}

	private static void renewAccessToken() {
		getNewAccessToken();
		revokeAccessToken();
		getNewAccessToken();
	}

	private static class OAuthCallback implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			try {
				Globals.accessToken = result.getResult().getString(AccountManager.KEY_AUTHTOKEN);
				Globals.email = result.getResult().getString(AccountManager.KEY_ACCOUNT_NAME);

				((MainActivity) Globals.activity).init();
			} catch (OperationCanceledException e) {
				// Was denied the access, didn't get an access token.
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
