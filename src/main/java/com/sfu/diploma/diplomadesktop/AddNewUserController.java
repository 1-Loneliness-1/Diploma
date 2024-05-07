package com.sfu.diploma.diplomadesktop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddNewUserController implements Initializable {
    public AddNewUserController() {}

    @FXML
    public TextField newUserIDTextField;
    public TextField newUserPasswordTextField;
    public TextField userPositionTextField;
    public TextField userSurnameTextField;
    public TextField userNameTextField;
    public TextField userMidnameTextField;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

    public void pushDataInDatabase() {
        JDBCManager manager = new JDBCManager("andro","andro");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(manager.getUrl(), manager.getNameOfUserInDataBase(),
                    manager.getPasswordOfUserInDataBase());
            String query = "INSERT INTO employees (worker_id, pass, surname, name_, mid_name, position)" +
                    "VALUES ('" + newUserIDTextField.getText() + "', '" + newUserPasswordTextField.getText() + "', '" + userSurnameTextField.getText() +
                    "', '" + userNameTextField.getText() + "', '" + userMidnameTextField.getText() + "', '" + userPositionTextField.getText() + "');";
            Statement statement = connect.createStatement();
            statement.executeUpdate(query);
            connect.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Пользователь успешно зарегистрирован.");
            alert.showAndWait();
        } catch (Exception ex) {
            System.out.println("Connection failure...");
            System.out.println(ex);
        }
        Stage stg = (Stage) newUserIDTextField.getScene().getWindow();
        stg.close();
    }
}
