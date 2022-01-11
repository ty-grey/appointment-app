package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;
import util.TimeConverter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class DBAppointment {

    public static boolean deleteAppointment(int id) {
        try {
            String sqlDeleteAppointment = "DELETE FROM appointments WHERE Appointment_ID=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlDeleteAppointment);

            stm.setInt(1, id);

            stm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Deletion of appointment failed!");
        }
        return false;
    }

    public static boolean modifyAppointment(Appointment appointment, User currentUser) {
        try {
            String sqlModifyAppointment = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, " +
                    "End=?, Last_Updated_By=?, Last_Update=NOW(), Customer_ID=?, User_ID=?, Contact_ID=? " +
                    "WHERE Appointment_ID=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlModifyAppointment);

            stm.setString(1, appointment.getTitle());
            stm.setString(2, appointment.getDescription());
            stm.setString(3, appointment.getLocation());
            stm.setString(4, appointment.getType());
            stm.setTimestamp(5, Timestamp.valueOf(appointment.getStartTime()));
            stm.setTimestamp(6, Timestamp.valueOf(appointment.getEndTime()));
            stm.setString(7, currentUser.getUsername());
            stm.setInt(8, appointment.getCustomerId());
            stm.setInt(9, appointment.getUserId());
            stm.setInt(10, appointment.getContactId());
            stm.setInt(11, appointment.getId());

            stm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Modification of appointment failed!");
        }
        return false;
    }

    public static boolean createAppointment(Appointment appointment, User currentUser) {
        try {
            String sqlCreateAppointment = "INSERT INTO appointments SET Title=?, Description=?, Location=?, Type=?, " +
                    "Start=?, End=?, Last_Updated_By=?, Created_By=?, Customer_ID=?, User_ID=?, Contact_ID=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlCreateAppointment);

            stm.setString(1, appointment.getTitle());
            stm.setString(2, appointment.getDescription());
            stm.setString(3, appointment.getLocation());
            stm.setString(4, appointment.getType());
            stm.setTimestamp(5, Timestamp.valueOf(appointment.getStartTime()));
            stm.setTimestamp(6, Timestamp.valueOf(appointment.getEndTime()));
            stm.setString(7, currentUser.getUsername());
            stm.setString(8, currentUser.getUsername());
            stm.setInt(9, appointment.getCustomerId());
            stm.setInt(10, appointment.getUserId());
            stm.setInt(11, appointment.getContactId());

            stm.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Creation of appointment failed!");
        }
        return false;
    }

    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList<Appointment> all = FXCollections.observableArrayList();
        try {
            String sqlGetAllAppointments = "SELECT a.Appointment_ID, a.Customer_ID, a.Start, a.End, a.Title, " +
                    "a.Description, a.Location, a.Type, a.Contact_ID, a.User_ID, c.Contact_Name FROM " +
                    "appointments a JOIN contacts c ON a.Contact_ID=c.Contact_ID";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetAllAppointments);
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                Appointment appointment = resultToAppointment(result);
                appointment.setContactName(result.getString("Contact_Name"));
                all.add(appointment);
            }

            return all;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting all appointments failed!");
        }
        return all;
    }

    public static ObservableList<Appointment> getUpcomingAppointment() {
        ObservableList<Appointment> all = FXCollections.observableArrayList();
        try {
            String sqlGetUpcomingAppointment = "SELECT * FROM appointments WHERE start BETWEEN NOW() AND " +
                    "DATE_ADD(NOW(), INTERVAL 15 MINUTE)";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetUpcomingAppointment);
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                all.add(resultToAppointment(result));
            }

            return all;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting appointment in next 15min failed!");
        }
        return all;
    }

    public static boolean checkOverlap(Appointment appointment) {
        try {
            // Complicated SQL statement to check for all edge cases along with overlap
            String sqlCheckOverlap = "SELECT * FROM appointments WHERE (Appointment_ID<>? AND Customer_ID=?) AND " +
                    "(? BETWEEN Start AND End OR ? BETWEEN Start AND End OR ?>End AND ?<Start) " +
                    "AND (?<>Start AND ?<>End)";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlCheckOverlap);

            Timestamp start = Timestamp.valueOf(appointment.getStartTime());
            Timestamp end = Timestamp.valueOf(appointment.getEndTime());

            stm.setInt(1, appointment.getId());
            stm.setInt(2, appointment.getCustomerId());
            stm.setTimestamp(3, start);
            stm.setTimestamp(4, end);
            stm.setTimestamp(5, end);
            stm.setTimestamp(6, start);
            stm.setTimestamp(7, end);
            stm.setTimestamp(8, start);

            ResultSet result = stm.executeQuery();

            return result.next();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting appointment overlaps failed!");
        }
        return true;
    }

    public static ObservableList<String> getAllTypes() {
        ObservableList<String> all = FXCollections.observableArrayList();
        try {
            String sqlGetAllTypes = "SELECT DISTINCT Type FROM appointments";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetAllTypes);
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                all.add(result.getString("Type"));
            }

            return all;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting all types failed!");
        }
        return all;
    }

    public static int getTypeCountByMonth(String type, String month) {
        try {
            String sqlGetTypeCountByMonth = "SELECT COUNT(Type) AS count FROM appointments WHERE Type=? " +
                    "AND MONTHNAME(Start)=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlGetTypeCountByMonth);

            stm.setString(1, type);
            stm.setString(2, month);

            ResultSet result = stm.executeQuery();

            if (result.next()) {
                return result.getInt("count");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Getting types by month failed!");
        }
        return -1;
    }

    private static Appointment resultToAppointment(ResultSet result) {
        try {
            return new Appointment(
                    result.getInt("Appointment_ID"),
                    result.getInt("Customer_ID"),
                    TimeConverter.timestampToTime(result.getTimestamp("Start")),
                    TimeConverter.timestampToTime(result.getTimestamp("End")),
                    result.getString("Title"),
                    result.getString("Description"),
                    result.getString("Location"),
                    result.getString("Type"),
                    result.getInt("Contact_ID"),
                    result.getInt("User_ID")
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Conversion of result to appointment failed!");
        }
        return null;
    }
}
