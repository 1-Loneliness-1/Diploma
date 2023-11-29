package com.sfu.diploma.diplomadesktop;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class InfoAboutReservedDrugController implements Initializable {
    public InfoAboutReservedDrugController() {}

    private final JDBCManager manager = new JDBCManager("andro", "andro");

    @FXML
    public Label numOfOrderLabel;
    public Label clientSurnameLabel;
    public Label clientNameLabel;
    public Label clientMidnameLabel;
    public Label clientIDLabel;
    public Label clientDisabilityLabel;
    public Label reservedDrugTitleLabel;
    public Label reserveDateLabel;
    public Label reserveStatusLabel;
    public Button setDrugPickedButton;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setValuesInElementsFromDB();
    }

    private void setValuesInElementsFromDB() {
        String reserveID = General.getValueFromTextFile("file1.txt");
        String clientID;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(manager.getUrl(), manager.getNameOfUserInDataBase(),
                    manager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM reserved_drugs WHERE reserve_id = " + reserveID;
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();

            clientIDLabel.setText("ID: " + resultSet.getString("patient_id"));
            numOfOrderLabel.setText("Заказ № " + resultSet.getInt("reserve_id"));
            reservedDrugTitleLabel.setText("Название лекарства: " + resultSet.getString("title_of_drug"));
            reserveDateLabel.setText("Ожидает получения до " + General.convertDateToStandardForm(resultSet.getDate("you_can_get_drug_date")));
            reserveStatusLabel.setText("Статус заказа: " + resultSet.getString("current_status"));
            clientID = resultSet.getString("patient_id");
            setDrugPickedButton.setDisable(!resultSet.getString("current_status").equals("готов"));

            connect.close();

            Class.forName("com.mysql.jdbc.Driver");
            Connection connect1 = DriverManager.getConnection(manager.getUrl(), manager.getNameOfUserInDataBase(),
                    manager.getPasswordOfUserInDataBase());
            String query1 = "SELECT * FROM patients WHERE id = '" + clientID + "';";
            Statement statement1 = connect1.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(query1);
            resultSet1.next();

            clientSurnameLabel.setText("Фамилия: " + resultSet1.getString("patient_surname"));
            clientNameLabel.setText("Имя: " + resultSet1.getString("patient_name"));
            clientMidnameLabel.setText("Отчество: " + resultSet1.getString("patient_midname"));
            clientDisabilityLabel.setText("Инвалидность (группа): " + resultSet1.getString("disability"));

            connect1.close();

            setDrugPickedButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection connect = DriverManager.getConnection(manager.getUrl(), manager.getNameOfUserInDataBase(),
                                manager.getPasswordOfUserInDataBase());
                        String query = "UPDATE reserved_drugs SET current_status = 'получен' WHERE reserve_id = " + reserveID;
                        Statement statement = connect.createStatement();
                        statement.executeUpdate(query);
                        connect.close();
                        reserveStatusLabel.setText("Статус заказа: получен");
                    }
                    catch(Exception ex) {
                        System.out.println("Connection failure...");
                        System.out.println("" + ex);
                    }
                }
            });
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }
}
