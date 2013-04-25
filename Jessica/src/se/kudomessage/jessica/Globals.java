package se.kudomessage.jessica;

import android.app.Activity;

public class Globals{
	
	private static String _accessToken;
	private static String _email;
	private static Activity _activity;
	private static String _apiPath = "http://domain.com:8080/KudoMessage_Web_Application/api/rest/";
	private static String GCM;

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

	public static void setActivity(Activity activity) {
		_activity = activity;
	}
	
	public static void setApiPath(String apiPath){
		_apiPath = apiPath;
	}

	public static String getApiPath(){
		return _apiPath;
	}

	public static String getGCM() {
		return GCM;
	}

	public static void setGCM(String GCM) {
		Globals.GCM = GCM;
	}
}
