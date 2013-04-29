package se.kudomessage.jessica;

import android.app.Activity;

public class Globals{
	
	private static String accessToken;
	private static String email;
	private static Activity activity;
	private static String apiPath = "http://192.168.43.82:8080/KudoMessage_Web_Application/api/rest/";
	private static String GCM;

	public static String getAccessToken() {
		return accessToken;
	}

	public static void setAccessToken(String accessToken) {
		Globals.accessToken = accessToken;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		Globals.email = email;
	}
	
	public static Activity getActivity() {
		return activity;
	}

	public static void setActivity(Activity activity) {
		Globals.activity = activity;
	}
	
	public static void setApiPath(String apiPath){
		Globals.apiPath = apiPath;
	}

	public static String getApiPath(){
		return apiPath;
	}

	public static String getGCM() {
		return GCM;
	}

	public static void setGCM(String GCM) {
		Globals.GCM = GCM;
	}
}
