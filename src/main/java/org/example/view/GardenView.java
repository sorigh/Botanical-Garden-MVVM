package org.example.view;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.viewmodel.GardenViewModel;
import org.example.viewmodel.dto.PlantDTO;
import org.example.viewmodel.dto.SpecimenDTO;

public class GardenView {
    private final GardenViewModel viewModel = new GardenViewModel();

    @FXML private TableView<PlantDTO> plantTable;
    @FXML private TableView<SpecimenDTO> specimenTable;

    @FXML private TableColumn<PlantDTO, Integer> idColumn;
    @FXML private TableColumn<PlantDTO, String> nameColumn;
    @FXML private TableColumn<PlantDTO, String> typeColumn;
    @FXML private TableColumn<PlantDTO, String> speciesColumn;
    @FXML private TableColumn<PlantDTO, String> carnivoreColumn;

    @FXML private TableColumn<SpecimenDTO, Integer> specimenIdColumn;
    @FXML private TableColumn<SpecimenDTO, Integer> plantIdColumn;
    @FXML private TableColumn<SpecimenDTO, String> locationColumn;
    @FXML private TableColumn<SpecimenDTO, String> imageColumn;

    @FXML private ComboBox<String> filterTypeBox;
    @FXML private CheckBox filterCarnivorousCheck;
    @FXML private Button filterButton;
    @FXML private Button exportButton;
    @FXML private Button exportDocButton;
    @FXML private Button specimenSearchButton;

    @FXML private TextField specimenSearchField;
    @FXML private Label messageLabel;

    @FXML private VBox rootLayout;

    @FXML
    public void initialize() {
        // Initialize Plant Table Columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));  // This is the type of plant
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));  // This is the species of the plant
        carnivoreColumn.setCellValueFactory(new PropertyValueFactory<>("carnivore"));

        // Initialize Specimen Table Columns
        specimenIdColumn.setCellValueFactory(new PropertyValueFactory<>("specimen_id"));
        plantIdColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        // Bind TableViews to ViewModel's ObservableLists
        plantTable.setItems(viewModel.getPlantList());
        specimenTable.setItems(viewModel.getSpecimenList());

        // Bind ComboBox and CheckBox to ViewModel
        filterTypeBox.valueProperty().bindBidirectional(viewModel.selectedTypeProperty());
        filterCarnivorousCheck.selectedProperty().bindBidirectional(viewModel.isCarnivorousProperty());

        filterTypeBox.getItems().add("No Filter");
        filterTypeBox.getItems().addAll(viewModel.getAvailablePlantTypes());  // Assuming getAvailablePlantTypes() is available in your ViewModel
        filterTypeBox.setValue("No Filter");  // Default selection

        // Bind search field to ViewModel
        specimenSearchField.textProperty().bindBidirectional(viewModel.searchQueryProperty());

        // Bind buttons to ViewModel commands
        filterButton.onActionProperty().bind(viewModel.getFilterCommand());
        exportButton.onActionProperty().bind(viewModel.getExportCommand());
        exportDocButton.onActionProperty().bind(viewModel.getExportDocCommand());

        // Bind search button to searchCommand
        specimenSearchButton.onActionProperty().bind(viewModel.getSearchCommand());  // Execute the search command
    }

}
