package se.kudomessage.kudomessage_jessica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	
	Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    AccountManager accountManager;
    Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		this.initOAuth();
		
		String regId = GCMRegistrar.getRegistrationId(this);
		
		if (regId.equals("")) {
			GCMRegistrar.register(this, Constants.SENDER_ID);
		}
		
		try {
            socket = new Socket(Constants.IP_ADDRESS, Constants.PORT);
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(UnknownHostException e) {
            System.err.println("Couldn't find the host.");
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		out.println("INITIALIZE");
		out.println("API-KEY");
		
		out.println("REGISTER_ANDROID_DEVICE");
		out.println(regId);
	}

	/**
	 * Initializes the OAuthentication communication with Google.
	 * Asks which account to use and then requests the oAuth tooken
	 * for that user.
	 */
	private void initOAuth() {
		accountManager = AccountManager.get(this);
		Account[] accounts = accountManager.getAccountsByType("com.google");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
