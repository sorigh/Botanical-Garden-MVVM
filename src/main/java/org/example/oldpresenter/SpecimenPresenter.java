package org.example.presenter;


import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.Specimen;
import org.example.model.repository.SpecimenRepository;
import org.example.presenter.dto.SpecimenDTO;


import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class SpecimenPresenter {
    private final SpecimenView view;
    private final SpecimenRepository repository;

    public SpecimenPresenter(SpecimenView view) {
        this.view = view;
        this.repository = new SpecimenRepository();
    }

    public void loadSpecimens() {
        List<SpecimenDTO> specimens = repository.getTableContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        view.displaySpecimens(specimens);
    }

    public void addSpecimen() {
        String location = view.getSpecimenLocation();
        String imageUrl = view.getSpecimenImageUrl();
        int plantId;
        try {
            plantId = view.getSpecimenPlantId();
        } catch (NumberFormatException e) {
            view.showError("Invalid Plant ID");
            return;
        }

        Specimen specimen = new Specimen(location, imageUrl, plantId);
        repository.insert(specimen);
        view.showMessage("Specimen added successfully");
        loadSpecimens();
    }

    public void updateSpecimen() {
        SpecimenDTO selected = view.getSelectedSpecimen();
        if (selected == null) {
            view.showError("No specimen selected");
            return;
        }

        String location = view.getSpecimenLocation();
        String imageUrl = view.getSpecimenImageUrl();
        int plantId;
        try {
            plantId = view.getSpecimenPlantId();
        } catch (NumberFormatException e) {
            view.showError("Invalid Plant ID");
            return;
        }

        Specimen specimen = new Specimen(location, imageUrl, plantId);
        specimen.setSpecimen_id(selected.getSpecimen_id());
        repository.update(specimen);
        view.showMessage("Specimen updated successfully");
        loadSpecimens();
    }

    public void deleteSpecimen() {
        SpecimenDTO selected = view.getSelectedSpecimen();
        if (selected == null) {
            view.showError("No specimen selected");
            return;
        }

        repository.deleteById(selected.getSpecimen_id());
        view.showMessage("Specimen deleted successfully");
        loadSpecimens();
    }

    private SpecimenDTO toDTO(Specimen specimen) {
        return new SpecimenDTO(specimen.getSpecimen_id(), specimen.getPlant_id(), specimen.getLocation(), specimen.getImageUrl());
    }

    public void setImageColumnFactory(TableColumn<SpecimenDTO, String> imageColumn) {
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        imageColumn.setCellFactory(column -> new TableCell<SpecimenDTO, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        // Construiește path-ul absolut pentru fișier
                        File file = new File(imageUrl);
                        String absolutePath = file.toURI().toString();

                        // Încarcă imaginea și o afișează
                        Image image = new Image(absolutePath, 100, 100, true, true);
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
    }


    public void selectionUpdate(SpecimenDTO newSel, TextField locationField, TextField imageUrlField, TextField plantIdField) {
        if (newSel != null) {
            locationField.setText(newSel.getLocation());
            imageUrlField.setText(newSel.getImageUrl());
            plantIdField.setText(String.valueOf(newSel.getPlant_id()));
        }

    }
}

