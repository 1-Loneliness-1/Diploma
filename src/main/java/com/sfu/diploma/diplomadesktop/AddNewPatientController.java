package com.sfu.diploma.diplomadesktop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddNewPatientController implements Initializable {

    @FXML
    public TextField patientIDTextField;
    public TextField patientSurnameTextField;
    public TextField patientNameTextField;
    public TextField patientMidnameTextField;
    public RadioButton maleGenderRB;
    public RadioButton femaleGenderRB;
    public TextField passportTextField;
    public TextField polisTextField;
    public TextField birthdayTextField;
    public TextField addressTextField;
    public TextField workplaceTextField;
    public TextField disabilityTextField;
    public TextField phoneTextField;
    public ToggleGroup group = new ToggleGroup();

    public AddNewPatientController() {}

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        maleGenderRB.setToggleGroup(group);
        femaleGenderRB.setToggleGroup(group);
        group.selectToggle(maleGenderRB);
    }

    public void regPatientInDataBase() {
        if (patientIDTextField.getText().isEmpty() || patientSurnameTextField.getText().isEmpty() ||
                patientNameTextField.getText().isEmpty() || passportTextField.getText().isEmpty() ||
                phoneTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Пожалуйста, заполните все поля!");
            alert.showAndWait();
        }
        else {
            try {
                JDBCManager jdbcManager = new JDBCManager("andro", "andro");
                Class.forName("com.mysql.jdbc.Driver");
                Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                        jdbcManager.getPasswordOfUserInDataBase());
                String query = "INSERT patients (id, patient_surname, patient_name, patient_midname, gender, num_of_passport, num_of_polis, address, birthday, workplace, disability, phone) " +
                        "VALUES ('" + patientIDTextField.getText() + "', '" + patientSurnameTextField.getText() + "', '" + patientNameTextField.getText() +
                        "', '" + patientMidnameTextField.getText() + "', '" + ((RadioButton) group.getSelectedToggle()).getText() + "', '"
                        + passportTextField.getText() + "', '" + polisTextField.getText() + "', '" + addressTextField.getText() + "', '" +
                        birthdayTextField.getText() + "', '" + workplaceTextField.getText() + "', '" + disabilityTextField.getText() + "', '" +
                        phoneTextField.getText() + "'" + ");";
                Statement statement = connect.createStatement();
                statement.executeUpdate(query);

                connect.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Пациент успешно зарегистрирован.");
                alert.showAndWait();
            } catch (Exception ex) {
                System.out.println("Connection failure...");
                System.out.println("Error: " + ex);
            }
        }
        Stage stg = (Stage) patientIDTextField.getScene().getWindow();
        stg.close();
    }
}
