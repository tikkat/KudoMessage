package se.kudomessage.torsken;

import java.util.ArrayList;

public class Contact {
    
    private String firstName;
    private String lastName;
    private ArrayList<String> numbers = new ArrayList<String>();
    private String email;
    private String name;
    
    public Contact () {
        
    }
    
    public Contact ( String name, String number ) {
        this.name = name;
        this.numbers.add(number);
    }
    
    public Contact ( String firstName, String lastName, String number ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numbers.add(number);
    }
    
    public Contact ( String firstName, String lastName, String number, String email ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numbers.add(number);
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumbers(String number) {
        this.numbers.add(number);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
