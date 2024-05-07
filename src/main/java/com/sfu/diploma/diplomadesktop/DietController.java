package com.sfu.diploma.diplomadesktop;

import Models.FoodNutrients;
import Models.FoodResponse;
import com.sfu.diploma.diplomadesktop.utils.Networker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DietController implements Initializable {

    private FoodResponse foodResponse = null;
    private String currentPatientId;

    @FXML
    public TextField tfNameOfProduct;
    public Button bSearchProduct;
    public Label lNameOfProduct;
    public Label lManufacturer;
    public Label lComponents;
    public Label lWeight;
    public Button bPrescription;
    public Button bCancelPrescription;
    public VBox vbNutrients;
    public VBox vbAlreadyPrescProducts;

    public DietController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAlreadyPrescProducts();
        currentPatientId = getCurrentPatientId();

        tfNameOfProduct.textProperty().addListener((obs, oldText, newText) -> {
            bSearchProduct.setDisable(newText.isBlank());
        });
        bSearchProduct.setOnAction(event -> {
            setVisibilityOfElements(false);
            try {
                foodResponse = Networker.sendRequest(tfNameOfProduct.getText());
                if (foodResponse == null) {
                    setVisibilityOfElements(false);
                    vbNutrients.getChildren().removeAll();
                    lNameOfProduct.setText("Nothing found!");
                    lNameOfProduct.setVisible(true);
                } else {
                    vbNutrients.getChildren().removeAll();
                    lNameOfProduct.setText("Название: " + foodResponse.getBrandName());
                    lManufacturer.setText("Описание: " + foodResponse.getDescription());
                    lComponents.setText("Состав: " + foodResponse.getIngredients());
                    lWeight.setText("Вес: " + foodResponse.getPackageWeight());
                    setVisibilityOfElements(true);

                    for (FoodNutrients nutrient : foodResponse.getFoodNutrients()) {
                        Button addingButton = new Button(nutrient.getNutrientName() + " " +
                                nutrient.getValue() + " " + nutrient.getUnitName()
                        );
                        addingButton.setStyle("-fx-pref-width: 285; -fx-pref-height: 30; -fx-border-color: grey; " +
                                "-fx-background-color: gainsboro; -fx-font-size: 14px; -fx-text-alignment: center;");
                        addingButton.setOnAction(event1 -> {
                            //Something to do in the future
                        });
                        vbNutrients.getChildren().add(addingButton);
                    }
                }
            } catch (Exception e) {
                System.out.println("Something went wrong!");
            }
        });
        bCancelPrescription.setOnAction(event -> {
            tfNameOfProduct.setText("");
            bSearchProduct.setDisable(true);
            setVisibilityOfElements(false);
            vbNutrients.getChildren().removeAll();
            foodResponse = null;
        });
        bPrescription.setOnAction(event -> {
            setVisibilityOfElements(false);
            tfNameOfProduct.setText("");
            bSearchProduct.setDisable(true);
            vbNutrients.getChildren().removeAll();

            sendPrescProductInDB();
            updateStateOfPrescProductsVB();
        });
    }

    private String getCurrentPatientId() {
        String result;
        try (FileReader rd = new FileReader("transfer_patient_id.txt")) {
            BufferedReader reader = new BufferedReader(rd);
            result = reader.readLine();
        } catch (IOException ex) {
            result = "007734";
            System.out.println(ex.getMessage());
        }

        return result;
    }

    private void setVisibilityOfElements(Boolean visibility) {
        lNameOfProduct.setVisible(visibility);
        lManufacturer.setVisible(visibility);
        lComponents.setVisible(visibility);
        lWeight.setVisible(visibility);
        bPrescription.setVisible(visibility);
        bCancelPrescription.setVisible(visibility);
    }

    private void setAlreadyPrescProducts() {
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM products_for_diet WHERE patient_id = " + currentPatientId;
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Button bForAdd = new Button(resultSet.getInt("id") + ". " +
                        resultSet.getString("description_")
                );
                bForAdd.setStyle("-fx-pref-width: 220; -fx-pref-height: 30; -fx-border-color: gainsboro; " +
                        "-fx-font-size: 16px; -fx-font-family: Arial; -fx-text-alignment: left; -fx-border-radius: 5; " +
                        "-fx-border-width: 2;");
                bForAdd.setOnAction(event1 -> {
                    //Do something on tap
                });
                vbAlreadyPrescProducts.getChildren().add(bForAdd);
            }
            connect.close();
        } catch (Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }

    private void sendPrescProductInDB() {
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "INSERT INTO products_for_diet (patient_id, brand_name, package_weight, " +
                    "description_, ingredients)" +
                    " VALUES ('" + currentPatientId + "', '" + foodResponse.getBrandName() + "', '" +
                    foodResponse.getPackageWeight() + "', '" + foodResponse.getDescription() +
                    "', '" + foodResponse.getIngredients() + "');";
            Statement statement = connect.createStatement();
            statement.executeUpdate(query);
            statement.close();
            connect.close();
        } catch (Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }

    private void updateStateOfPrescProductsVB() {
        try {
            JDBCManager jdbcManager = new JDBCManager("andro", "andro");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection(jdbcManager.getUrl(), jdbcManager.getNameOfUserInDataBase(),
                    jdbcManager.getPasswordOfUserInDataBase());
            String query = "SELECT * FROM products_for_diet ORDER BY id DESC LIMIT 1;";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();

            Button bForAdd = new Button(resultSet.getInt("id") + ". " +
                    resultSet.getString("description_")
            );
            bForAdd.setStyle("-fx-pref-width: 220; -fx-pref-height: 30; -fx-border-color: gainsboro; " +
                    "-fx-font-size: 16px; -fx-font-family: Arial; -fx-text-alignment: left; -fx-border-radius: 5; " +
                    "-fx-border-width: 2;");
            bForAdd.setOnAction(event1 -> {
                //Do something on tap
            });
            vbAlreadyPrescProducts.getChildren().add(bForAdd);

            statement.close();
            connect.close();
        } catch (Exception ex) {
            System.out.println("Connection failure...");
            System.out.println("" + ex);
        }
    }
}
