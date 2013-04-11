package se.kudomessage.kudomessage_jessica;

import com.google.android.gcm.GCMRegistrar;
import android.os.Bundle;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.util.Log;

public class MainActivity extends Activity {
	private static SMSHandler smsHandler = null;

	private static String token;
	private static String GCMKey;

	private String oauthScope = "oauth2:https://mail.google.com https://www.google.com/m8/feeds https://www.googleapis.com/auth/userinfo.email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		smsHandler = new SMSHandler(this);

		this.initOAuth();

		GCMKey = GCMRegistrar.getRegistrationId(this);

		if (GCMKey.equals("")) {
			GCMRegistrar.register(this, Constants.SENDER_ID);
		}

		if (!GCMKey.equals(""))
			Log.v("SMSTagTracker", "Got an GCM-key: " + GCMKey);
		else
			Log.v("SMSTagTracker", "Didn't get an GCM-key");
		
		new SocketHandler();
	}

	/**
	 * Asks which account to use and then requests an access token for that account.
	 */
	private void initOAuth() {
		AccountManager am = AccountManager.get(this);
		am.getAuthTokenByFeatures("com.google", oauthScope, null, this, null, null, new OAuthCallback(), null);
	}

	private class OAuthCallback implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> result) {
			try {
				token = result.getResult().getString(AccountManager.KEY_AUTHTOKEN);
				Log.v("SMSTagTracker", "Got an access token: " + token);
			} catch (OperationCanceledException e) {
				Log.v("SMSTagTracker", "Was denied the access, didn't get an access token.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static SMSHandler getSMSHandler() {
		return smsHandler;
	}
	
	public static String getToken() {
		return token;
	}
	
	public static String getGCM() {
		return GCMKey;
	}
}
