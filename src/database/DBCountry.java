package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBCountry {

    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> all = FXCollections.observableArrayList();
        try {
            String sqlGetAllCountries = "SELECT Country_ID, Country FROM countries";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetAllCountries);
            ResultSet result = stm.executeQuery();

            // Add all of the results to a format that is easily display-able to the GUI
            while (result.next()) {
                all.add(resultToContact(result));
            }

            return all;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting all countries failed!");
        }
        return all;
    }

    private static Country resultToContact(ResultSet result) {
        try {
            return new Country(
                    result.getInt("Country_ID"),
                    result.getString("Country")
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Conversion of result to country failed!");
        }
        return null;
    }
}
