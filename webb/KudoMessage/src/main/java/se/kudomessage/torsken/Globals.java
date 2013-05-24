package se.kudomessage.torsken;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import javax.enterprise.context.SessionScoped;
import org.icefaces.application.PortableRenderer;

@SessionScoped
public class Globals implements Serializable {
    public Globals() {
    }
    
    private PortableRenderer pr;
    private String email = "";
    private String accessToken = "";
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public PortableRenderer getPr() {
        return pr;
    }

    public void setPr(PortableRenderer pr) {
        this.pr = pr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }
}