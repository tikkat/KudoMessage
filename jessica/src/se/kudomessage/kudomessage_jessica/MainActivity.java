package se.kudomessage.kudomessage_jessica;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.util.Log;

public class MainActivity extends Activity {
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	private String token;
	private String GCMKey;

	private String oauthScope = "oauth2:https://mail.google.com https://www.google.com/m8/feeds https://www.googleapis.com/auth/userinfo.email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		this.initOAuth();

		GCMKey = GCMRegistrar.getRegistrationId(this);

		if (GCMKey.equals("")) {
			//GCMRegistrar.register(this, Constants.SENDER_ID);
		}

		Log.i("SMSTagTracker", "Got an GCM-key: " + GCMKey);

		new Thread(new Runnable() {
			public void run() {
				try {
					socket = new Socket(Constants.IP_ADDRESS, Constants.PORT);
					
					out = new PrintWriter(socket.getOutputStream(), true);
		            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		            
		            Log.i("SMSTagTracker", "Connected to socket " + Constants.IP_ADDRESS + ":" + Constants.PORT);
		        } catch (UnknownHostException e) {
		        	Log.i("SMSTagTracker", "Couldn't find the host.");
		        } catch (ConnectException e) {
		        	Log.i("SMSTagTracker", "Failed to connect to socket " + Constants.IP_ADDRESS + ":" + Constants.PORT);
				} catch (Exception e) {
					Log.e("SMSTagTracker", "Exception", e);
				}
			}
		}).start();
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
				Log.i("SMSTagTracker", "Got an access token: " + token);
			} catch (OperationCanceledException e) {
				Log.i("SMSTagTracker", "Was denied the access, didn't get an access token.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
