package org.example.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.Specimen;
import org.example.model.repository.SpecimenRepository;
import org.example.viewmodel.commands.Command;
import org.example.viewmodel.dto.SpecimenDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SpecimenViewModel {
    private final SpecimenRepository repository;

    // Observable properties
    private final ObservableList<SpecimenDTO> specimenList = FXCollections.observableArrayList();
    private final StringProperty location = new SimpleStringProperty();
    private final StringProperty imageUrl = new SimpleStringProperty();
    private final StringProperty plantId = new SimpleStringProperty();
    private final ObjectProperty<SpecimenDTO> selectedSpecimen = new SimpleObjectProperty<>();

    // Commands
    private final Command updateCommand;
    private final Command addCommand;
    private final Command deleteCommand;
    private final Command clearFieldsCommand;

    public SpecimenViewModel() {
        this.repository = new SpecimenRepository();
        loadSpecimens();
        setupListeners();

        // Define the commands
        this.addCommand = new Command(this::addSpecimen);
        this.updateCommand = new Command(this::updateSpecimen);
        this.deleteCommand = new Command(this::deleteSpecimen);
        this.clearFieldsCommand = new Command(this::clearFields);

        selectedSpecimen.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                location.set(newValue.getLocation());
                imageUrl.set(newValue.getImageUrl());
                plantId.set(String.valueOf(newValue.getPlant_id()));
            }
        });
    }

    public void clearFields() {
        location.set("");
        imageUrl.set("");
        plantId.set("");
    }

    private void setupListeners() {
        selectedSpecimen.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                location.set(newValue.getLocation());
                imageUrl.set(newValue.getImageUrl());
                plantId.set(String.valueOf(newValue.getPlant_id()));
            }
        });
    }

    // Convert image URL to ImageView (or Image)
    public ImageView getImageForSpecimen(SpecimenDTO specimenDTO) {
        if (specimenDTO != null && specimenDTO.getImageUrl() != null && !specimenDTO.getImageUrl().isEmpty()) {
            try {
                // Construct the path relative to the 'resources' directory
                String imagePath = "/" + specimenDTO.getImageUrl();  // No need for leading slash here
                // Load image using ClassLoader to access resources in the classpath
                Image image = new Image(getClass().getResource(imagePath).toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100); // Set fixed width
                imageView.setFitHeight(100); // Set fixed height
                return imageView;
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }
        return null; // Return null if the image URL is invalid
    }


    public void loadSpecimens() {
        List<SpecimenDTO> dtos = repository.getTableContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        specimenList.setAll(dtos);
    }

    public void addSpecimen() {
        Specimen specimen = createSpecimenFromFields();
        if (specimen != null && repository.insert(specimen) != 0) {
            loadSpecimens();
            showAlert("Success", "Specimen added successfully!", AlertType.INFORMATION);
        } else {
            showAlert("Failure", "Failed to add specimen.", AlertType.ERROR);
        }
    }

    public void updateSpecimen() {
        if (selectedSpecimen.get() == null) return;

        Specimen specimen = createSpecimenFromFields();
        if (specimen != null) {
            specimen.setSpecimen_id(selectedSpecimen.get().getSpecimen_id());
            if (repository.update(specimen) != 0) {
                loadSpecimens();
                showAlert("Success", "Specimen updated successfully!", AlertType.INFORMATION);
            } else {
                showAlert("Failure", "Failed to update specimen.", AlertType.ERROR);
            }
        }
    }

    public void deleteSpecimen() {
        if (selectedSpecimen.get() != null && repository.deleteById(selectedSpecimen.get().getSpecimen_id()) != 0) {
            loadSpecimens();
            showAlert("Success", "Specimen deleted successfully!", AlertType.INFORMATION);
        } else {
            showAlert("Failure", "Failed to delete specimen.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Specimen createSpecimenFromFields() {
        try {
            return new Specimen(location.get(), imageUrl.get(), Integer.parseInt(plantId.get()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private SpecimenDTO convertToDTO(Specimen specimen) {
        return new SpecimenDTO(specimen.getSpecimen_id(), specimen.getPlant_id(), specimen.getLocation(), specimen.getImageUrl());
    }

    // Getters for View Binding
    public ObservableList<SpecimenDTO> getSpecimenList() { return specimenList; }
    public StringProperty locationProperty() { return location; }
    public StringProperty imageUrlProperty() { return imageUrl; }
    public StringProperty plantIdProperty() { return plantId; }
    public ObjectProperty<SpecimenDTO> selectedSpecimenProperty() { return selectedSpecimen; }

    // Expose commands for FXML binding
    public Command getUpdateCommand() {
        return updateCommand;
    }

    public Command getAddCommand() {
        return addCommand;
    }

    public Command getDeleteCommand() {
        return deleteCommand;
    }

    public Command getClearFieldsCommand() {
        return clearFieldsCommand;
    }
}
