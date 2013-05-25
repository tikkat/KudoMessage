package se.kudomessage.torsken.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class ContactsModel implements Serializable {
    private Map<String, String> contacts = new HashMap<String, String>();
    
    public ContactsModel() {
    }
    
    public void addContact(String name, String number) {
        contacts.put(number, name);
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }
}
