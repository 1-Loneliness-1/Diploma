package com.sfu.diploma.diplomadesktop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public HelloController() {}

    private final JDBCManager jdbcManagerForCurrentClass = new JDBCManager("andro", "andro");

    @FXML
    public Button loginButton;
    public TextField idOfUserField;
    public PasswordField userPassField;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        checkReservedDrugsTable();
        loginButton.setDisable(true);
        idOfUserField.textProperty().addListener((obs, oldText, newText) -> {
            loginButton.setDisable(idOfUserField.getText().isEmpty() || userPassField.getText().isEmpty());
        });
        userPassField.textProperty().addListener((obs, oldText, newText) -> {
            loginButton.setDisable(idOfUserField.getText().isEmpty() || userPassField.getText().isEmpty());
        });
    }
    public void userLogin() throws Exception {
        //Checking if text field is empty
        if (idOfUserField.getText().isEmpty()) {
            Alert idFieldIsEmptyWindow = new Alert(Alert.AlertType.ERROR, "Поле \"ID сотрудника\" не может быть пустым!");
            idFieldIsEmptyWindow.showAndWait();
        }
        else if (userPassField.getText().isEmpty()) {
            Alert passFieldIsEmptyWindow = new Alert(Alert.AlertType.ERROR, "Поле \"Пароль\" не может быть пустым!");
            passFieldIsEmptyWindow.showAndWait();
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManagerForCurrentClass.getUrl(), jdbcManagerForCurrentClass.getNameOfUserInDataBase(),
                    jdbcManagerForCurrentClass.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM employees WHERE worker_id = " + idOfUserField.getText();
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Не удалось войти.\nПользователя с указанным ID не существует");
                alert.showAndWait();
                idOfUserField.requestFocus();
            }
            else {
                if (resultSet.getString("pass").equals(userPassField.getText())) {
                    String currentWorkerID = resultSet.getString("worker_id");
                    String currentUserSurname = resultSet.getString("surname");
                    String currentUserName = resultSet.getString("name_");
                    String currentUserMidname = resultSet.getString("mid_name");
                    String currentUserPosition = resultSet.getString("position");
                    String photoUrl = resultSet.getString("photos_url");

                    try(FileWriter writer = new FileWriter("transfer.txt", false))
                    {
                        writer.write(currentWorkerID + "@");
                        writer.write(currentUserSurname + "@");
                        writer.write(currentUserName + "@");
                        writer.write(currentUserMidname + "@");
                        writer.write(currentUserPosition + "@");
                        writer.write(photoUrl + "@");
                        writer.flush();
                    }
                    catch(IOException ex){
                        System.out.println(ex.getMessage());
                    }

                    connect.close();

                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();
                    com.sfu.diploma.diplomadesktop.Login loginView = new Login();
                    loginView.showWindow();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Не удалось войти.\nПроверьте корректность введенного пароля!");
                    alert.showAndWait();
                    userPassField.requestFocus();
                }
            }
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println(String.valueOf(ex));
        }
    }

    //TODO Fix those functions (Incorrect changing values of current num of drug in db)
    private void checkReservedDrugsTable() {
        java.util.Date currentDateAndTime = new Date();
        Queue<String> resultsFromQuery = new LinkedList<String>();
        SimpleDateFormat formatForCurrentDateAndTime = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDateInCorrectFormat = java.sql.Date.valueOf(formatForCurrentDateAndTime.format(currentDateAndTime));
        formatForCurrentDateAndTime = new SimpleDateFormat("HH:mm:ss");
        Time currentTimeInCorrectFormat = Time.valueOf(formatForCurrentDateAndTime.format(currentDateAndTime));
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManagerForCurrentClass.getUrl(), jdbcManagerForCurrentClass.getNameOfUserInDataBase(),
                    jdbcManagerForCurrentClass.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM reserved_drugs WHERE current_status = 'готов' AND DATE(you_can_get_drug_date) < '" + currentDateInCorrectFormat + "' OR " +
                    "current_status = 'готов' AND DATE(you_can_get_drug_date) = '" + currentDateInCorrectFormat + "' AND TIME(you_can_get_drug_time) < '" + currentTimeInCorrectFormat + "';";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                resultsFromQuery.offer(resultSet.getString("title_of_drug"));
            }
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println(String.valueOf(ex));
        }

        //Update status for unpicked medicines in table reserved_drugs
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManagerForCurrentClass.getUrl(), jdbcManagerForCurrentClass.getNameOfUserInDataBase(),
                    jdbcManagerForCurrentClass.getPasswordOfUserInDataBase());
            String query = "UPDATE reserved_drugs SET current_status = 'просрочен' WHERE current_status = 'готов' AND DATE(you_can_get_drug_date) < '" + currentDateInCorrectFormat + "' OR " +
                    "current_status = 'готов' AND DATE(you_can_get_drug_date) = '" + currentDateInCorrectFormat + "' AND TIME(you_can_get_drug_time) < '" + currentTimeInCorrectFormat + "';";
            Statement statement = connect.createStatement();
            statement.executeUpdate(query);
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println(String.valueOf(ex));
        }

        //Incrementing current_num field in table medicines
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            Statement statement = connect.createStatement();

            for (String drug : resultsFromQuery) {
                int currentNum = getCurrentNumFieldForSelectedDrug(drug) + 1;
                statement.addBatch("UPDATE medicines SET current_num = " + currentNum + " WHERE name_of_product = '" + drug + "';");
            }

            statement.executeBatch();
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println(String.valueOf(ex));
        }
    }

    private int getCurrentNumFieldForSelectedDrug(String titleOfSelectedDrug) {
        int result = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManagerForCurrentClass.getUrl(), jdbcManagerForCurrentClass.getNameOfUserInDataBase(),
                    jdbcManagerForCurrentClass.getPasswordOfUserInDataBase());
            String query = "SELECT current_num FROM medicines WHERE name_of_product = '" + titleOfSelectedDrug + "';";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            result = resultSet.getInt("current_num");
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println(ex);
        }
        return result;
    }
}