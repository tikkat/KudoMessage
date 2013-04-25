package se.kudomessage.jessica;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		OAuthModel.requestAccessToken();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
