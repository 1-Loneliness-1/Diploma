package com.sfu.diploma.diplomadesktop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class PatientInfoController implements Initializable {
    public PatientInfoController() {}

    private String appointmentID = "";
    private String selectedID = "";
    private String symptoms = "";
    private String diagnose = "";
    private Date appointmentDate = new Date(2023, 4, 2);
    private Time appointmentTime = new Time(8, 0, 0);
    private int globalValue = 1;
    private String appointmentIDInButton;

    @FXML
    public Label workerIDLabel;
    public Label surnameLabel;
    public Label nameLabel;
    public Label midNameLabel;
    public Label positionLabel;
    public ImageView userAvatar;
    public VBox patientsVBox;
    public Rectangle backgroundRectangle;
    public Label patientIdLabel;
    public Label patientSurnameLabel;
    public Label patientNameLabel;
    public Label patientMidnameLabel;
    public Label birthdayLabel;
    public Label passportLabel;
    public Label pollisLabel;
    public Label workplaceLabel;
    public Label disabilityLabel;
    public Label phoneLabel;
    public Button closePatientInfoButton;
    public VBox allVisitsVBox;
    public ScrollPane allVisitsScrollPane;
    public ScrollPane allDrugsScrollPane;
    public VBox allDrugsVBox;
    public Button beginNewAppointmentButton;
    public Label newAppointmentLabel;
    public Label symptomsLabel;
    public TextField symptomsTextField;
    public Label diagnoseLabel;
    public TextField diagnoseTextField;
    public Label prescriptionDrugsLabel;
    public VBox prescriptionDrugsVBox;
    public Button cancelNewAppointmentButton;
    public Button finishAppointmentButton;
    public Button addNewDrugsButton;
    public Label lastAppointmentLabel;
    public Label allPrescriptionDrugsLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initInfoAboutUser();
        setVisibilityForLabels(false);
        setVisibilityForAppointmentElements(false);

        allDrugsVBox.setSpacing(2);
        prescriptionDrugsVBox.setSpacing(2);

        closePatientInfoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setVisibilityForLabels(false);
                setVisibilityForAppointmentElements(false);

            }
        });

        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM patients";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Button buttonForAdd = new Button("ID:" + resultSet.getString("id") + " " +
                        resultSet.getString("patient_surname") + " " + (resultSet.getString("patient_name")).charAt(0) +
                        "." + (resultSet.getString("patient_midname")).charAt(0) + ".");
                buttonForAdd.setCursor(Cursor.HAND);
                buttonForAdd.setStyle("-fx-pref-width: 285; -fx-pref-height: 30; -fx-border-color: grey; " +
                        "-fx-background-color: gainsboro; -fx-font-size: 14px; -fx-text-alignment: center;");
                buttonForAdd.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        setTextForLabels(buttonForAdd.getText());
                        setVisibilityForAppointmentElements(false);
                    }
                });
                patientsVBox.getChildren().add(buttonForAdd);
            }
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }

    public void goToFirstPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent rootFirstPage = (Parent) loader.load();
            Stage stage = (Stage) workerIDLabel.getScene().getWindow();
            stage.close();

            stage = new Stage();
            stage.setScene(new Scene(rootFirstPage, 600, 400));
            stage.setMaximized(true);
            stage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
            stage.setTitle("Pulse-MIS");
            stage.show();
        }
        catch (Exception error) {
            System.out.println(error);
        }
    }

    private void initInfoAboutUser() {
        String[] parseFileResult = General.parseFile();
        workerIDLabel.setText(parseFileResult[0]);
        surnameLabel.setText(parseFileResult[1]);
        nameLabel.setText(parseFileResult[2]);
        midNameLabel.setText(parseFileResult[3]);
        positionLabel.setText(parseFileResult[4]);
        Image image = new Image(getClass().getResourceAsStream(parseFileResult[5].toString()));
        userAvatar.setImage(image);
    }

    //Function for set visibility for group of elements
    private void setVisibilityForLabels(boolean visible) {
        backgroundRectangle.setVisible(visible);
        allPrescriptionDrugsLabel.setVisible(visible);
        patientIdLabel.setVisible(visible);
        patientSurnameLabel.setVisible(visible);
        patientNameLabel.setVisible(visible);
        patientMidnameLabel.setVisible(visible);
        birthdayLabel.setVisible(visible);
        passportLabel.setVisible(visible);
        pollisLabel.setVisible(visible);
        workplaceLabel.setVisible(visible);
        disabilityLabel.setVisible(visible);
        phoneLabel.setVisible(visible);
        closePatientInfoButton.setVisible(visible);
        allVisitsVBox.setVisible(visible);
        allVisitsScrollPane.setVisible(visible);
        allDrugsScrollPane.setVisible(visible);
        allDrugsVBox.setVisible(visible);
        beginNewAppointmentButton.setVisible(visible);
        lastAppointmentLabel.setVisible(visible);
    }

    //Function for set values from db to labels
    private void setTextForLabels(String textOnPressedButton) {
        setVisibilityForLabels(false);
        char[] charArray = new char[6];
        textOnPressedButton.getChars(3, 9, charArray, 0);
        StringBuilder patientID = new StringBuilder();
        for (int i = 0; i < 6; i++) {patientID.append(charArray[i]);}
        selectedID = patientID.toString();

        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM patients WHERE id = " + patientID;
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();

            patientIdLabel.setText("ID: " + resultSet.getString("id"));
            patientSurnameLabel.setText(resultSet.getString("patient_surname"));
            patientNameLabel.setText(resultSet.getString("patient_name"));
            patientMidnameLabel.setText(resultSet.getString("patient_midname"));
            passportLabel.setText("Паспорт: " + resultSet.getString("num_of_passport"));
            pollisLabel.setText("Полис: " + resultSet.getString("num_of_polis"));
            birthdayLabel.setText("Дата рождения: " + convertDateToStandardForm(resultSet.getDate("birthday")));
            workplaceLabel.setText("Место работы: " + resultSet.getString("workplace"));
            disabilityLabel.setText("Инвалидность: " + resultSet.getString("disability"));
            phoneLabel.setText("Телефон: " + resultSet.getString("phone"));

            connect.close();

            setAllVisits();
            setAllDrugs();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
        setVisibilityForLabels(true);
    }

    //Function for convert date to normal format
    private String convertDateToStandardForm(Date incorrectFormOfDate) {
        return (incorrectFormOfDate.toString()).substring(8, 10) + "." + (incorrectFormOfDate.toString()).substring(5, 7) +
                "." + (incorrectFormOfDate.toString()).substring(0, 4);
    }

    //Function for setting values to allVisitsVBox
    private void setAllVisits() {
        allVisitsVBox.getChildren().clear();
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM appointments WHERE patient_id = '" + selectedID + "'";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Button buttonForAddInVBox = new Button("ID:" + resultSet.getString("appointment_id") +
                        ":от " + convertDateToStandardForm(resultSet.getDate("appointment_date")));
                buttonForAddInVBox.setCursor(Cursor.HAND);
                buttonForAddInVBox.setStyle("-fx-pref-width: 220; -fx-pref-height: 30; -fx-border-color: gainsboro; " +
                        "-fx-font-size: 16px; -fx-font-family: Arial; -fx-text-alignment: left; -fx-border-radius: 5; " +
                        "-fx-border-width: 2;");
                buttonForAddInVBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try(FileWriter writer = new FileWriter("file1.txt", false))
                        {
                            writer.write(buttonForAddInVBox.getText().split(":")[1] + "@");
                            writer.flush();
                        }
                        catch(IOException ex){
                            System.out.println(ex.getMessage());
                        }
                        try {
                            FXMLLoader addNewDrugLoader = new FXMLLoader(getClass().getResource("info_about_past_appointment.fxml"));
                            Parent rootAddNewDrugWindow = (Parent) addNewDrugLoader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(rootAddNewDrugWindow, 600, 400));
                            stage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
                            stage.setTitle("Сведения о приеме");
                            stage.showAndWait();
                        }
                        catch (Exception error) {
                            System.out.println(error);
                        }
                    }
                });
                allVisitsVBox.getChildren().add(buttonForAddInVBox);
            }
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }

    //Function for setting values in all drugs element
    private void setAllDrugs() {
        allDrugsVBox.getChildren().clear();

        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM reserved_drugs WHERE patient_id = '" + selectedID + "'";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Button buttonForAddInVBox = new Button( "#:" + resultSet.getInt("reserve_id") + ":Статус: " +
                        resultSet.getString("current_status"));
                buttonForAddInVBox.setStyle("-fx-pref-width: 250; -fx-pref-height: 30; -fx-border-width: 2;" +
                        " -fx-border-color: gainsboro; -fx-border-radius: 5;" +
                        "-fx-font-size: 14px; -fx-font-family: Arial;");
                buttonForAddInVBox.setCursor(Cursor.HAND);
                buttonForAddInVBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try(FileWriter writer = new FileWriter("file1.txt", false))
                        {
                            writer.write(buttonForAddInVBox.getText().split(":")[1] + "@");
                            writer.flush();
                        }
                        catch(IOException ex){
                            System.out.println(ex.getMessage());
                        }
                        try {
                            FXMLLoader infoAboutReservedDrugLoader = new FXMLLoader(getClass().getResource("info_about_reserved_drug.fxml"));
                            Parent rootInfoAboutReservedDrug = (Parent) infoAboutReservedDrugLoader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(rootInfoAboutReservedDrug, 600, 350));
                            stage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
                            stage.setTitle("Сведения о заказе");
                            stage.showAndWait();
                        }
                        catch (Exception error) {
                            System.out.println(error);
                        }
                    }
                });
                allDrugsVBox.getChildren().add(buttonForAddInVBox);
            }
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }

    //Implementation function for starting new appointment (for showing text-fields)
    public void startNewAppointment() {
        beginNewAppointmentButton.setVisible(false);
        setVisibilityForAppointmentElements(true);
        symptomsTextField.setText("");
        diagnoseTextField.setText("");
        prescriptionDrugsVBox.getChildren().clear();

        //Search for unique appointment ID for DB
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM appointments";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int b = 0;
            while(resultSet.next()) b++;
            appointmentID = String.valueOf(b + 1);
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }

        java.util.Date currentDateAndTime = new java.util.Date();
        SimpleDateFormat formatForCurrentDateAndTime = new SimpleDateFormat("yyyy-MM-dd");
        appointmentDate = Date.valueOf(formatForCurrentDateAndTime.format(currentDateAndTime));
        formatForCurrentDateAndTime = new SimpleDateFormat("HH:mm:ss");
        appointmentTime = Time.valueOf(formatForCurrentDateAndTime.format(currentDateAndTime));

        newAppointmentLabel.setText("Новый прием №" + appointmentID + " от " + convertDateToStandardForm(appointmentDate) +
                " " + appointmentTime.toString());
    }

    private void setVisibilityForAppointmentElements(boolean visible) {
        newAppointmentLabel.setVisible(visible);
        symptomsLabel.setVisible(visible);
        symptomsTextField.setVisible(visible);
        diagnoseLabel.setVisible(visible);
        diagnoseTextField.setVisible(visible);
        prescriptionDrugsLabel.setVisible(visible);
        prescriptionDrugsVBox.setVisible(visible);
        cancelNewAppointmentButton.setVisible(visible);
        finishAppointmentButton.setVisible(visible);
        addNewDrugsButton.setVisible(visible);
    }

    public void cancelNewAppointment() {
        symptomsTextField.setText("");
        diagnoseTextField.setText("");
        prescriptionDrugsVBox.getChildren().clear();
        setVisibilityForAppointmentElements(false);
        beginNewAppointmentButton.setVisible(true);
    }

    //Function for send data about new appointment to db
    public void finishAppointment() {
        //Preparing necessary data for input in DB
        DateTimeZone dateTimeZone = DateTimeZone.forID("Europe/Moscow");
        DateTime youCanGetMedicineDateAndTime = (new DateTime(dateTimeZone)).plusDays(3);

        //Preparing data for sending in table 'reserved_drugs'
        String titleOfSelectedDrug = General.getTitleOfSelectedDrug();
        Date youCanGetMedicineDate = Date.valueOf(youCanGetMedicineDateAndTime.getYear() + "-" + youCanGetMedicineDateAndTime.getMonthOfYear() +
                "-" + youCanGetMedicineDateAndTime.getDayOfMonth());
        Time youCanGetMedicineTime = Time.valueOf(youCanGetMedicineDateAndTime.getHourOfDay() + ":" + youCanGetMedicineDateAndTime.getMinuteOfHour() + ":" +
                youCanGetMedicineDateAndTime.getSecondOfMinute());
        String currentStatus = "готов";

        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "INSERT reserved_drugs (patient_id, title_of_drug, you_can_get_drug_date, you_can_get_drug_time, current_status) VALUES " +
                    "('" + selectedID + "', '" + titleOfSelectedDrug + "', '" + youCanGetMedicineDate + "', '" + youCanGetMedicineTime + "', '"
                    + currentStatus + "');";
            Statement statement = connect.createStatement();
            statement.executeUpdate(query);
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }

        int currentNumOfDrug = getCurrentNumForDrug()-1;

        //Update of 'medicines' db table
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "UPDATE medicines SET current_num = " + currentNumOfDrug + " WHERE name_of_product LIKE '" + General.getTitleOfSelectedDrug() + "';";
            Statement statement = connect.createStatement();
            statement.executeUpdate(query);
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }

        //Preparing data for adding in db table appointments
        symptoms = symptomsTextField.getText();
        diagnose = diagnoseTextField.getText();
        StringBuilder prescriptionDrugsInAppointment = new StringBuilder();
        for (int i = 0; i < prescriptionDrugsVBox.getChildren().size(); i++) {
            Button but = (Button)prescriptionDrugsVBox.getChildren().get(i);
            prescriptionDrugsInAppointment.append(but.getText() + "@");
        }

        //Update appointments table
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "INSERT appointments (appointment_id, patient_id, doctor_id, symptoms, diagnose, prescription_medications, appointment_date, appointment_time) VALUES " +
                    "('" + appointmentID + "', '" + selectedID + "', '" + workerIDLabel.getText() + "', '" + symptoms + "', '" + diagnose + "', '" + prescriptionDrugsInAppointment.toString() + "', '" + appointmentDate + "', '" + appointmentTime + "');";
            Statement statement = connect.createStatement();
            statement.executeUpdate(query);
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }

        setVisibilityForAppointmentElements(false);
        beginNewAppointmentButton.setVisible(true);
        setAllVisits();
        setAllDrugs();
    }

    //Function for adding new drugs for current appointment
    public void addNewDrugsForAppointment() {
        try {
            FXMLLoader addNewDrugLoader = new FXMLLoader(getClass().getResource("add_new_drug.fxml"));
            Parent rootAddNewDrugWindow = (Parent) addNewDrugLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(rootAddNewDrugWindow, 600, 400));
            stage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
            stage.setTitle("Назначение нового лекарства для пациента");
            stage.showAndWait();
        }
        catch (Exception error) {
            System.out.println(error);
        }

        //Get all patient medicines for current appointment
        Button buttonForAdd = new Button(General.getTitleOfSelectedDrug());
        buttonForAdd.setCursor(Cursor.HAND);
        buttonForAdd.setStyle("-fx-pref-width: 300; -fx-pref-height: 30; -fx-border-color: gainsboro; " +
                "-fx-background-color: white; -fx-border-width: 2; -fx-border-radius: 5; -fx-font-size: 16px;");
        prescriptionDrugsVBox.getChildren().add(buttonForAdd);
    }

    private int getCurrentNumForDrug() {
        int result = 1;
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT current_num FROM medicines WHERE name_of_product LIKE '" + General.getTitleOfSelectedDrug() + "'";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            result = resultSet.getInt("current_num");
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
        return result;
    }
}
