package se.kudomessage.torsken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.icefaces.application.PushRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

@SessionScoped
@ManagedBean
public class BackendBean {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private boolean loaded = false;

    public String init() {
        if (!loaded) {
            loaded = true;
            doInit();
        }

        return "";
    }

    private void doInit() {
        try {
            socket = new Socket(CONSTANTS.SERVER_ADDRESS, CONSTANTS.SERVER_PORT);
        } catch (Exception e) {
            e.printStackTrace();
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
                
                // Read new messages in new thread.
                (new SocketHandler(socket, in, out)).start();
            }
        }
    }
}