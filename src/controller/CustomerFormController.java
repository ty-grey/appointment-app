package controller;

import database.DBCountry;
import database.DBCustomer;
import database.DBDivision;
import database.DBUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Country;
import model.Customer;
import model.Division;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    private final ControllerUtil controllerUtil = new ControllerUtil();
    private ObservableList<Customer> allCustomer = FXCollections.observableArrayList();
    private ObservableList<Division> allDivision = FXCollections.observableArrayList();
    private ObservableList<Country> allCountry = FXCollections.observableArrayList();
    private final ObservableList<String> countryNames = FXCollections.observableArrayList();
    private Customer selectedCustomer;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<?, ?> customerID;

    @FXML
    private TableColumn<?, ?> customerName;

    @FXML
    private TableColumn<?, ?> customerAddress;

    @FXML
    private TableColumn<?, ?> customerZIP;

    @FXML
    private TableColumn<?, ?> customerPhone;

    @FXML
    private TableColumn<?, ?> customerCountry;

    @FXML
    private TableColumn<?, ?> customerFirstLevelDiv;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField zipField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> countryCombo;

    @FXML
    private ComboBox<String> firstLevelDivCombo;

    @FXML
    void contactChangeHandler() {
    }

    @FXML
    void endDateChooser() {
    }

    @FXML
    void startDateChooser() {
    }

    @FXML
    void countryChangeHandler() {
        populateFirstLevelCombo(countryCombo.getValue());
    }

    @FXML
    void addHandler() {
        if (inputVerification()) {
            try {
                Customer temp = fieldsToCustomer(-1);
                DBCustomer.createCustomer(temp, DBUser.getUser());
            } catch (Exception e) {
                controllerUtil.errorPopup("Error", "Bad data in text fields!",
                        "Pleasure ensure all text fields are filled with correct data!");
                return;
            }

            refreshTable();
            clearFields();
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
            DBCustomer.deleteCustomer(selectedCustomer.getId());
        } catch (Exception e) {
            controllerUtil.errorPopup("Error", "Nothing was selected!",
                    "Please select something before attempting a deletion!");
            return;
        }
        controllerUtil.infoPopup("Information", "Customer has been deleted!",
                "ID: " + selectedCustomer.getId() + "\nName: " + selectedCustomer.getName());
        refreshTable();
    }

    @FXML
    void modifyHandler() {
        if (inputVerification()) {
            try {
                Customer temp = fieldsToCustomer(selectedCustomer.getId());
                DBCustomer.modifyCustomer(temp, DBUser.getUser());
            } catch (Exception e) {
                controllerUtil.errorPopup("Error", "Bad data in text fields!",
                        "Pleasure ensure all text fields are filled with correct data!");
                return;
            }

            refreshTable();
            clearFields();
        }
    }

    @FXML
    void selectHandler() {
        try {
            getSelectedFromTable();
        } catch (Exception e) {
            controllerUtil.errorPopup("Error", "Nothing was selected!",
                    "Please select something!");
            return;
        }

        fillFields();
    }

    private boolean inputVerification() {
        if (checkForEmptyFields()) {
            controllerUtil.errorPopup("Error", "Bad data in text fields!",
                    "Pleasure ensure all text fields are filled!");
            return false;
        }
        return true;
    }

    private void clearFields() {
        selectedCustomer = null;
        idField.setText("Disabled - Auto Gen ID");
        nameField.clear();
        addressField.clear();
        zipField.clear();
        phoneField.clear();
        countryCombo.getSelectionModel().clearSelection();
        firstLevelDivCombo.getItems().clear();
        customerTable.getSelectionModel().clearSelection();
    }

    private void fillFields() {
        idField.setText(selectedCustomer.getId() + " - Auto Gen if Adding a Customer");
        nameField.setText(selectedCustomer.getName());
        addressField.setText(selectedCustomer.getAddress());
        zipField.setText(selectedCustomer.getZip());
        phoneField.setText(selectedCustomer.getPhone());
        populateCountryCombo();
        populateFirstLevelCombo(selectedCustomer.getCountryName());
        countryCombo.setValue(selectedCustomer.getCountryName());
        firstLevelDivCombo.setValue(selectedCustomer.getDivisionName());
    }

    private void getSelectedFromTable() {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
    }

    private void refreshTable() {
        databasePopulateLists();
        customerTable.setItems(allCustomer);
    }

    private void populateFirstLevelCombo(String country) {
        // Prevent any null pointer exception errors
        if (country == null) {
            return;
        }

        int countryId = searchAllCountry(country);
        ObservableList<String> divisionNames = FXCollections.observableArrayList();

        allDivision.forEach((division) -> {
            if (countryId == division.getCountryId()) {
                divisionNames.add(division.getDivision());
            }
        });

        firstLevelDivCombo.setItems(divisionNames);
    }

    private Customer fieldsToCustomer(int id) {
        return new Customer(
                id,
                nameField.getText().trim(),
                addressField.getText().trim(),
                zipField.getText().trim(),
                phoneField.getText().trim(),
                searchAllDivision(firstLevelDivCombo.getValue().trim()),
                searchAllCountry(countryCombo.getValue().trim()),
                countryCombo.getValue().trim(),
                firstLevelDivCombo.getValue().trim()
        );
    }

    private boolean checkForEmptyFields() {
        return nameField.getText().isEmpty() ||
                addressField.getText().isEmpty() ||
                zipField.getText().isEmpty() ||
                phoneField.getText().isEmpty();
    }

    private int searchAllCountry(String country) {
        for (Country value : allCountry) {
            if (country.equals(value.getCountry())) {
                return value.getCountryId();
            }
        }
        return -1;
    }

    private int searchAllDivision(String division) {
        for (Division value : allDivision) {
            if (division.equals(value.getDivision())) {
                return value.getDivisionId();
            }
        }
        return -1;
    }

    private void databasePopulateLists() {
        allDivision = DBDivision.getAllDivisions();
        allCustomer = DBCustomer.getAllCustomers();
        allCountry = DBCountry.getAllCountries();
    }

    private void initTable() {
        customerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerZIP.setCellValueFactory(new PropertyValueFactory<>("zip"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        customerFirstLevelDiv.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        customerTable.setItems(allCustomer);
    }

    private void populateCountryCombo() {
        countryCombo.setItems(countryNames);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databasePopulateLists();
        initTable();
        allCountry.forEach((country) -> countryNames.add(country.getCountry()));
        populateCountryCombo();
        clearFields();
    }
}
