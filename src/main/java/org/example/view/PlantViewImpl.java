package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.presenter.PlantPresenter;
import org.example.presenter.PlantView;
import org.example.presenter.dto.PlantDTO;

import java.util.List;

public class PlantViewImpl implements PlantView {
    private PlantPresenter presenter;
    private TableView<PlantDTO> plantTable;
    private Label messageLabel;

    private TextField nameField;
    private TextField typeField;
    private TextField speciesField;
    private TextField carnivoreField;

    private Button addPlantButton;
    private Button updatePlantButton;
    private Button deletePlantButton;

    private VBox rootLayout;

    public PlantViewImpl() {
        initView();
        presenter = new PlantPresenter(this);
        presenter.loadPlant();
    }

    public void initView() {
        plantTable = new TableView<>();
        messageLabel = new Label();

        TableColumn<PlantDTO, Integer> idColumn = new TableColumn<>("Plant_id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        idColumn.setPrefWidth(50);

        TableColumn<PlantDTO, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);

        TableColumn<PlantDTO, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(200);

        TableColumn<PlantDTO, String> speciesColumn = new TableColumn<>("Species");
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        speciesColumn.setPrefWidth(100);

        TableColumn<PlantDTO, Integer> carnivoreColumn = new TableColumn<>("Carnivore");
        carnivoreColumn.setCellValueFactory(new PropertyValueFactory<>("carnivore"));





        plantTable.getColumns().addAll(idColumn, nameColumn, typeColumn, speciesColumn, carnivoreColumn);

        plantTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nameField.setText(newSel.getName());
                typeField.setText(newSel.getType());
                speciesField.setText(newSel.getSpecies());
                carnivoreField.setText(String.valueOf(newSel.getCarnivore()));
            }
        });

        nameField = new TextField();
        nameField.setPromptText("Name");
        typeField = new TextField();
        typeField.setPromptText("Type");
        speciesField = new TextField();
        speciesField.setPromptText("Species");
        carnivoreField = new TextField();
        carnivoreField.setPromptText("Carnivore");


        addPlantButton = new Button("Add Plant");
        addPlantButton.setOnAction(e -> {
            presenter.addPlant();
            clearFields();
        });

        updatePlantButton = new Button("Update Plant");
        updatePlantButton.setOnAction(e -> {
            presenter.updatePlant();
            clearFields();
        });

        deletePlantButton = new Button("Delete Plant");
        deletePlantButton.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Plant");
            confirm.setContentText("Are you sure you want to delete this plant?");
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.OK) {
                    presenter.deletePlant();
                    clearFields();
                }
            });
        });

        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(10));
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(new Label("Name:"), 0, 0);
        inputGrid.add(nameField, 1, 0);
        inputGrid.add(new Label("Type:"), 0, 1);
        inputGrid.add(typeField, 1, 1);
        inputGrid.add(new Label("Species:"), 0, 2);
        inputGrid.add(speciesField, 1, 2);
        inputGrid.add(new Label("Carnivore:"), 0, 3);
        inputGrid.add(carnivoreField, 1, 3);


        HBox buttonBox = new HBox(10, addPlantButton, updatePlantButton, deletePlantButton);
        buttonBox.setPadding(new Insets(10));

        rootLayout = new VBox(10, plantTable, inputGrid, buttonBox, messageLabel);
        rootLayout.setPadding(new Insets(10));
    }

    public VBox getView() {
        return rootLayout;
    }

    private void clearFields() {
        nameField.clear();
        typeField.clear();
        speciesField.clear();
        carnivoreField.clear();
    }

    @Override
    public void displayPlants(List<PlantDTO> products) {
        plantTable.getItems().clear();
        plantTable.getItems().addAll(products);
    }

    @Override
    public void displayPlant(PlantDTO plant) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Plant Details");
        info.setHeaderText("Details for Plant ID: " + plant.getPlant_id());
        info.setContentText("Name: " + plant.getName() +
                "\nSpecies: " + plant.getSpecies() +
                "\nType: " + plant.getType() +
                "\nCarnivore: " + plant.getCarnivore());

        info.showAndWait();
    }

    @Override
    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    @Override
    public void showError(String errorMessage) {
        messageLabel.setText(errorMessage);
    }


    @Override
    public String getPlantName() {
        return nameField.getText();
    }

    @Override
    public String getPlantType() {
        return typeField.getText();
    }

    @Override
    public String getPlantSpecies() {
        return speciesField.getText();
    }

    @Override
    public String getPlantCarnivore() {
        return carnivoreField.getText();
    }

    @Override
    public PlantDTO getSelectedProduct() {
        return plantTable.getSelectionModel().getSelectedItem();
    }


}
