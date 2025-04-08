

package org.example.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.example.model.Plant;
import org.example.model.repository.PlantRepository;
import org.example.viewmodel.dto.PlantDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PlantViewModel {
    private final PlantRepository repository;

    // Observable properties
    private final ObservableList<PlantDTO> plantList = FXCollections.observableArrayList();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty species = new SimpleStringProperty();
    private final StringProperty carnivore = new SimpleStringProperty();
    private final ObjectProperty<PlantDTO> selectedPlant = new SimpleObjectProperty<>();

    // Commands
    private final Command updateCommand;
    private final Command addCommand;
    //private final Command updateCommand;
    private final Command deleteCommand;
    private final Command clearFieldsCommand;

    public PlantViewModel() {
        this.repository = new PlantRepository();
        loadPlants();
        setupListeners();

        // Define the commands
        this.addCommand = new Command(this::addPlant);
        this.updateCommand = new Command(this::updatePlant);
        this.deleteCommand = new Command(this::deletePlant);
        this.clearFieldsCommand = new Command(this::clearFields);

        selectedPlant.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                name.set(newValue.getName());
                type.set(newValue.getType());
                species.set(newValue.getSpecies());
                carnivore.set(String.valueOf(newValue.getCarnivore()));
            }
        });

    }

    public void clearFields() {
        name.set("");
        type.set("");
        species.set("");
        carnivore.set("");
    }

    private void setupListeners() {
        selectedPlant.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                name.set(newValue.getName());
                type.set(newValue.getType());
                species.set(newValue.getSpecies());
                carnivore.set(String.valueOf(newValue.getCarnivore()));
            }
        });
    }

    public void loadPlants() {
        List<PlantDTO> dtos = repository.getTableContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        plantList.setAll(dtos);
    }

    public void addPlant() {
        Plant plant = createPlantFromFields();
        if (plant != null && repository.insert(plant) != 0) {
            loadPlants();
            showAlert("Success", "Plant added successfully!", AlertType.INFORMATION);
        } else {
            showAlert("Failure", "Failed to add plant.", AlertType.ERROR);
        }
    }

    public void updatePlant() {
        if (selectedPlant.get() == null) return;

        Plant plant = createPlantFromFields();
        if (plant != null) {
            plant.setPlant_id(selectedPlant.get().getPlant_id());
            if (repository.update(plant) != 0) {
                loadPlants();
                showAlert("Success", "Plant updated successfully!", AlertType.INFORMATION);
            } else {
                showAlert("Failure", "Failed to update plant.", AlertType.ERROR);
            }
        }
    }



    public void deletePlant() {
        if (selectedPlant.get() != null && repository.deleteById(selectedPlant.get().getPlant_id()) != 0) {
            loadPlants();
            showAlert("Success", "Plant deleted successfully!", AlertType.INFORMATION);
        } else {
            showAlert("Failure", "Failed to delete plant.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Plant createPlantFromFields() {
        try {
            return new Plant(name.get(), type.get(), species.get(), Integer.parseInt(carnivore.get()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private PlantDTO convertToDTO(Plant plant) {
        return new PlantDTO(plant.getPlant_id(), plant.getName(), plant.getSpecies(), plant.getType(), plant.getCarnivore());
    }

    // Getters for View Binding
    public ObservableList<PlantDTO> getPlantList() { return plantList; }
    public StringProperty nameProperty() { return name; }
    public StringProperty typeProperty() { return type; }
    public StringProperty speciesProperty() { return species; }
    public StringProperty carnivoreProperty() { return carnivore; }
    public ObjectProperty<PlantDTO> selectedPlantProperty() { return selectedPlant; }

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

