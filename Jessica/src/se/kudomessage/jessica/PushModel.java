package se.kudomessage.jessica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class PushModel {

	private PushModel(){}
	
	public static void pushMessage(KudoMessage m) {
		//post("pushMessage", m.toString());
	}

	public static void registerServer() {
		
		final String pushData = 
		"{" +
			"\"token\":\""+Globals.getAccessToken()+"\"," +
			"\"gcm\":\""+Globals.getGCM()+"\"," +
			"\"protocol\":\"SMS\"" +
		"}";

		new Thread( new Runnable(){
			@Override
			public void run() {
				post("register-server", pushData);
			}
		}).start();
	}
	
    public static String post(String method, String data) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(Globals.getApiPath() + method);

        StringEntity input;
        try {
            input = new StringEntity(data, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
        
        input.setContentType("text/plain");
        postRequest.setEntity(input);

        HttpResponse response;
        BufferedReader br;
        try {
            response = httpClient.execute(postRequest);
            br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        } catch (IOException ex) {
            return null;
        }
        
        String result = "";
        String output  = "";
        try {
            while ((output = br.readLine()) != null) {
                result += output;
            }
            
            httpClient.getConnectionManager().shutdown();
            Log.e("http-Response:", result);
            return result;
        } catch (IOException ex) {
            return null;
        }
    }

}
