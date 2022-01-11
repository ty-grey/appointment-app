package util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Instant;

public class Logger {

    public static void loginLogger(String username, boolean valid) {
        try {
            PrintWriter printer = new PrintWriter(new FileWriter("login_activity.txt",true));
            printer.printf("%s %s %s%n", username, valid, Instant.now().toString());
            printer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Logger has run into an error!");
        }
    }
}
