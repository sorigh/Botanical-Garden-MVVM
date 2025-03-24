package org.example.view;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.example.presenter.PlantView;
import org.example.view.PlantViewImpl;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        PlantView plantView = new PlantViewImpl();

        Button btnCosmetics = new Button("Plants");


        HBox menuBar = new HBox(10, btnCosmetics);
        menuBar.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(plantView.getView());

        btnCosmetics.setOnAction(e -> root.setCenter(plantView.getView()));

        Scene scene = new Scene(root, 900, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Menu");
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}