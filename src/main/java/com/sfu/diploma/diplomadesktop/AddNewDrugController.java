package com.sfu.diploma.diplomadesktop;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddNewDrugController implements Initializable {
    public AddNewDrugController() {}

    private String dynamicQuery = "";

    @FXML
    public VBox allDrugsInPharmacyVBox;
    public TextField nameOfDrugTextField;
    public Button confirmNewDrugsChoiceButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dynamicQuery = "SELECT * FROM medicines WHERE current_num > 0";
        setContentInAllDrugsVBox(dynamicQuery);

        nameOfDrugTextField.textProperty().addListener((obs, oldText, newText) -> {
            dynamicQuery = "SELECT * FROM medicines WHERE current_num > 0 AND name_of_product LIKE '" +
                    newText + "%'";
            setContentInAllDrugsVBox(dynamicQuery);
        });
    }

    //Function for setting content in all drugs vbox
    private void setContentInAllDrugsVBox(String queryFromTextField) {
        allDrugsInPharmacyVBox.getChildren().clear();
        Label standardLabel = new Label(" №     Наименование лекарства   Всего/Осталось   Источник финанс.");
        standardLabel.setStyle("-fx-font-size: 16px; -fx-font-family: Arial; -fx-background-color: white;" +
                "-fx-border-color: gainsboro; -fx-border-radius: 5; -fx-border-width: 2; -fx-pref-height: 30;" +
                "-fx-pref-width: 530;");
        allDrugsInPharmacyVBox.getChildren().add(standardLabel);

        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(queryFromTextField);

            int i = 1;

            while(resultSet.next()) {
                Button buttonForAddInVBox = new Button(i + "." + resultSet.getString("name_of_product"));
                buttonForAddInVBox.setCursor(Cursor.HAND);
                buttonForAddInVBox.setStyle("-fx-font-size: 16px; -fx-font-family: Arial; -fx-background-color: white;" +
                        "-fx-border-color: gainsboro; -fx-border-radius: 5; -fx-border-width: 2; -fx-pref-height: 30;" +
                        "-fx-pref-width: 530;");
                Insets hz = new Insets(0, 60, 0 ,0);
                buttonForAddInVBox.setPadding(hz);
                buttonForAddInVBox.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try(FileWriter writer = new FileWriter("transfer_selected_drug.txt", false)) {
                            writer.write(getTitleOfSelectedDrug(buttonForAddInVBox.getText()) + "@");
                            writer.flush();
                        }
                        catch(IOException ex){
                            System.out.println(ex.getMessage());
                        }
                        confirmNewDrugsChoiceButton.setDisable(false);
                    }
                });
                allDrugsInPharmacyVBox.getChildren().add(buttonForAddInVBox);
                i++;
            }
            connect.close();
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }

    public void finishSelectingNewDrug() {
        Stage localStage = (Stage) confirmNewDrugsChoiceButton.getScene().getWindow();
        localStage.close();
    }

    //Function for get title of selected medicine from text in pressed button
    private String getTitleOfSelectedDrug(String textInPressedButton) {
        StringBuilder result = new StringBuilder();
        char[] charArray = textInPressedButton.toCharArray();
        for (int j = 0; j < charArray.length; j++) {
            if (charArray[j] == '.') {
                int k = j + 1;
                while(k != charArray.length) {
                    result.append(charArray[k]);
                    k++;
                }
            }
        }
        return result.toString();
    }
}
