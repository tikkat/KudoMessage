package se.kudomessage.torsken;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ContactsAPI {
    //------------------------------
    //  Remember to refactor the retreiveContact methods!!!
    //------------------------------
    public static ContactsService authenticateId(String token){

        ContactsService contactService = null;
        try{
            contactService = new ContactsService(Constants.APPLICATION_NAME);
            contactService.setAuthSubToken(token);
            
        }catch(Exception e){
            System.out.println(e);
        }
        return contactService;
    }
    
    public Contact retreiveContact ( String number, ContactsService cs ) throws MalformedURLException, IOException, ServiceException {
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        ContactFeed resultFeed = cs.getFeed(feedUrl, ContactFeed.class);
        
        for ( int i = 0; i < resultFeed.getEntries().size(); i++ ) {
            ContactEntry entry = resultFeed.getEntries().get(i);
            for ( int j = 0; j < entry.getPhoneNumbers().size(); j++ ) {
                if (entry.getPhoneNumbers().get(j).getPhoneNumber().equals(number)) {
                    Contact c = new Contact();
                    c.setNumbers(number);
                    c.setName(entry.getName().getFullName().getValue());
                }
            }
        }
        
        return null;
    }
    
    /**
     * Returns the number if there is no name for this contact
     * @param number
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws ServiceException 
     */
    public String exchangeNumberForName ( String number ) throws MalformedURLException, IOException, ServiceException {
        ContactsService cs = ClientUser.getInstance().getContactsService();
        Contact tmp = retreiveContact(number, cs);
        
        return (tmp == null) ? number : tmp.getName();
    }
    
    public ArrayList<Contact> retreiveAllContacts ( ContactsService cs ) throws MalformedURLException, IOException, ServiceException {
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        ContactFeed resultFeed = cs.getFeed(feedUrl, ContactFeed.class);
        ArrayList<Contact> tmp = new ArrayList<Contact>();
        
        for ( int i = 0; i < resultFeed.getEntries().size(); i++ ) {
            ContactEntry entry = resultFeed.getEntries().get(i);
            Contact tmpContact = new Contact();
            tmpContact.setName(entry.getName().getFullName().getValue());
            for ( int j = 0; j < entry.getPhoneNumbers().size(); j++ ) {
                tmpContact.setNumbers(entry.getPhoneNumbers().get(j).getPhoneNumber());
            }
            tmp.add(tmpContact);
        }
        
        
        return tmp;
    }
    
    public void deleteContact ( ContactsService cs, String number ) throws MalformedURLException {
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        ContactFeed resultFeed = cs.getFeed(feedUrl, ContactFeed.class);
        
        for ( int i = 0; i < resultFeed.getEntries().size(); i++ ) {
            ContactEntry entry = resultFeed.getEntries().get(i);
            for ( int j = 0; j < entry.getPhoneNumbers().size(); j++ ) {
                if (entry.getPhoneNumbers().get(j).getPhoneNumber().equals(number)) {
                    entry.delete();
                }
            }
        }
    }
    
    public void createContact ( ContactsService cs, String name, String number ) throws MalformedURLException, IOException, ServiceException {
        
        ContactEntry contact = new ContactEntry();
        Name n = new Name();
        final String NO_YOMI = null;
        n.setFullName(new FullName(name, NO_YOMI));
        contact.setName(n);
        
        PhoneNumber p = new PhoneNumber();
        p.setPhoneNumber(number);
        p.setLabel("label");
        contact.addPhoneNumber(p);
        
        
        URL postUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        cs.insert(postUrl, contact);
    }
}
