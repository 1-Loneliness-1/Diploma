package com.sfu.diploma.diplomadesktop;

import Models.Pharmacy;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PharmacyController implements Initializable {
    public PharmacyController() {}

    private String dynamicQuery = "SELECT * FROM medicines WHERE current_num > 0";

    @FXML
    public Label workerIDLabel;
    public Label surnameLabel;
    public Label nameLabel;
    public Label midNameLabel;
    public Label positionLabel;
    public ImageView userAvatar;
    public TableView<Pharmacy> pharmacyTable;
    public TableColumn numOfRowTabColumn;
    public TableColumn productNameTabColumn;
    public TableColumn totalNumberOfProductTabColumn;
    public TableColumn leftPacksNumTabColumn;
    public TableColumn priceTabColumn;
    public TableColumn sumTabColumn;
    public TableColumn shelfLifeTabColumn;
    public TableColumn manufacturerTabColumn;
    public TableColumn sourceOfFundingTabColumn;
    public TextField sumTextField;
    public TextField priceTextField;
    public ChoiceBox<String> sourceOfFoundChoiceBox;
    public ChoiceBox<String> manufacturerChoiceBox;
    public Button patientInfoForDoctorButton;
    public Button scheduleButton;
    public Button pharmacyButton;
    public Button newPatientRegButton;
    public Button goToPatientPageButton;

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

        checkPositionOfCurrentUser();

        numOfRowTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        productNameTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        totalNumberOfProductTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        leftPacksNumTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        priceTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        sumTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        shelfLifeTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        manufacturerTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        sourceOfFundingTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");

        numOfRowTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pharmacy, String> t) {
                return new SimpleStringProperty(t.getValue().getNumberOfRow());
            }
        });
        productNameTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pharmacy, String> t) {
                return new SimpleStringProperty(t.getValue().getNameOfProduct());
            }
        });
        totalNumberOfProductTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, Integer>, SimpleIntegerProperty>() {
            public SimpleIntegerProperty call(TableColumn.CellDataFeatures<Pharmacy, Integer> t) {
                return new SimpleIntegerProperty(t.getValue().getTotalNumberOfProduct());
            }
        });
        leftPacksNumTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, Integer>, SimpleIntegerProperty>() {
            public SimpleIntegerProperty call(TableColumn.CellDataFeatures<Pharmacy, Integer> t) {
                return new SimpleIntegerProperty(t.getValue().getCurrentNumberOfProduct());
            }
        });
        priceTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, Float>, SimpleFloatProperty>() {
            public SimpleFloatProperty call(TableColumn.CellDataFeatures<Pharmacy, Float> t) {
                return new SimpleFloatProperty(t.getValue().getPriceOfProduct());
            }
        });
        sumTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, Float>, SimpleFloatProperty>() {
            public SimpleFloatProperty call(TableColumn.CellDataFeatures<Pharmacy, Float> t) {
                return new SimpleFloatProperty(t.getValue().getSumOfProduct());
            }
        });
        shelfLifeTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pharmacy, String> t) {
                return new SimpleStringProperty(t.getValue().getShelfLife());
            }
        });
        manufacturerTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pharmacy, String> t) {
                return new SimpleStringProperty(t.getValue().getManufacturerOfProduct());
            }
        });
        sourceOfFundingTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Pharmacy, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Pharmacy, String> t) {
                return new SimpleStringProperty(t.getValue().getSourceOfFound());
            }
        });

        ObservableList<String> sources = FXCollections.observableArrayList();
        sources.add("все");
        sources.add("внебюджетные");
        sources.add("бюджетные");
        sourceOfFoundChoiceBox.setItems(sources);

        //Set default values
        sourceOfFoundChoiceBox.setValue("все");
        manufacturerChoiceBox.setValue("все");

        refreshPharmacyTable();
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

    //Fun for refresh pharmacy table
    public void refreshPharmacyTable() {
        final ObservableList<Pharmacy> data = FXCollections.observableArrayList();
        pharmacyTable.getItems().clear();

        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(dynamicQuery);

            int i = 1;
            ObservableList<String> manufacturers = FXCollections.observableArrayList();

            while(resultSet.next()) {
                Pharmacy currentRow = new Pharmacy(
                        String.valueOf(i),
                        resultSet.getString("name_of_product"),
                        resultSet.getInt("total_num"),
                        resultSet.getInt("current_num"),
                        resultSet.getFloat("price"),
                        resultSet.getInt("current_num") * resultSet.getFloat("price"),
                        resultSet.getString("shelf_life"),
                        resultSet.getString("manufacturer"),
                        resultSet.getString("source_of_found")
                        );
                data.add(currentRow);
                manufacturers.add(resultSet.getString("manufacturer"));
                i++;
            }
            manufacturers.add("все");
            manufacturerChoiceBox.setItems(manufacturers);

            connect.close();
            pharmacyTable.setItems(data);
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }

    //fun for filter button
    public void filterEntries() {
        dynamicQuery = "SELECT * FROM medicines WHERE current_num > 0";
        if (!sumTextField.getText().equals("")) {
            dynamicQuery += " AND price * current_num " + sumTextField.getText();
        }
        if (!priceTextField.getText().equals("")) {
            dynamicQuery += " AND price " + priceTextField.getText();
        }
        if (!sourceOfFoundChoiceBox.getValue().equals("все")) {
            dynamicQuery += " AND source_of_found = '" + sourceOfFoundChoiceBox.getValue() + "'";
        }
        if (!manufacturerChoiceBox.getValue().equals("все")) {
            dynamicQuery += " AND manufacturer = '" + manufacturerChoiceBox.getValue() + "'";
        }
        refreshPharmacyTable();
    }

    //Fun for return to first page button
    public void returnToFirstPage() {
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

    //Fun for going to info about patient page
    public void goToPatientPage() {
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

    private void checkPositionOfCurrentUser() {
        if(workerIDLabel.getText().startsWith("11")) {
            patientInfoForDoctorButton.setVisible(true);
            scheduleButton.setVisible(true);
            pharmacyButton.setVisible(true);
            newPatientRegButton.setVisible(false);
            goToPatientPageButton.setVisible(false);
        }
        else if(workerIDLabel.getText().startsWith("42")) {
            patientInfoForDoctorButton.setVisible(false);
            scheduleButton.setVisible(true);
            pharmacyButton.setVisible(true);
            newPatientRegButton.setVisible(true);
            goToPatientPageButton.setVisible(true);
        }
    }
}
