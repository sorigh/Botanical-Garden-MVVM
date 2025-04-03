package org.example.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.viewmodel.PlantViewModel;
import org.example.viewmodel.dto.PlantDTO;

import java.awt.event.MouseEvent;

public class PlantView {
    private final PlantViewModel viewModel = new PlantViewModel();
    public Button updateButton;
    public Button deleteButton;
    public Button clearFieldsButton;
    public Button addButton;

    @FXML private TableView<PlantDTO> plantTable;
    @FXML private TableColumn<PlantDTO, Integer> idColumn;
    @FXML private TableColumn<PlantDTO, String> nameColumn;
    @FXML private TableColumn<PlantDTO, String> typeColumn;
    @FXML private TableColumn<PlantDTO, String> speciesColumn;
    @FXML private TableColumn<PlantDTO, Integer> carnivoreColumn;

    @FXML private TextField nameField;
    @FXML private TextField typeField;
    @FXML private TextField speciesField;
    @FXML private TextField carnivoreField;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        // Initialize Table Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        carnivoreColumn.setCellValueFactory(new PropertyValueFactory<>("carnivore"));

        // Bind TableView to ViewModel
        plantTable.setItems(viewModel.getPlantList());

        // Bind input fields to ViewModel properties
        nameField.textProperty().bindBidirectional(viewModel.nameProperty());
        typeField.textProperty().bindBidirectional(viewModel.typeProperty());
        speciesField.textProperty().bindBidirectional(viewModel.speciesProperty());
        carnivoreField.textProperty().bindBidirectional(viewModel.carnivoreProperty());

        // Bind selected row to ViewModel
        viewModel.selectedPlantProperty().bind(plantTable.getSelectionModel().selectedItemProperty());

        //Bind buttons
        updateButton.onActionProperty().bind(viewModel.getUpdateCommand());
        addButton.onActionProperty().bind(viewModel.getAddCommand());
        deleteButton.onActionProperty().bind(viewModel.getDeleteCommand());
        clearFieldsButton.onActionProperty().bind(viewModel.getClearFieldsCommand());

    }

    private void showAlert(String message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
