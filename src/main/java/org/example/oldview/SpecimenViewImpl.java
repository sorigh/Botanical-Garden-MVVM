package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.presenter.SpecimenPresenter;
import org.example.presenter.SpecimenView;
import org.example.presenter.dto.SpecimenDTO;

import java.util.List;

@SuppressWarnings("ALL")
public class SpecimenViewImpl implements SpecimenView {
    private final SpecimenPresenter presenter;
    private TableView<SpecimenDTO> specimenTable;
    private TableColumn<SpecimenDTO, String> imageColumn;
    private Label messageLabel;

    private TextField locationField;
    private TextField imageUrlField;
    private TextField plantIdField;

    private VBox rootLayout;

    public SpecimenViewImpl() {
        presenter = new SpecimenPresenter(this);
        initView();
        presenter.loadSpecimens();

    }

    public void initView() {
        specimenTable = new TableView<>();
        specimenTable.setPrefWidth(550);
        messageLabel = new Label();

        TableColumn<SpecimenDTO, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("specimen_id"));
        idColumn.setPrefWidth(50);

        TableColumn<SpecimenDTO, Integer> plantIdColumn = new TableColumn<>("Plant Id");
        plantIdColumn.setCellValueFactory(new PropertyValueFactory<>("plant_id"));
        plantIdColumn.setPrefWidth(50);

        TableColumn<SpecimenDTO, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.setPrefWidth(300);

        imageColumn = new TableColumn<>("Image");

        specimenTable.getColumns().addAll(idColumn, plantIdColumn, locationColumn, imageColumn);

        setImageColumnFactory(); // Set the image column logic in presenter

        specimenTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            presenter.selectionUpdate(newSel,locationField,imageUrlField,plantIdField);
        });

        locationField = new TextField();
        locationField.setPromptText("Location");
        imageUrlField = new TextField();
        imageUrlField.setPromptText("Image URL");
        plantIdField = new TextField();
        plantIdField.setPromptText("Plant ID");

        Button addSpecimenButton = new Button("Add Specimen");
        addSpecimenButton.setOnAction(e -> {
            presenter.addSpecimen();
            clearFields();
        });

        Button updateSpecimenButton = new Button("Update Specimen");
        updateSpecimenButton.setOnAction(e -> {
            presenter.updateSpecimen();
            clearFields();
        });

        Button deleteSpecimenButton = new Button("Delete Specimen");
        deleteSpecimenButton.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Deletion");
            confirm.setHeaderText("Delete Specimen");
            confirm.setContentText("Are you sure you want to delete this specimen?");
            confirm.showAndWait().ifPresent(resp -> {
                if (resp == ButtonType.OK) {
                    presenter.deleteSpecimen();
                    clearFields();
                }
            });
        });

        Button clearFieldsButton = new Button("Clear fields");
        clearFieldsButton.setOnAction(e -> clearFields());

        VBox buttonBox = new VBox(10, addSpecimenButton, updateSpecimenButton, deleteSpecimenButton, clearFieldsButton);
        buttonBox.setPadding(new Insets(10));

        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(10));
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(new Label("Location:"), 0, 0);
        inputGrid.add(locationField, 1, 0);
        inputGrid.add(new Label("Image URL:"), 0, 1);
        inputGrid.add(imageUrlField, 1, 1);
        inputGrid.add(new Label("Plant ID:"), 0, 2);
        inputGrid.add(plantIdField, 1, 2);

        // Main layout: Wider Table + Input Fields + Buttons (Right Side)
        HBox mainLayout = new HBox(20, specimenTable, inputGrid, buttonBox);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setPrefWidth(800); // Ensure layout takes full space

        rootLayout = new VBox(10, mainLayout, messageLabel);
        rootLayout.setPadding(new Insets(10));
    }


    public VBox getView() {
        return rootLayout;
    }

    private void clearFields() {
        locationField.clear();
        imageUrlField.clear();
        plantIdField.clear();
    }

    @Override
    public void displaySpecimens(List<SpecimenDTO> specimens) {
        specimenTable.getItems().clear();
        specimenTable.getItems().addAll(specimens);
    }

    @Override
    public void displaySpecimen(SpecimenDTO plant) {

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
    public void setImageColumnFactory() {
        presenter.setImageColumnFactory(imageColumn);
    }

    @Override
    public String getSpecimenLocation() {
        return locationField.getText();
    }

    @Override
    public String getSpecimenImageUrl() {
        return imageUrlField.getText();
    }

    @Override
    public int getSpecimenPlantId() {
        return Integer.parseInt(plantIdField.getText());
    }

    @Override
    public SpecimenDTO getSelectedSpecimen() {
        return specimenTable.getSelectionModel().getSelectedItem();
    }
}
