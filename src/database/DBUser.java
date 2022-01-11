package database;

import model.User;
import util.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DBUser {

    private static User loggedUser;

    public static boolean checkLogin(String username, String password) {
        try {
            String sqlCheckLogin = "SELECT * FROM users WHERE User_Name=? AND Password=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlCheckLogin);

            stm.setString(1, username);
            stm.setString(2, password);

            ResultSet result = stm.executeQuery();

            // Make sure to log any login attempts through the logger!
            if (result.next()) {
                loggedUser = new User(result.getInt("User_ID"), username);
                Logger.loginLogger(username, true);
                return true;
            } else {
                Logger.loginLogger(username, false);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Login check has failed!");
        }
        return false;
    }

    public static boolean userExist(int userId) {
        try {
            String sqlUserExist = "SELECT * FROM users WHERE User_ID=?";
            PreparedStatement stm = DBConnection.getConnection().prepareStatement(sqlUserExist);

            stm.setInt(1, userId);

            ResultSet result = stm.executeQuery();

            return result.next();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("User exist check has failed!");
        }
        return false;
    }

    public static User getUser() {
        return loggedUser;
    }

    public static void userLogout() {
        loggedUser = null;
    }
}
