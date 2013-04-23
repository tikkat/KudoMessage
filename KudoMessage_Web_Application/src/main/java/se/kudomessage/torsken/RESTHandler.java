/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage.torsken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

@ManagedBean
@RequestScoped
public class RESTHandler {
    public static String post(String path, String data) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost("http://localhost:8080/KudoMessage_Web_Application/api/rest/" + path);

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
            return result;
        } catch (IOException ex) {
            return null;
        }
    }
}
