package se.kudomessage.torsken;

import com.google.gdata.client.contacts.ContactsService;
import java.util.ArrayList;
import javax.faces.bean.SessionScoped;

@SessionScoped
public class ClientUser {
    private static ClientUser instance;
    private String accessToken = "";
    private String email = "";
    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    ContactsService contactsService;
    
    protected ClientUser() {
    }
    
    public static ClientUser getInstance() {
        if (instance == null){
            instance = new ClientUser();
        }
          
        return instance;
    }
    
    public void authenticateContacts () {
        contactsService = ContactsAPI.authenticateId(accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ContactsService getContactsService() {
        return contactsService;
    }
}
