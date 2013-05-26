package se.kudomessage.torsken;

import se.kudomessage.torsken.controllers.ConversationsController;
import se.kudomessage.torsken.controllers.ContactsController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.icefaces.application.PushRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

@Named
@SessionScoped
public class SocketHandler implements Serializable {
    
    @Inject
    private AsyncBean asyncBean;
    
    @Inject
    private TmpMessages tmpMessages;

    @Inject
    private Globals globals;
    @Inject
    private ConversationsController conversationsController;
    @Inject
    private ContactsController contactsController;
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public String load() {
        if (socket == null) {
            start();
        }

        return "";
    }

    public void start() {
        try {
            socket = new Socket(CONSTANTS.SERVER_ADDRESS, CONSTANTS.SERVER_PORT);
        } catch (Exception e) {
        }

        if (socket != null) {
            in = null;
            out = null;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

                OutputStreamWriter outstream = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
                out = new PrintWriter(outstream, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (in != null && out != null) {
                System.out.println("Connected to server " + CONSTANTS.SERVER_ADDRESS + ":" + CONSTANTS.SERVER_PORT);
                
                globals.setOut(out);
                init();
                
                asyncBean.readMessages(in, globals.getEmail());
            }
        }
    }

    private void init() {
        // PushRenderer
        globals.setPr(PushRenderer.getPortableRenderer());
        tmpMessages.setPr(PushRenderer.getPortableRenderer());

        out.println("CLIENT");
        out.flush();

        try {
            JSONObject output = new JSONObject();
            output.put("action", "init");
            output.put("token", globals.getAccessToken());

            out.println(output.toString());
            out.flush();

            // First answer from server is the email.
            globals.setEmail(in.readLine());
            
            // Add to push
            PushRenderer.addCurrentSession(globals.getEmail());

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

                contactsController.addContact(name, number);
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
                
                conversationsController.addMessage(message);
            }
            
            conversationsController.loadFirst();
        } catch (Exception ex) {
            System.err.println("Something wrong in init: " + ex.toString());
        }
    }
}