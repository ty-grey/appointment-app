package model;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String zip;
    private String phone;
    private int divisionId;
    private int countryId;
    private String divisionName;
    private String countryName;

    public Customer(int id, String name, String address, String zip, String phone, int divisionId, int countryId, String divisionName, String countryName) {
        this.setId(id);
        this.setName(name);
        this.setAddress(address);
        this.setZip(zip);
        this.setPhone(phone);
        this.setDivisionId(divisionId);
        this.setCountryId(countryId);
        this.setDivisionName(divisionName);
        this.setCountryName(countryName);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public String getCountryName() {
        return countryName;
    }
}
