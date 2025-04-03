package org.example.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.example.presenter.GardenView;
import org.example.presenter.GardenPresenter;
import org.example.presenter.dto.PlantDTO;
import org.example.presenter.dto.SpecimenDTO;

import java.util.List;


public class GardenViewImpl implements GardenView {


    private GardenPresenter presenter;

    private TableView<PlantDTO> plantTable;
    private TableView<SpecimenDTO> specimenTable;
    private TableColumn<SpecimenDTO, String> imageColumn;
    private Label messageLabel;

    private ComboBox<String> filterTypeBox;
    private CheckBox filterCarnivorousCheck;
    private Button filterButton;
    private Button exportButton;
    private Button exportDocButton;

    private TextField specimenSearchField; // Search field for specimens
    private List<SpecimenDTO> allSpecimens;

    private VBox rootLayout;

    public GardenViewImpl() {
        presenter = new GardenPresenter(this);
        initView();
        presenter.loadAllPlants();
        presenter.loadSpecimens();
        populateDropdowns();
    }

    private void initView() {
        plantTable = new TableView<>();
        specimenTable = new TableView<>();
        messageLabel = new Label();

        // Plant Table Columns
        TableColumn<PlantDTO, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        idColumn.setPrefWidth(50);

        TableColumn<PlantDTO, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);

        TableColumn<PlantDTO, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(200);

        TableColumn<PlantDTO, String> carnivoreColumn = new TableColumn<>("Carnivorous");
        carnivoreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((cellData.getValue().getCarnivore() == 1) ? "Yes" : "No"));
        carnivoreColumn.setPrefWidth(100);

        plantTable.getColumns().addAll(idColumn, nameColumn, typeColumn, carnivoreColumn);
        plantTable.setPrefWidth(600);

        // Specimen Table Columns
        TableColumn<SpecimenDTO, Integer> idSpecimenColumn = new TableColumn<>("Id");
        idSpecimenColumn.setCellValueFactory(new PropertyValueFactory<>("specimen_id"));
        idSpecimenColumn.setPrefWidth(50);

        TableColumn<SpecimenDTO, Integer> plantIdColumn = new TableColumn<>("Plant Id");
        plantIdColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        plantIdColumn.setPrefWidth(100);

        TableColumn<SpecimenDTO, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.setPrefWidth(200);

        imageColumn = new TableColumn<>("Image");

        specimenTable.getColumns().addAll(idSpecimenColumn, plantIdColumn, locationColumn, imageColumn);
        specimenTable.setPrefWidth(600);

        setImageColumnFactory(); // Set the image column logic in presenter

        // Filter Controls
        filterTypeBox = new ComboBox<>();
        filterTypeBox.setPromptText("Select Type");

        filterCarnivorousCheck = new CheckBox("Carnivorous");

        filterButton = new Button("Filter Plants");
        filterButton.setOnAction(e -> {
            String selectedType = filterTypeBox.getValue();
            boolean isCarnivorous = filterCarnivorousCheck.isSelected();
            presenter.filterPlants(selectedType, isCarnivorous);
        });

        exportButton = new Button("Export Plants to CSV");
        exportButton.setOnAction(e -> presenter.exportPlantsToCSV());

        exportDocButton = new Button("Export Plants to DOC");
        exportDocButton.setOnAction(e -> presenter.exportPlantsToDOC());

        specimenSearchField = new TextField();
        specimenSearchField.setPromptText("Search specimens...");
        specimenSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchSpecimensInTable(newValue);
        });

        // VBox for Filters
        VBox filterBox = new VBox(10,
                new Label("Filter Plants"), filterTypeBox, filterCarnivorousCheck, filterButton);
        filterBox.setPadding(new javafx.geometry.Insets(10, 0, 60, 0)); // Bottom padding to space it properly

        // Align search field with Specimen Table
        HBox specimenSearchBox = new HBox(10, new Label("Search Specimens:"), specimenSearchField);
        specimenSearchBox.setPadding(new javafx.geometry.Insets(20, 0, 0, 0)); // Push it down to align

        VBox rightControls = new VBox(20, filterBox, specimenSearchBox, exportButton,exportDocButton);
        rightControls.setPadding(new javafx.geometry.Insets(10));

        // Tables layout
        VBox tableBox = new VBox(10, plantTable, specimenTable);

        // Layout: Tables on Left, Controls on Right
        HBox mainLayout = new HBox(20, tableBox, rightControls);
        mainLayout.setPadding(new javafx.geometry.Insets(10));

        rootLayout = new VBox(10, mainLayout, messageLabel);
        rootLayout.setPadding(new javafx.geometry.Insets(10));
    }



    private void populateDropdowns() {
        filterTypeBox.getItems().add("No Filter");
        filterTypeBox.getItems().addAll(presenter.getAvailablePlantTypes());
        filterTypeBox.setValue("No Filter"); // Default selection
    }

    public VBox getView() {
        return rootLayout;
    }
    @Override
    public void setImageColumnFactory() {
        presenter.setImageColumnFactory(imageColumn);
    }
    @Override
    public void displayPlants(List<PlantDTO> plants) {
        plantTable.getItems().clear();
        plantTable.getItems().addAll(plants);
    }

    @Override
    public void displaySpecimens(List<SpecimenDTO> specimens) {
        this.allSpecimens = specimens;
        specimenTable.getItems().clear();
        specimenTable.getItems().addAll(specimens);
    }
    @Override
    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    @Override
    public void showError(String errorMessage) {
        messageLabel.setText(errorMessage);
    }

    private void searchSpecimensInTable(String query) {
        List<SpecimenDTO> filteredSpecimens = presenter.filterSpecimens(query);
        specimenTable.setItems(FXCollections.observableArrayList(filteredSpecimens));
    }
}
