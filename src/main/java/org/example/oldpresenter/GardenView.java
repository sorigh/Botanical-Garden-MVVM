package org.example.oldpresenter;

import javafx.scene.layout.VBox;
import org.example.viewmodel.dto.PlantDTO;
import org.example.viewmodel.dto.SpecimenDTO;

import java.util.List;

public interface GardenView {

    void displaySpecimens(List<SpecimenDTO> plants);

    void displayPlants(List<PlantDTO> plants);

    void showMessage(String message);
    void showError(String errorMessage);
    void setImageColumnFactory();

    VBox getView();
}
