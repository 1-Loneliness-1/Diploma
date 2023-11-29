package com.sfu.diploma.diplomadesktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Login extends Application {
    Stage stage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 1366, 710));
        primaryStage.setMaximized(true);
        stage.getIcons().add(new Image("/img/icon_for_diploma_app_1024px_t.png"));
        primaryStage.setTitle("Pulse-MIS");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void  showWindow() throws Exception {
        start(stage);
    }
}
