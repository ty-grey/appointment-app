package controller;

import database.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Contact;
import model.Customer;
import util.TimeConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AppointmentFormController implements Initializable {

    private final ControllerUtil controllerUtil = new ControllerUtil();
    private ObservableList<Contact> allContact = FXCollections.observableArrayList();
    private ObservableList<Appointment> allAppointment = FXCollections.observableArrayList();
    private ObservableList<Customer> allCustomer = FXCollections.observableArrayList();
    private Appointment selectedAppointment;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<?, ?> appId;

    @FXML
    private TableColumn<?, ?> appCustomerId;

    @FXML
    private TableColumn<?, ?> appUserId;

    @FXML
    private TableColumn<?, ?> appTitle;

    @FXML
    private TableColumn<?, ?> appDescription;

    @FXML
    private TableColumn<?, ?> appLocation;

    @FXML
    private TableColumn<?, ?> appContact;

    @FXML
    private TableColumn<?, ?> appType;

    @FXML
    private TableColumn<?, ?> appStart;

    @FXML
    private TableColumn<?, ?> appEnd;

    @FXML
    private TextField idField;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField userIdField;

    @FXML
    private ComboBox<String> contactCombo;

    @FXML
    private TextField typeField;

    @FXML
    private DatePicker startDateField;

    @FXML
    private TextField startTimeField;

    @FXML
    private DatePicker endDateField;

    @FXML
    private TextField endTimeField;

    @FXML
    private RadioButton weekFilter;

    @FXML
    private ToggleGroup filter;

    @FXML
    private RadioButton monthFilter;

    @FXML
    private RadioButton allFilter;

    @FXML
    void weekTableFilterHandler() {
        ObservableList<Appointment> weekList = FXCollections.observableArrayList();

        for (Appointment appointment : allAppointment) {
            if (appointment.getStartTime().isAfter(LocalDateTime.now()) &&
                    appointment.getEndTime().isBefore(LocalDateTime.now().plusWeeks(1))) {
                weekList.add(appointment);
            }
        }

        appointmentTable.setItems(weekList);
    }

    @FXML
    void monthTableFilterHandler() {
        ObservableList<Appointment> monthList = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();

        for (Appointment appointment : allAppointment) {
            if (appointment.getStartTime().isAfter(now) &&
                    appointment.getEndTime().isBefore(now.plusMonths(1))) {
                monthList.add(appointment);
            }
        }

        appointmentTable.setItems(monthList);
    }

    @FXML
    void allTableFilterHandler() {
        appointmentTable.setItems(allAppointment);
    }

    @FXML
    void addHandler() {
        Appointment newAppointment = fieldsToAppointment(-1);
        if (inputVerification(newAppointment)) {
            DBAppointment.createAppointment(newAppointment, DBUser.getUser());
            refreshTable();
        }
    }

    @FXML
    void modifyHandler() {
        Appointment newAppointment = fieldsToAppointment(selectedAppointment.getId());
        if (inputVerification(newAppointment)) {
            DBAppointment.modifyAppointment(newAppointment, DBUser.getUser());
            refreshTable();
        }
    }

    @FXML
    void cancelHandler(ActionEvent event) {
        controllerUtil.quickChangeScreen(event, "../view/MainForm.fxml");
    }

    @FXML
    void clearSelectionHandler() {
        clearFields();
    }

    @FXML
    void deleteHandler() {
        try {
            getSelectedFromTable();
            DBAppointment.deleteAppointment(selectedAppointment.getId());
        } catch (Exception e) {
            controllerUtil.errorPopup("Error", "Nothing was selected!",
                    "Please select something before attempting a deletion!");
            return;
        }

        controllerUtil.infoPopup("Information", "Appointment has been cancelled!",
                "ID: " + selectedAppointment.getId() + "\nType: " + selectedAppointment.getType());
        refreshTable();
    }

    @FXML
    void selectHandler() {
        try {
            getSelectedFromTable();
            fillFields();
        } catch (Exception e) {
            controllerUtil.errorPopup("Error", "Nothing was selected!",
                    "Please select something!");
        }
    }

    private boolean inputVerification(Appointment appointment) {
        try {
            if (checkForEmptyFields()) {
                controllerUtil.errorPopup("Error", "Bad data in text fields!",
                        "Pleasure ensure all text fields are filled!");
            } else if (!searchAllCustomers(appointment.getCustomerId())) {
                controllerUtil.errorPopup("Error", "There is customer issue!",
                        "The customer id you are trying to reference does not exist!");
            } else if (!timeChecks(appointment)) {
                controllerUtil.errorPopup("Error", "There is a time issue!",
                        "The appointment you are trying to modify is outside of the typical working hours or has the start scheduled before the end!");
            } else if (DBAppointment.checkOverlap(appointment)) {
                controllerUtil.errorPopup("Error", "There is a scheduling issue!",
                        "You have an appointment scheduled at the same time as you are trying to enter!");
            } else if (!DBUser.userExist(appointment.getUserId())) {
                controllerUtil.errorPopup("Error", "There is an input issue!",
                        "The user ID you are trying to reference does not exist!");
            } else {
                return true;
            }

        } catch (Exception e) {
            controllerUtil.errorPopup("Error", "Bad data in text fields!",
                    "Pleasure ensure all text fields are filled with correct data!\nA common error is not having a leading zero in front of the time!");
        }
        return false;
    }

    private Appointment fieldsToAppointment(int id) {
        return new Appointment(
                id,
                Integer.parseInt(customerIdField.getText().trim()),
                TimeConverter.formatTimeToUTC(LocalDateTime.of(startDateField.getValue(), LocalTime.parse(startTimeField.getText()))),
                TimeConverter.formatTimeToUTC(LocalDateTime.of(endDateField.getValue(), LocalTime.parse(endTimeField.getText()))),
                titleField.getText().trim(),
                descriptionField.getText().trim(),
                locationField.getText().trim(),
                typeField.getText().trim(),
                searchAllContacts(contactCombo.getValue()),
                Integer.parseInt(userIdField.getText().trim())
        );
    }

    private boolean timeChecks(Appointment appointment) {
        LocalDate startDay = appointment.getStartTime().toLocalDate();
        LocalDateTime workStart = LocalDateTime.of(startDay, LocalTime.parse("11:59:59"));
        LocalDateTime workEnd = workStart.plusHours(14).plusSeconds(2);

        return appointment.getStartTime().isBefore(appointment.getEndTime()) &&
                appointment.getStartTime().isAfter(workStart) &&
                appointment.getEndTime().isBefore(workEnd);
    }

    private int searchAllContacts(String name) {
        for (Contact contact : allContact) {
            if (name.equals(contact.getContactName())) {
                return contact.getContactId();
            }
        }
        return -1;
    }

    private boolean searchAllCustomers(int id) {
        for (Customer customer : allCustomer) {
            if (customer.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void fillFields() {
        LocalDateTime start = TimeConverter.formatTimeToZone(selectedAppointment.getStartTime());
        LocalDateTime end = TimeConverter.formatTimeToZone(selectedAppointment.getEndTime());

        idField.setText(selectedAppointment.getId() + " - Auto Gen if Adding an App.");
        customerIdField.setText(Integer.toString(selectedAppointment.getCustomerId()));
        userIdField.setText(Integer.toString(selectedAppointment.getUserId()));
        titleField.setText(selectedAppointment.getTitle());
        descriptionField.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        contactCombo.setValue(selectedAppointment.getContactName());
        typeField.setText(selectedAppointment.getType());
        startTimeField.setText(start.toLocalTime().toString());
        endTimeField.setText(end.toLocalTime().toString());
        startDateField.setValue(start.toLocalDate());
        endDateField.setValue(end.toLocalDate());
    }

    private boolean checkForEmptyFields() {
        return customerIdField.getText().isEmpty() ||
                userIdField.getText().isEmpty() ||
                titleField.getText().isEmpty() ||
                descriptionField.getText().isEmpty() ||
                locationField.getText().isEmpty() ||
                typeField.getText().isEmpty();
    }

    private void clearFields() {
        selectedAppointment = null;
        idField.setText("Disabled - Auto Gen ID");
        customerIdField.clear();
        userIdField.clear();
        titleField.clear();
        descriptionField.clear();
        locationField.clear();
        contactCombo.getSelectionModel().clearSelection();
        typeField.clear();
        startTimeField.clear();
        endTimeField.clear();
        startDateField.setValue(null);
        endDateField.setValue(null);
        allFilter.setSelected(true);
        appointmentTable.setItems(allAppointment);
    }

    private void refreshTable() {
        databasePopulateLists();
        timeConversion();
        allFilter.setSelected(true);
        appointmentTable.setItems(allAppointment);
    }

    private void getSelectedFromTable() {
        selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
    }

    private void databasePopulateLists() {
        allAppointment = DBAppointment.getAllAppointments();
        allContact = DBContact.getAllContacts();
        allCustomer = DBCustomer.getAllCustomers();
    }

    private void timeConversion() {
        Appointment temp;
        for (Appointment appointment : allAppointment) {
            temp = appointment;
            appointment.setZoneStartTime(TimeConverter.formatTimeToTable(TimeConverter.formatTimeToZone(temp.getStartTime())));
            appointment.setZoneEndTime(TimeConverter.formatTimeToTable(TimeConverter.formatTimeToZone(temp.getEndTime())));
        }
    }

    private void initTable() {
        appId.setCellValueFactory(new PropertyValueFactory<>("id"));
        appCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        appTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appStart.setCellValueFactory(new PropertyValueFactory<>("zoneStartTime"));
        appEnd.setCellValueFactory(new PropertyValueFactory<>("zoneEndTime"));
        allFilter.setSelected(true);

        appointmentTable.setItems(allAppointment);
    }

    private void initContactCombo() {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        for (Contact contact : allContact) {
            contactNames.add(contact.getContactName());
        }
        contactCombo.setItems(contactNames);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databasePopulateLists();
        timeConversion();
        initTable();
        initContactCombo();
    }
}
