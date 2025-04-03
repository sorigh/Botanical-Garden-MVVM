package org.example.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Parent plantView;
    private Parent specimenView;
    private Parent gardenView;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load views from FXML
            plantView = loadFXML("/PlantView.fxml");
            //specimenView = loadFXML("/org/example/view/SpecimenView.fxml");
            //gardenView = loadFXML("/org/example/view/GardenView.fxml");

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Create navigation buttons
        Button buttonPlants = new Button("Plants");
        Button buttonSpecimens = new Button("Specimens");
        Button buttonGarden = new Button("Garden");

        HBox menuBar = new HBox(20, buttonPlants, buttonSpecimens, buttonGarden);
        menuBar.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(plantView); // Default view

        // Button actions to switch views
        buttonPlants.setOnAction(e -> root.setCenter(plantView));
        buttonSpecimens.setOnAction(e -> root.setCenter(specimenView));
        buttonGarden.setOnAction(e -> root.setCenter(gardenView));

        Scene scene = new Scene(root, 1000, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Botanical Garden");
        primaryStage.show();
    }

    private Parent loadFXML(String resourcePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
        if (loader.getLocation() == null) {
            throw new IOException("FXML file not found: " + resourcePath);
        }
        return loader.load();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
