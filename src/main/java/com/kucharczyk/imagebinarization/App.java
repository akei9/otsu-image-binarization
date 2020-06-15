package com.kucharczyk.imagebinarization;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    public static void main( String[] args ) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/MainWindow.fxml"));
        
        VBox vbox = loader.load();
        
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(this.getClass().getResource("/css/style.css").toString());
        
        primaryStage.setResizable(false);
        primaryStage.setTitle("Image Binarization App");
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/icons/java.png").toString()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
