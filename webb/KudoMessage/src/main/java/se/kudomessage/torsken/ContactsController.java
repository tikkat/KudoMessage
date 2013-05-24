package se.kudomessage.torsken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.json.JSONException;
import org.json.JSONObject;

@RequestScoped
@Named
public class ContactsController {
    @Inject
    ContactsModel model;
    
    @Inject
    Globals globals;
    
    @Inject
    BackingBean view;
    
    public ContactsController() {
    }
    
    public void addContact(String name, String number) {
        model.addContact(name, number);
    }
    
    public boolean hasContact(String number) {
        return model.getContacts().containsKey(number);
    }

    public String getNameOfContact(String number) {
        if (hasContact(number)) {
            return model.getContacts().get(number);
        } else {
            return number;
        }
    }
    
    public String getNumberOfContact(String name) {
        for (Entry<String, String> entry : model.getContacts().entrySet()) {
            if (entry.getValue().equals(name))
                return entry.getKey();
        }
        
        return "";
    }
    
    public List<String> getNameOfContacts() {
        List<String> sortedContacts = new ArrayList<String>();
        sortedContacts.addAll(model.getContacts().values());
        Collections.sort(sortedContacts);
        
        return sortedContacts;
    }
    
    public void createContact(String name, String number) {
        try {
            JSONObject output = new JSONObject();
            output.put("action", "create-contact");
            output.put("name", name);
            output.put("number", number);
            
            globals.getOut().println(output.toString());
            globals.getOut().flush();
            
            addContact(name, number);
        } catch (JSONException ex) {
        }
        view.setTmpContactName("");
    }
}