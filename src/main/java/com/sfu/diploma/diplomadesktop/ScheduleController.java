package com.sfu.diploma.diplomadesktop;

import Models.Schedule;
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

import java.sql.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {
    public ScheduleController() {

    }

    @FXML public Label workerIDLabel;
    @FXML public Label surnameLabel;
    @FXML public Label nameLabel;
    @FXML public Label midNameLabel;
    @FXML public Label positionLabel;
    @FXML public ImageView userAvatar;
    @FXML public Button refreshButton;
    @FXML public TableView<Schedule> scheduleTable;
    @FXML public TableColumn<Schedule, String> fioTabColumn;
    @FXML public TableColumn<Schedule, String> positionTabColumn;
    @FXML public TableColumn<Schedule, String> mondayTabColumn;
    @FXML public TableColumn<Schedule, String> tuesdayTabColumn;
    @FXML public TableColumn<Schedule, String> wednesdayTabColumn;
    @FXML public TableColumn<Schedule, String> thursdayTabColumn;
    @FXML public TableColumn<Schedule, String> fridayTabColumn;
    @FXML public TableColumn<Schedule, String> saturdayTabColumn;
    @FXML public TableColumn<Schedule, String> sundayTabColumn;
    public Button scheduleButton;
    public Button pharmacyButton;
    public Button newPatientRegButton;
    public Button goToPatientsPageButton;
    public Button patientInfoForDoctorButton;
    public Button regNewPatientForEmplButton;

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

        setVisibilityForElementsByRole();

        Image img = new Image(getClass().getResourceAsStream("/img/refresh.png"));
        refreshButton.setGraphic(new ImageView(img));

        fioTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        positionTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        mondayTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        tuesdayTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        wednesdayTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        thursdayTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        fridayTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        saturdayTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");
        sundayTabColumn.setStyle( "-fx-alignment: CENTER; -fx-font-size: 11pt;");

        fioTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                        ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getDoctorFIO());
            }
        });
        positionTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getPosition());
            }
        });
        mondayTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getMondaySchedule());
            }
        });
        tuesdayTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getTuesdaySchedule());
            }
        });
        wednesdayTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getWednesdaySchedule());
            }
        });
        thursdayTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getThursdaySchedule());
            }
        });
        fridayTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getFridaySchedule());
            }
        });
        saturdayTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getSaturdaySchedule());
            }
        });
        sundayTabColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule, String>,
                ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule, String> t) {
                return new SimpleStringProperty(t.getValue().getSundaySchedule());
            }
        });

        getSchedule();
    }

    //Fun for refresh table button
    public void getSchedule() {
        final ObservableList<Schedule> data = FXCollections.observableArrayList();
        scheduleTable.getItems().clear();

        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM schedule";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Schedule currentRow = new Schedule(
                        resultSet.getString("id"),
                        "some position",
                        resultSet.getString("monday"),
                        resultSet.getString("tuesday"),
                        resultSet.getString("wednesday"),
                        resultSet.getString("thursday"),
                        resultSet.getString("friday"),
                        resultSet.getString("saturday"),
                        resultSet.getString("sunday"));

                data.add(currentRow);
            }

            connect.close();
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect1 = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query1 = "SELECT worker_id, surname, name_, mid_name, position FROM employees";
            Statement statement1 = connect1.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(query1);

            while(resultSet1.next()) {
                for (Schedule elem: data) {
                    if (Objects.equals(elem.getDoctorFIO(), resultSet1.getString("worker_id"))) {
                        elem.setDoctorFIO(resultSet1.getString("surname") +
                                " " + resultSet1.getString("name_").charAt(0) + "." +
                                resultSet1.getString("mid_name").charAt(0) + ".");
                        elem.setPosition(resultSet1.getString("position"));
                    }
                }
            }
            connect1.close();
            scheduleTable.setItems(data);
        }
        catch(Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
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

    //Implementation of fun for going to page with list of all patients
    public void goToPatientsPage() {
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

    //Fun for returning to the first page of the app
    public void returnToFirstPage() {
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

    //Fun for set visible for elements for role of current user
    private void setVisibilityForElementsByRole() {
        if(workerIDLabel.getText().startsWith("11")) {
            scheduleButton.setVisible(true);
            pharmacyButton.setVisible(true);
            newPatientRegButton.setVisible(false);
            goToPatientsPageButton.setVisible(false);
            patientInfoForDoctorButton.setVisible(true);
            regNewPatientForEmplButton.setVisible(false);
        } else if(workerIDLabel.getText().startsWith("22")) {
            scheduleButton.setVisible(true);
            pharmacyButton.setVisible(false);
            newPatientRegButton.setVisible(false);
            goToPatientsPageButton.setVisible(false);
            patientInfoForDoctorButton.setVisible(false);
            regNewPatientForEmplButton.setVisible(true);
        } else if(workerIDLabel.getText().startsWith("42")) {
            scheduleButton.setVisible(true);
            pharmacyButton.setVisible(true);
            newPatientRegButton.setVisible(true);
            goToPatientsPageButton.setVisible(true);
            patientInfoForDoctorButton.setVisible(false);
            regNewPatientForEmplButton.setVisible(false);
        }
    }
}
