package com.sfu.diploma.diplomadesktop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LastAppointmentInfoController implements Initializable {
    public LastAppointmentInfoController() {}

    private final static JDBCManager manager = new JDBCManager("andro", "andro");

    @FXML
    public Label mainInfoAboutAppointmentLabel;
    public Label patientSurnameLabel;
    public Label patientNameLabel;
    public Label patientMidnameLabel;
    public Label birthdayLabel;
    public Label disabilityLabel;
    public Label doctorIDLabel;
    public Label appointmentTimeLabel;
    public Label symptomsLabel;
    public Label diagnoseLabel;
    public Label prescriptionDrugsLabel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setValuesForElements();
    }

    private void setValuesForElements() {
        String appointmentID = General.getValueFromTextFile("file1.txt");
        String patientID;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(manager.getUrl(), manager.getNameOfUserInDataBase(),
                    manager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM appointments WHERE appointment_id = '" + appointmentID + "'";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            mainInfoAboutAppointmentLabel.setText("Прием №" + resultSet.getString("appointment_id") + " от " +
                    convertDateToStandardForm(resultSet.getDate("appointment_date")));
            patientID = resultSet.getString("patient_id");
            doctorIDLabel.setText("ID врача: " + resultSet.getString("doctor_id"));
            appointmentTimeLabel.setText("Время приема: " + resultSet.getTime("appointment_time"));
            symptomsLabel.setText("Симптомы: " + resultSet.getString("symptoms"));
            diagnoseLabel.setText("Диагноз: " + resultSet.getString("diagnose"));
            StringBuilder stringBuilder = new StringBuilder();
            String[] prescriptionDrugs = (resultSet.getString("prescription_medications")).split("@");
            stringBuilder.append("Назначенные лекарства:\n");
            for (String drug: prescriptionDrugs) {stringBuilder.append("- ").append(drug).append("\n");}
            prescriptionDrugsLabel.setText(stringBuilder.toString());
            connect.close();

            Class.forName("com.mysql.jdbc.Driver");
            Connection connect1 = DriverManager.getConnection(manager.getUrl(), manager.getNameOfUserInDataBase(),
                    manager.getPasswordOfUserInDataBase());
            String query1 = "SELECT * FROM patients WHERE id = '" + patientID + "'";
            Statement statement1 = connect1.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(query1);
            resultSet1.next();

            patientSurnameLabel.setText("Фамилия: " + resultSet1.getString("patient_surname"));
            patientNameLabel.setText("Имя: " + resultSet1.getString("patient_name"));
            patientMidnameLabel.setText("Отчество: " + resultSet1.getString("patient_midname"));
            birthdayLabel.setText("Дата рождения: " + convertDateToStandardForm(resultSet1.getDate("birthday")));
            disabilityLabel.setText("Инвалидность (группа): " + resultSet1.getString("disability"));

            connect1.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println(ex);
        }
    }

    private String convertDateToStandardForm(Date dateInIncorrectForm) {
        return (dateInIncorrectForm.toString()).substring(8, 10) + "." + (dateInIncorrectForm.toString()).substring(5, 7) +
                "." + (dateInIncorrectForm.toString()).substring(0, 4);
    }
}
