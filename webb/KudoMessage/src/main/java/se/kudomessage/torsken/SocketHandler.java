package se.kudomessage.torsken;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.faces.bean.ManagedBean;
import org.icefaces.application.PushRenderer;
import org.json.JSONArray;
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
        
        System.out.println("####### LOADED SOCKETHANDLER() ###");

        out.println("CLIENT");
        out.flush();

        try {
            JSONObject output = new JSONObject();
            output.put("action", "init");
            output.put("token", Globals.accessToken);

            out.println(output.toString());
            out.flush();

            // First answer from server is the email.
            Globals.email = in.readLine();

            // Global PushRenderer
            Globals.pr = PushRenderer.getPortableRenderer();
            ConversationsHolder.getInstance().addViewToPush();

            // Get contacts
            output = new JSONObject();
            output.put("action", "get-contacts");

            out.println(output.toString());
            out.flush();

            JSONObject c = new JSONObject(in.readLine());
            JSONArray d = c.getJSONArray("contacts");

            for (int i = 0; i < d.length(); i++) {
                String name = d.getJSONObject(i).getString("name");
                String number = d.getJSONObject(i).getString("number");

                Globals.contacts.put(number, name);
            }

            // Load the first emails
            output = new JSONObject();
            output.put("action", "get-messages");
            output.put("lower", "0");
            output.put("count", "5");

            out.println(output.toString());
            out.flush();

            JSONObject e = new JSONObject(in.readLine());
            JSONArray f = e.getJSONArray("messages");

            for (int i = 0; i < f.length(); i++) {
                KudoMessage message = new KudoMessage();
                message.content = f.getJSONObject(i).getString("content");
                message.origin = f.getJSONObject(i).getString("origin");
                message.addReceiver(f.getJSONObject(i).getString("receiver"));

                ConversationsHolder.getInstance().addMessage(message);
            }
        } catch (Exception ex) {
            System.err.println("Something wrong in action init: " + ex.toString());
        }
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
            
            if (inputString.equals("CLOSE")) {
                System.out.println("### GOT CLOSE ###");
                
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (Exception e) {}

                break;
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