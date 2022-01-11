package model;

public class Contact {

    private int contactId;
    private String contactName;

    public Contact(int contactId, String contactName) {
        this.setContactId(contactId);
        this.setContactName(contactName);
    }

    public int getContactId() {
        return contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
