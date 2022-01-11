package model;

import java.time.LocalDateTime;

public class Appointment {

    private int id;
    private int customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String description;
    private String location;
    private String type;
    private int contactId;
    private int userId;
    private String contactName;
    private String zoneStartTime;
    private String zoneEndTime;

    public Appointment(int id, int customerId, LocalDateTime startTime, LocalDateTime endTime, String title, String description,
                       String location, String type, int contactId, int userId) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setTitle(title);
        this.setDescription(description);
        this.setLocation(location);
        this.setType(type);
        this.setContactId(contactId);
        this.setUserId(userId);
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getContactName() {
        return contactName;
    }

    public String getZoneStartTime() {
        return zoneStartTime;
    }

    public String getZoneEndTime() {
        return zoneEndTime;
    }

    public int getContactId() {
        return contactId;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setZoneStartTime(String zoneStartTime) {
        this.zoneStartTime = zoneStartTime;
    }

    public void setZoneEndTime(String zoneEndTime) {
        this.zoneEndTime = zoneEndTime;
    }
}
