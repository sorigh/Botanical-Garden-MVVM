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
import org.example.presenter.dto.Mapper;
import org.example.presenter.dto.SpecimenDTO;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class GardenPresenter {
    private GardenView view;
    private PlantRepository plantRepository;
    private SpecimenRepository specimenRepository;

    public GardenPresenter(GardenView view) {
        this.view = view;
        this.plantRepository = new PlantRepository();
        this.specimenRepository = new SpecimenRepository();
    }

    public void loadAllPlants() {
        List<Plant> plants = plantRepository.getTableContent();
        view.displayPlants(Mapper.mapToDTO(plants));
    }


    public void loadSpecimens() {
        List<SpecimenDTO> specimens = specimenRepository.getTableContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        view.displaySpecimens(specimens);
    }
    private SpecimenDTO toDTO(Specimen specimen) {
        return new SpecimenDTO(specimen.getSpecimen_id(), specimen.getPlant_id(), specimen.getLocation(), specimen.getImageUrl());
    }


    public void filterPlants(String type, boolean isCarnivorous) {
        List<Plant> filteredPlants = plantRepository.getTableContent().stream()
                .filter(p -> (type.equals("No Filter") || p.getType().equals(type)))
                .filter(p -> (!isCarnivorous || p.getCarnivore() == 1))
                .collect(Collectors.toList());

        view.displayPlants(Mapper.mapToDTO(filteredPlants));
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

    public List<SpecimenDTO> filterSpecimens(String query) {
        List<SpecimenDTO> allSpecimens = specimenRepository.getTableContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        if (query == null || query.isEmpty()) {
            return allSpecimens;
        }

        return allSpecimens.stream()
                .filter(s -> String.valueOf(s.getSpecimen_id()).contains(query) ||
                        String.valueOf(s.getPlant_id()).contains(query) ||
                        s.getLocation().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void exportPlantsToDOC() {
        String filePath = "plants_list.docx"; // Example file path
        List<Plant> plants = plantRepository.getTableContent();
        PlantExporter.exportToDOC(plants, filePath);
    }
}
