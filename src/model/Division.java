package model;

public class Division {

    private int divisionId;
    private String division;
    private int countryId;

    public Division(int divisionId, String division, int countryId) {
        this.setDivisionId(divisionId);
        this.setDivision(division);
        this.setCountryId(countryId);
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
