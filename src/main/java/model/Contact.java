package model;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    private Integer contactId;
    private String firstName;
    private String lastName;
    private String email;
    private List<Group> groups = new ArrayList<>();
    private List<Phone> phones = new ArrayList<>();

    public Contact(Integer contactId, String firstName, String lastName, String email) {
        this.contactId = contactId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Contact(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> addGroup(Group group) {
        this.groups.add(group);
        return this.groups;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<Phone> addPhone(Phone phone) {
        this.phones.add(phone);
        return this.phones;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", groups=" + groups +
                ", phones=" + phones +
                '}';
    }
}
