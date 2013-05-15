package se.kudomessage.jessica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PushModel {

	private PushModel(){}

	public static void pushMessage(KudoMessage m) {
		post("push-message", m.toJSON());
	}

	public static void registerServer() {
		JSONObject pushData = new JSONObject();
		try {
			pushData.put("gcm", Globals.getGCM());
			pushData.put("protocol", "SMS");
			
			post("register-server", pushData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String post(final String method, final JSONObject data) {
		// TODO: Make the return work!
		
		try {
			data.put("token", Globals.getAccessToken());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Thread( new Runnable(){
			@Override
			public void run() {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(Globals.getApiPath() + method);

				StringEntity input;
				try {
					input = new StringEntity(data.toString(), "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					input = null;
				}

				input.setContentType("text/plain");
				postRequest.setEntity(input);

				HttpResponse response;
				BufferedReader br = null;
				try {
					response = httpClient.execute(postRequest);
					br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
				} catch (IOException ex) {}

				String result = "";
				String output  = "";
				try {
					while ((output = br.readLine()) != null) {
						result += output;
					}

					httpClient.getConnectionManager().shutdown();
					Log.e("http-Response:", result);
				} catch (IOException ex) {}
			}
		}).start();
		
		return "Unimplemented result handeling";
	}

	public static boolean testServer() {

		JSONObject pushData = new JSONObject();
		try {
			pushData.put("validate", "Requested");
			
			if( post("test", pushData).equals("OK") ){
				return true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}