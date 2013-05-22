package se.kudomessage.torsken;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import org.icefaces.application.PushRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ManagedBean
public class ContactsController {
    public static Map<String, String> contacts = new HashMap<String, String>();
    private String tmpContactName = "";

    public boolean hasContact(String number) {
        return contacts.containsKey(number);
    }

    public String getNameOfContact(String number) {
        if (hasContact(number)) {
            return contacts.get(number);
        } else {
            return "" + number;
        }
    }
    
    public Collection<String> getNameOfContacts () {
        return contacts.values();
    }

    public String getTmpContactName() {
        return tmpContactName;
    }

    public void setTmpContactName(String tmpContactName) {
        this.tmpContactName = tmpContactName;
    }
    
    public void createContactFromJSF ( String name, String number ) {
        if (!name.equals("")) {  
            createContact(name, number);
            tmpContactName = "";
        }
    }
    
    public static void createContact(String name, String number) {
        try {
            JSONObject output = new JSONObject();
            output.put("action", "create-contact");
            output.put("name", name);
            output.put("number", number);
            
            SocketHandler.getOut().println(output.toString());
            SocketHandler.getOut().flush();
            
            contacts.put(number, name);            
        } catch (JSONException ex) {}
    }

    public static void getContacts() {
        try {
            JSONObject output = new JSONObject();
            output.put("action", "get-contacts");

            SocketHandler.getOut().println(output.toString());
            SocketHandler.getOut().flush();

            JSONObject c = new JSONObject(SocketHandler.getIn().readLine());
            JSONArray d = c.getJSONArray("contacts");

            for (int i = 0; i < d.length(); i++) {
                String name = d.getJSONObject(i).getString("name");
                String number = d.getJSONObject(i).getString("number");

                contacts.put(number, name);
            }
        } catch (Exception e) {
            System.out.println("### ERROR IN GET-CONTACTS");
        }
    }
}