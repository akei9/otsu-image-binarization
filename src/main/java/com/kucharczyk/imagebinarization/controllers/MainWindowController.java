package com.kucharczyk.imagebinarization.controllers;

import com.kucharczyk.imagebinarization.OtsuBinarization;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

public class MainWindowController {
    @FXML
    private TextField pathToFileTextField;

    @FXML
    private ImageView imageView;
    
    private String systemUsername = System.getProperty("user.name");
    private String defaultPath = "C:\\Users\\" + systemUsername + "\\Desktop";
    
    @FXML
    void initialize() throws IOException {
        pathToFileTextField.setText(defaultPath);
    }
    
    @FXML
    protected void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open image file");
        fileChooser.setInitialDirectory(new File(defaultPath));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        
        File file = fileChooser.showOpenDialog(null);
        
        if(file != null) {
            pathToFileTextField.setText(file.getAbsolutePath());
        }

    }
    
    public BufferedImage getOriginalImage() throws IOException {
        BufferedImage originalImage = null;
        originalImage = OtsuBinarization.readImage(pathToFileTextField.getText(), originalImage);
        return originalImage;
    }
    
    public void addImageToImageView(BufferedImage originalImage) {
        Image image = SwingFXUtils.toFXImage(originalImage, null);
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
    }
    
    protected void showAlertWindow() throws IOException {
            Stage window = new Stage();
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml/AlertWindow.fxml"));

            VBox vbox = loader.load();
            Label alertMessage = (Label) vbox.lookup("#alertMessage");
            
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Error");
            window.setResizable(false);

            alertMessage.setText("Nieprawidłowy format pliku!");
            Scene scene = new Scene(vbox);
            window.getIcons().add(new Image(this.getClass().getResource("/icons/java.png").toString()));
            window.setScene(scene);
            window.showAndWait();
    }
    
    @FXML
    protected void showOriginalImage() throws IOException {
        try {
            BufferedImage originalImage = getOriginalImage();
            addImageToImageView(originalImage);
        } catch(IOException e) {
            e.printStackTrace();
            showAlertWindow();
        }
    }

    @FXML
    protected void showGrayscaleImage() throws IOException {
        try {
            BufferedImage originalImage = getOriginalImage();
            OtsuBinarization.toGrayscale(originalImage);
            addImageToImageView(originalImage);
        } catch(IOException e) {
            e.printStackTrace();
            showAlertWindow();
        }
    }
    
    @FXML
    protected void showBinarizedImage() throws IOException {
        try {
            BufferedImage originalImage = getOriginalImage();
            OtsuBinarization.toBinary(originalImage);
            addImageToImageView(originalImage);
        } catch(IOException e) {
            e.printStackTrace();
            showAlertWindow();
        }
    }
}
