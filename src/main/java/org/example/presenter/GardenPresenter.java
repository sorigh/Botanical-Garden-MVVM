package org.example.presenter;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.Plant;
import org.example.model.Specimen;
import org.example.model.repository.PlantRepository;
import org.example.model.repository.SpecimenRepository;
import org.example.presenter.dto.Maper;
import org.example.presenter.dto.SpecimenDTO;
import org.example.view.GardenViewImpl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class GardenViewPresenter {
    private GardenView view;
    private PlantRepository plantRepository;
    private SpecimenRepository specimenRepository;

    public GardenViewPresenter(GardenView view) {
        this.view = view;
        this.plantRepository = new PlantRepository();
        this.specimenRepository = new SpecimenRepository();
    }

    public void loadAllPlants() {
        List<Plant> plants = plantRepository.getTableContent();
        view.displayPlants(Maper.mapToDTO(plants));
    }

    public void loadAllSpecimens() {
        List<Specimen> specimens = specimenRepository.getTableContent();
        view.displaySpecimens(Maper.mapToDTOSpecimen(specimens));
    }

    public void filterPlants(String type, boolean isCarnivorous) {
        List<Plant> filteredPlants = plantRepository.getTableContent().stream()
                .filter(p -> (type.equals("No Filter") || p.getType().equals(type)))
                .filter(p -> (!isCarnivorous || p.getCarnivore() == 1))
                .collect(Collectors.toList());

        view.displayPlants(Maper.mapToDTO(filteredPlants));
    }

    public void exportPlantsToCSV() {
        List<Plant> plants = plantRepository.getTableContent();
        PlantExporter.exportToCSV(plants, "plants.csv");
    }

    public List<String> getAvailablePlantTypes() {
        return plantRepository.getTableContent().stream()
                .map(Plant::getType)
                .distinct()
                .collect(Collectors.toList());
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

}
