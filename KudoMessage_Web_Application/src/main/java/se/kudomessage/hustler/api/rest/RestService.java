package se.kudomessage.hustler.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// Här skriver man in basurlen, nu är den på /api/rest/ enbart.
@Path("/")
public class RestService {

    // Körs om TEXT_PLAIN efterfrågas
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String simplePlainText() {
        return "Hello from KudoMessage API";
    }

    // Körs om HTML efterfrågas
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String simpleHtml() {
        return "<html> " + "<title>" + "Hello from KudoMessage API" + "</title>"
                + "<body><h1>" + "Hello from KudoMessage API" + "</body></h1>" + "</html> ";
    }
    
    // Körs om JSON efterfrågas
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String simpleJSON() {
        return "{ \"message\" : \"Hello from KudoMessage API\" }";
    }
}