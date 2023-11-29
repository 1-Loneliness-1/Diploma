package com.sfu.diploma.diplomadesktop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public LoginController() {
    }
    @FXML
    public Label workerIDLabel;
    public Label surnameLabel;
    public Label nameLabel;
    public Label midNameLabel;
    public Label positionLabel;
    public ImageView userAvatar;
    public Button scheduleButton;
    public Button pharmacyButton;
    public Button newPatientRegButton;
    public Button patientMedInfoButton;
    public Button addNewUserButton;
    public Button registrationPatientButton;
    public Button patientButtonForDoctors;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String[] parseFileResult = General.parseFile();
        workerIDLabel.setText(parseFileResult[0]);
        surnameLabel.setText(parseFileResult[1]);
        nameLabel.setText(parseFileResult[2]);
        midNameLabel.setText(parseFileResult[3]);
        positionLabel.setText(parseFileResult[4]);
        Image image = new Image(getClass().getResourceAsStream(parseFileResult[5].toString()));
        userAvatar.setImage(image);
        checkRoleOfCurrentUser();
    }

    //Function for schedule button
    public void getSchedule() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("schedule.fxml"));
            Parent rootSchedulePage = (Parent) loader.load();
            Stage stage = (Stage) workerIDLabel.getScene().getWindow();
            stage.close();

            stage = new Stage();

            stage.setScene(new Scene(rootSchedulePage, 1366, 710));
            stage.setMaximized(true);
            stage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
            stage.setTitle("Расписание");
            stage.show();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    //Fun for pharmacy button
    public void getPharmacyInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pharmacy.fxml"));
            Parent rootPharmacyPage = (Parent) loader.load();
            Stage stage = (Stage) workerIDLabel.getScene().getWindow();
            stage.close();

            stage = new Stage();

            stage.setScene(new Scene(rootPharmacyPage, 1366, 710));
            stage.setMaximized(true);
            stage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
            stage.setTitle("Система учета лекарственных средств");
            stage.show();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    //Fun for registration button
    public void regNewPatient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_new_patient.fxml"));
            Parent rootRegistrationPage = (Parent) loader.load();

            Stage localStage = new Stage();
            localStage.setScene(new Scene(rootRegistrationPage, 600, 400));
            localStage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
            localStage.setTitle("Регистрация нового пациента");
            localStage.show();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    //Function for reg new user of IS
    public void addNewUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_new_user.fxml"));
            Parent rootRegistrationPage = (Parent) loader.load();

            Stage localStage = new Stage();
            localStage.setScene(new Scene(rootRegistrationPage, 600, 250));
            localStage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
            localStage.setTitle("Регистрация нового сотрудника");
            localStage.show();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void getPatientInfo() {
        try {
            FXMLLoader patientInfoLoader = new FXMLLoader(getClass().getResource("patient_info.fxml"));
            Parent rootPatientInfoPage = (Parent) patientInfoLoader.load();
            Stage stage = (Stage) workerIDLabel.getScene().getWindow();
            stage.close();

            stage = new Stage();
            stage.setScene(new Scene(rootPatientInfoPage, 600, 400));
            stage.setMaximized(true);
            stage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
            stage.setTitle("Пациенты");
            stage.show();
        }
        catch (Exception error) {
            System.out.println(error);
        }
    }

    //Function for check role of current user of IS
    private void checkRoleOfCurrentUser() {
        if(workerIDLabel.getText().startsWith("33")) {
            scheduleButton.setVisible(false);
            pharmacyButton.setVisible(false);
            newPatientRegButton.setVisible(false);
            patientMedInfoButton.setVisible(false);
            addNewUserButton.setVisible(true);
            registrationPatientButton.setVisible(false);
            patientButtonForDoctors.setVisible(false);
        } else if (workerIDLabel.getText().startsWith("11")) {
            scheduleButton.setVisible(true);
            pharmacyButton.setVisible(true);
            newPatientRegButton.setVisible(false);
            patientMedInfoButton.setVisible(false);
            addNewUserButton.setVisible(false);
            registrationPatientButton.setVisible(false);
            patientButtonForDoctors.setVisible(true);
        } else if (workerIDLabel.getText().startsWith("22")) {
            scheduleButton.setVisible(true);
            pharmacyButton.setVisible(false);
            newPatientRegButton.setVisible(false);
            patientMedInfoButton.setVisible(false);
            addNewUserButton.setVisible(false);
            registrationPatientButton.setVisible(true);
            patientButtonForDoctors.setVisible(false);
        } else if(workerIDLabel.getText().startsWith("42")) {
            scheduleButton.setVisible(true);
            pharmacyButton.setVisible(true);
            newPatientRegButton.setVisible(true);
            patientMedInfoButton.setVisible(true);
            addNewUserButton.setVisible(false);
            registrationPatientButton.setVisible(false);
            patientButtonForDoctors.setVisible(false);
        }
    }

}
