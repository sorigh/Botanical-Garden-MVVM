package org.example.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.viewmodel.SpecimenViewModel;
import org.example.viewmodel.dto.SpecimenDTO;

public class SpecimenView {
    private final SpecimenViewModel viewModel = new SpecimenViewModel();

    @FXML private TableView<SpecimenDTO> specimenTable;
    @FXML private TableColumn<SpecimenDTO, Integer> idColumn;
    @FXML private TableColumn<SpecimenDTO, Integer> plantIdColumn;
    @FXML private TableColumn<SpecimenDTO, String> locationColumn;
    @FXML private TableColumn<SpecimenDTO, String> imageColumn;

    @FXML private TextField locationField;
    @FXML private TextField imageUrlField;
    @FXML private TextField plantIdField;
    @FXML private Label messageLabel;

    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearFieldsButton;

    @FXML
    public void initialize() {
        // Initialize Table Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("specimen_id"));
        plantIdColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        // Bind TableView to ViewModel
        specimenTable.setItems(viewModel.getSpecimenList());

        // Bind input fields to ViewModel properties
        locationField.textProperty().bindBidirectional(viewModel.locationProperty());
        imageUrlField.textProperty().bindBidirectional(viewModel.imageUrlProperty());
        plantIdField.textProperty().bindBidirectional(viewModel.plantIdProperty());

        // Bind selected row to ViewModel
        viewModel.selectedSpecimenProperty().bind(specimenTable.getSelectionModel().selectedItemProperty());

        // Bind buttons to ViewModel commands
        addButton.onActionProperty().bind(viewModel.getAddCommand());
        updateButton.onActionProperty().bind(viewModel.getUpdateCommand());
        deleteButton.onActionProperty().bind(viewModel.getDeleteCommand());
        clearFieldsButton.onActionProperty().bind(viewModel.getClearFieldsCommand());
    }
}
