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
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private static final int DIALOG_ACCOUNTS = 0;
	private static final String AUTH_TOKEN_TYPE = "mail";
	Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    AccountManager accountManager;
    Account account;
	protected String token;

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
		showDialog(DIALOG_ACCOUNTS);
		accountManager.getAuthToken(account, AUTH_TOKEN_TYPE, null, true, new AccountManagerCallback<Bundle>() {
			@Override
			public void run(AccountManagerFuture<Bundle> future) {
				try {
					// If the user has authorized your application to use the Gmail API
					
					token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);

				} catch (OperationCanceledException e) {
					// TODO: The user has denied you access to the API, you should handle that
				} catch (Exception e) {
					handleException(e);
				}
			}

		}, null);
	}
	
	protected void handleException(Exception e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected Dialog onCreateDialog(int id) {
	  switch (id) {

		 // Asks the user which google account he or she wants to use
	    case DIALOG_ACCOUNTS:
	      AlertDialog.Builder builder = new AlertDialog.Builder(this);
	      builder.setTitle("Select a Google account");
	      final Account[] accounts = accountManager.getAccountsByType("com.google");
	      final int size = accounts.length;
	      String[] names = new String[size];
	      for (int i = 0; i < size; i++) {
	        names[i] = accounts[i].name;
	      }
	      builder.setItems(names, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	          // Stuff to do when the account is selected by the user
	          gotAccount(accounts[which]);
	        }
	      });
	      return builder.create();
	  }
	  return null;
	}

	protected void gotAccount(Account account) {
		if(account != null){
			this.account = account;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
