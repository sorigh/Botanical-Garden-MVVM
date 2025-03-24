package org.example.presenter;

import javafx.scene.layout.VBox;
import org.example.presenter.dto.PlantDTO;

import java.util.List;

public interface PlantView {
    void displayPlants(List<PlantDTO> plants);
    void displayPlant(PlantDTO plant);
    void showMessage(String message);
    void showError(String errorMessage);

    String getPlantName();
    String getPlantType();
    String getPlantSpecies();
    String getPlantCarnivore();

    PlantDTO getSelectedProduct();

    VBox getView();
}
