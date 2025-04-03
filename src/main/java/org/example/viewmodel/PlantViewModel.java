package org.example.presenter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.model.Plant;
import org.example.model.repository.PlantRepository;
import org.example.presenter.dto.PlantDTO;

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

    public PlantViewModel() {
        this.repository = new PlantRepository();
        loadPlants();

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
        Plant plant = getPlantFromFields();
        if (plant == null) return;

        if (repository.insert(plant) != 0) {
            loadPlants();
        }
    }

    public void updatePlant() {
        if (selectedPlant.get() == null) return;

        Plant plant = getPlantFromFields();
        if (plant == null) return;

        plant.setPlant_id(selectedPlant.get().getPlant_id());

        if (repository.update(plant) != 0) {
            loadPlants();
        }
    }

    public void deletePlant() {
        if (selectedPlant.get() == null) return;

        if (repository.deleteById(selectedPlant.get().getPlant_id()) != 0) {
            loadPlants();
        }
    }

    private Plant getPlantFromFields() {
        try {
            return new Plant(name.get(), type.get(), species.get(), Integer.parseInt(carnivore.get()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private PlantDTO convertToDTO(Plant plant) {
        return new PlantDTO(
                plant.getPlant_id(),
                plant.getName(),
                plant.getSpecies(),
                plant.getType(),
                plant.getCarnivore()
        );
    }

    // Getters for View Binding
    public ObservableList<PlantDTO> getPlantList() { return plantList; }
    public StringProperty nameProperty() { return name; }
    public StringProperty typeProperty() { return type; }
    public StringProperty speciesProperty() { return species; }
    public StringProperty carnivoreProperty() { return carnivore; }
    public ObjectProperty<PlantDTO> selectedPlantProperty() { return selectedPlant; }
}
