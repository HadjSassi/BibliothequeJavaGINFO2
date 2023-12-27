package sample.App.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("Boarder.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("emprunt/EmpruntConsultation.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Bibliothéque");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
