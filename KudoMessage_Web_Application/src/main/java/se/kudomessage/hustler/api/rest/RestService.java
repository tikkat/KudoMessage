package se.kudomessage.hustler.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import se.kudomessage.hustler.GmailModel;
import se.kudomessage.hustler.GmailModelList;
import se.kudomessage.hustler.KudoMessage;
import se.kudomessage.hustler.PushHandler;
import se.kudomessage.hustler.Utils;

// Här skriver man in basurlen, nu är den på /api/rest/ enbart.
@Path("/")
public class RestService {

    @POST
    @Path("register-server")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String registerServer(String inputData) throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject input = new JSONObject(inputData);
        
        System.out.println("En server vill regga.");

        if (input.has("gcm")) {
            System.out.println("Det är en GCM-push server.");
            
            PushHandler.registerAndroidServer(input.getString("token"), input.getString("gcm"));
            response.put("response", "OK");
        }

        return response.toString();
    }

    @POST
    @Path("send-message")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String sendMessage(String inputData) throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject input = new JSONObject(inputData);

        String token = input.getString("token");
        String receiver = input.getString("receiver");
        String content = input.getString("content");
        String email = Utils.getUserInfo(token).get("email");
        
        GmailModelList.getGmailModel(token, email).saveMessageToPending(new KudoMessage(content, email, receiver));

        response.put("response", "OK");
        return response.toString();
    }

    @POST
    @Path("sent-message")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String sentMessage(String inputData) throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject input = new JSONObject(inputData);

        response.put("response", "OK");
        return response.toString();
    }

    @GET
    @Path("get-message")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getMessage(String inputData) throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject input = new JSONObject(inputData);

        response.put("response", "OK");
        return response.toString();
    }

    @GET
    @Path("get-messages")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getMessages(String inputData) throws JSONException {
        JSONObject response = new JSONObject();
        JSONObject input = new JSONObject(inputData);

        response.put("response", "OK");
        return response.toString();
    }
}