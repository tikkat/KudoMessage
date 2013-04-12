package se.kudomessage.jessica;

import android.app.Activity;

public class Globals{
	
	private static String _accessToken;
	private static String _email;
	private static Activity _activity;

	public static String getAccessToken() {
		return _accessToken;
	}

	public static void setAccessToken(String accessToken) {
		_accessToken = accessToken;
	}

	public static String getEmail() {
		return _email;
	}

	public static void setEmail(String email) {
		_email = email;
	}
	
	public static Activity getActivity() {
		return _activity;
	}

	public static void setEmail(Activity activity) {
		_activity = activity;
	}

}
