package se.kudomessage.torsken;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.faces.bean.ManagedBean;
import org.json.JSONObject;

@ManagedBean
public class SocketHandler extends Thread {

    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    public SocketHandler() {
        // Must be here.
    }

    public SocketHandler(Socket socket, BufferedReader in, PrintWriter out) {
        SocketHandler.socket = socket;
        SocketHandler.in = in;
        SocketHandler.out = out;
    }

    public static PrintWriter getOut() {
        return out;
    }

    @Override
    public void run() {
        String inputString;
        JSONObject input;

        while (true) {
            try {
                inputString = in.readLine();
            } catch (Exception e) {
                continue;
            }

            if (inputString == null || inputString.isEmpty()) {
                continue;
            }

            try {
                input = new JSONObject(inputString);

                if (input.getString("action").equals("new-message")) {
                    JSONObject json = input.getJSONObject("message");
                    
                    KudoMessage message = new KudoMessage();
                    message.content = json.getString("content");
                    message.origin = json.getString("origin");
                    message.addReceiver(json.getString("receiver"));
                    
                    ConversationsHolder.getInstance().addMessage(message);
                }
            } catch (Exception e) {}
        }
    }
}