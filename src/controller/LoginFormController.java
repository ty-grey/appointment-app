package controller;

import database.DBAppointment;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import database.DBUser;
import model.Appointment;
import util.TimeConverter;

import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    private final ControllerUtil controllerUtil = new ControllerUtil();
    private ResourceBundle resourceBundle;

    @FXML
    private Label loginLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label languageLabel;

    @FXML
    void exitHandler() {
        if(controllerUtil.confirmPopup(resourceBundle.getString("confirmation"),
                resourceBundle.getString("confirmationExit"), "")) {
            Platform.exit();
        }
    }

    @FXML
    void loginHandler(ActionEvent event) {
        if(DBUser.checkLogin(usernameField.getText().trim(), passwordField.getText().trim())) {
            showUpcoming();
            controllerUtil.quickChangeScreen(event,"../view/MainForm.fxml");
        } else {
            controllerUtil.errorPopup(resourceBundle.getString("error"),
                    resourceBundle.getString("errorType"),
                    resourceBundle.getString("errorDescription"));
        }
    }

    private void changeUILang() {
        loginLabel.setText(resourceBundle.getString("login"));
        loginButton.setText(resourceBundle.getString("login"));
        usernameLabel.setText(resourceBundle.getString("username"));
        usernameField.setPromptText(resourceBundle.getString("username"));
        passwordLabel.setText(resourceBundle.getString("password"));
        passwordField.setPromptText(resourceBundle.getString("password"));
        exitButton.setText(resourceBundle.getString("exit"));
        languageLabel.setText("Timezone: " + ZoneId.systemDefault());
    }

    private void showUpcoming() {
        ObservableList<Appointment> upcoming = DBAppointment.getUpcomingAppointment();
        if (upcoming.size() == 0) {
            controllerUtil.infoPopup("Confirmation", "You have no upcoming appointments!", "");
            return;
        }

        for (Appointment appointment : upcoming) {
            controllerUtil.infoPopup("Confirmation",
                    "You have an upcoming appointment!",
                    "Details: \nID: " + appointment.getId() +
                            "\nName: " + appointment.getTitle() +
                            "\nStart: " + TimeConverter.formatTimeToZone(appointment.getStartTime()) +
                            "\nEnd: " + TimeConverter.formatTimeToZone(appointment.getEndTime()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TEST: Other language support
        //Locale.setDefault(new Locale("fr", "FR"));

        try {
            this.resourceBundle=ResourceBundle.getBundle("resources.translations.login", Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Login translation files could not be found.");
        }
        changeUILang();
    }
}
