package database;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class DBConnection {

    // allowMultiQueries needs to be set to true for some queries to work
    private static final String url = "jdbc:mysql://localhost/client_schedule?allowMultiQueries=true";
    private static final String username = "sqlUser";
    private static final String password = "passw0rd!";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static Connection connection;

    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection succeeded!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
        }
    }


    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Database connection closure succeeded!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database connection closure failed!");
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
