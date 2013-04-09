/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kudomessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONObject;

/**
 * This class will handle the communication between, for example, Hustler and Torsken. 
 * The communication will be by Sockets.
 * @author Philip
 */
public class SocketHandler {
    
    private static Socket socket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;
    
    public static void openSocketCommunication () {
        try {
            socket = new Socket(Constants.SOCKET_CONNECTION_IP, Constants.SOCKET_CONNECTION_PORT);
            
            OutputStreamWriter outstream = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            out = new PrintWriter(outstream, true);
            
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch(UnknownHostException e) {
            System.err.println("Couldn't find the host.");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    public static boolean checkIfSocketConnectionIsUp () {
        if (socket != null && in != null && out != null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * This method will send the access token to Hustler and initialize a socket communication
     * Hustler will listen for a "init" string to start.
     * @param accessToken The access token from Google.
     */
    public static void sendInitializeViaSocket ( String accessToken ) {
        JSONObject token = new JSONObject();
        token.put("action", "init");
        token.put("token", accessToken);
        
        out.println(token);
        out.flush();
    }
    
    public static void sendObjectViaSocket (JSONObject objectWithMSG) {
        out.println(objectWithMSG.toString());
        //out.println("CLOSE");
        out.flush();
    }
}
