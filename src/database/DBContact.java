package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBContact {

    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> all = FXCollections.observableArrayList();
        try {
            String sqlGetAllContacts = "SELECT Contact_ID, Contact_Name FROM contacts";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetAllContacts);
            ResultSet result = stm.executeQuery();

            // Add all of the results to a format that is easily display-able to the GUI
            while (result.next()) {
                all.add(resultToContact(result));
            }

            return all;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting all contacts failed!");
        }
        return all;
    }

    private static Contact resultToContact(ResultSet result) {
        try {
            return new Contact(
                    result.getInt("Contact_ID"),
                    result.getString("Contact_Name")
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Conversion of result to contact failed!");
        }
        return null;
    }
}
