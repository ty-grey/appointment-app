package controller;

import database.DBUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    private final ControllerUtil controllerUtil = new ControllerUtil();

    @FXML
    void appointmentHandler(ActionEvent event) {
        controllerUtil.quickChangeScreen(event,"../view/AppointmentForm.fxml");
    }

    @FXML
    void customerHandler(ActionEvent event) {
        controllerUtil.quickChangeScreen(event,"../view/CustomerForm.fxml");
    }

    @FXML
    void logoutHandler(ActionEvent event) {
        if(controllerUtil.confirmPopup("Confirmation", "Logout", "Are you sure you want to logout?")) {
            DBUser.userLogout();
            controllerUtil.quickChangeScreen(event,"../view/LoginForm.fxml");
        }
    }

    @FXML
    void reportHandler(ActionEvent event) {
        controllerUtil.quickChangeScreen(event,"../view/ReportForm.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
