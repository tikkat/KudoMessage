package se.kudomessage.torsken;

import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

@Stateless
public class AsyncBean {

    @Inject
    private TmpMessages tmpMessages;
    
    @Asynchronous
    public void sendMessage(PrintWriter out, KudoMessage message) {
        try {
            JSONObject output = new JSONObject();
            output.put("action", "send-message");
            output.put("message", message.toJSON());
            
            out.println(output.toString());
            out.flush();    
        } catch (JSONException ex) {
        }
    }

    @Asynchronous
    public void readMessages(BufferedReader in, String pushGroup) {
        String inputString;
        JSONObject input;

        int countErrors = 0;

        while (true) {
            if (countErrors > 5) {
                System.err.println("More errors than 5, exiting socket.");
                break;
            }

            try {
                inputString = in.readLine();
            } catch (Exception e) {
                countErrors++;
                continue;
            }

            if (inputString == null || inputString.isEmpty()) {
                countErrors++;
                continue;
            }

            countErrors = 0;

            try {
                input = new JSONObject(inputString);

                if (input.getString("action").equals("new-message")) {
                    JSONObject json = input.getJSONObject("message");

                    KudoMessage message = new KudoMessage();
                    message.content = json.getString("content");
                    message.origin = json.getString("origin");
                    message.addReceiver(json.getString("receiver"));

                    tmpMessages.addMessage(message, pushGroup);
                }
            } catch (Exception e) {
            }
        }
    }
}