package org.example.presenter;

import javafx.scene.layout.VBox;
import org.example.presenter.dto.SpecimenDTO;

import java.util.List;

public interface SpecimenView {
    void displaySpecimens(List<SpecimenDTO> plants);
    void displaySpecimen(SpecimenDTO plant);
    void showMessage(String message);
    void showError(String errorMessage);
    void setImageColumnFactory();
    String getSpecimenLocation();
    String getSpecimenImageUrl();
    int getSpecimenPlantId();
    SpecimenDTO getSelectedSpecimen();

    VBox getView();
}
