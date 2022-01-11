package model;

public class Country {

    private int countryId;
    private String country;

    public Country(int countryId, String country) {
        this.setCountryId(countryId);
        this.setCountry(country);
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
