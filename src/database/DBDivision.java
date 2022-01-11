package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBDivision {

    public static ObservableList<Division> getAllDivisions() {
        ObservableList<Division> all = FXCollections.observableArrayList();
        try {
            String sqlGetAllDivisions = "SELECT Division_ID, Division, Country_ID FROM first_level_divisions";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetAllDivisions);
            ResultSet result = stm.executeQuery();

            // Add all of the results to a format that is easily display-able to the GUI
            while (result.next()) {
                all.add(resultToDivision(result));
            }

            return all;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting all divisions failed!");
        }
        return all;
    }

    private static Division resultToDivision(ResultSet result) {
        try {
            return new Division(
                    result.getInt("Division_ID"),
                    result.getString("Division"),
                    result.getInt("Country_ID")
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Conversion of result to division failed!");
        }
        return null;
    }
}
