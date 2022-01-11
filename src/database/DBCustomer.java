package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBCustomer {

    public static boolean deleteCustomer(int id) {
        try {
            // Delete any appointments where Customer_ID is referenced to avoid any potential errors
            String sqlDeleteCustomer = "DELETE FROM appointments WHERE Customer_ID=?;" +
                    "DELETE FROM customers WHERE Customer_ID=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlDeleteCustomer);

            stm.setInt(1, id);
            stm.setInt(2, id);

            stm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Deletion of customer failed!");
        }
        return false;
    }

    public static boolean modifyCustomer(Customer customer, User currentUser) {
        try {
            String sqlModifyCustomer = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, " +
                    "Last_Updated_By=?, Last_Update=NOW(), Division_ID=? WHERE Customer_ID=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlModifyCustomer);

            stm.setString(1, customer.getName());
            stm.setString(2, customer.getAddress());
            stm.setString(3, customer.getZip());
            stm.setString(4, customer.getPhone());
            stm.setString(5, currentUser.getUsername());
            stm.setInt(6, customer.getDivisionId());
            stm.setInt(7, customer.getId());

            stm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Modification of customer failed!");
        }
        return false;
    }

    public static boolean createCustomer(Customer customer, User currentUser) {
        try {
            String sqlCreateCustomer = "INSERT INTO customers SET Customer_Name=?, Address=?, Postal_Code=?, " +
                    "Phone=?, Created_By=?, Last_Updated_By=?, Division_ID=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlCreateCustomer);

            stm.setString(1, customer.getName());
            stm.setString(2, customer.getAddress());
            stm.setString(3, customer.getZip());
            stm.setString(4, customer.getPhone());
            stm.setString(5, currentUser.getUsername());
            stm.setString(6, currentUser.getUsername());
            stm.setInt(7, customer.getDivisionId());

            stm.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Creation of customer failed!");
        }
        return false;
    }

    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> all = FXCollections.observableArrayList();
        try {
            String sqlGetAllCustomers = "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, " +
                    "d.Division_ID, d.Division, o.Country_ID, o.Country FROM customers c JOIN first_level_divisions d " +
                    "ON c.Division_ID=d.Division_ID JOIN countries o ON d.Country_ID=o.Country_ID";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetAllCustomers);
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                all.add(resultToCustomer(result));
            }

            return all;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting all customers failed!");
        }
        return all;
    }

    public static ObservableList<String> getAllPostalCodes() {
        ObservableList<String> all = FXCollections.observableArrayList();
        try {
            String sqlGetAllPostalCodes = "SELECT DISTINCT Postal_Code FROM customers";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetAllPostalCodes);
            ResultSet result = stm.executeQuery();

            // Add all of the results to a format that is easily display-able to the GUI
            while (result.next()) {
                all.add(result.getString("Postal_Code"));
            }

            return all;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting all postal codes failed!");
        }
        return all;
    }

    public static int getPostalCodeCount(String postalCode) {
        try {
            String sqlGetPostalCodeCount = "SELECT COUNT(Postal_Code) AS count FROM customers WHERE Postal_Code=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetPostalCodeCount);

            stm.setString(1, postalCode);

            ResultSet result = stm.executeQuery();

            if (result.next()) {
                return result.getInt("count");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting postal count failed!");
        }
        return -1;
    }

    private static Customer resultToCustomer(ResultSet result) {
        try {
            return new Customer(
                    result.getInt("Customer_ID"),
                    result.getString("Customer_Name"),
                    result.getString("Address"),
                    result.getString("Postal_Code"),
                    result.getString("Phone"),
                    result.getInt("Division_ID"),
                    result.getInt("Country_ID"),
                    result.getString("Division"),
                    result.getString("Country")
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Conversion of result to customer failed!");
        }
        return null;
    }
}
