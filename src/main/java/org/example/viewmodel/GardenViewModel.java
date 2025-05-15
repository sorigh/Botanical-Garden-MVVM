package org.example.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.model.Plant;
import org.example.model.repository.PlantRepository;
import org.example.model.repository.SpecimenRepository;
import org.example.viewmodel.commands.Command;
import org.example.viewmodel.dto.PlantDTO;
import org.example.viewmodel.dto.SpecimenDTO;

import java.util.List;
import java.util.stream.Collectors;

public class GardenViewModel {

    private final PlantRepository plantRepository = new PlantRepository();
    private final SpecimenRepository specimenRepository = new SpecimenRepository();

    // Observable properties
    private final ObservableList<PlantDTO> plantList = FXCollections.observableArrayList();
    private final ObservableList<SpecimenDTO> specimenList = FXCollections.observableArrayList();
    private final StringProperty selectedType = new SimpleStringProperty("No Filter");
    private final BooleanProperty isCarnivorous = new SimpleBooleanProperty(false);
    private final StringProperty searchQuery = new SimpleStringProperty();
    private final ObjectProperty<PlantDTO> selectedPlant = new SimpleObjectProperty<>();

    // Commands
    private final Command filterCommand;
    private final Command exportCommand;
    private final Command exportDocCommand;
    private final Command searchCommand;  // New command for search functionality

    public GardenViewModel() {
        loadAllPlants();
        loadSpecimens();

        // Define the commands
        this.filterCommand = new Command(this::filterPlants);
        this.exportCommand = new Command(this::exportPlantsToCSV);
        this.exportDocCommand = new Command(this::exportPlantsToDOC);
        this.searchCommand = new Command(this::searchSpecimens);  // Bind the new search command
    }

    public ObservableList<PlantDTO> getPlantList() {
        return plantList;
    }
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


    public ObservableList<SpecimenDTO> getSpecimenList() {
        return specimenList;
    }

    public StringProperty selectedTypeProperty() {
        return selectedType;
    }

    public BooleanProperty isCarnivorousProperty() {
        return isCarnivorous;
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }

    public ObjectProperty<PlantDTO> selectedPlantProperty() {
        return selectedPlant;
    }

    // Load data methods
    public void loadAllPlants() {
        List<PlantDTO> plants = plantRepository.getTableContent().stream()
                .map(plant -> new PlantDTO(plant.getPlant_id(), plant.getName(), plant.getType(), plant.getSpecies(), plant.getCarnivore()))
                .collect(Collectors.toList());
        plantList.setAll(plants);
    }

    public void loadSpecimens() {
        List<SpecimenDTO> specimens = specimenRepository.getTableContent().stream()
                .map(specimen -> new SpecimenDTO(specimen.getSpecimen_id(), specimen.getPlant_id(), specimen.getLocation(), specimen.getImageUrl()))
                .collect(Collectors.toList());
        specimenList.setAll(specimens);
    }

    // Command actions
    public void filterPlants() {
        String type = selectedType.get();  // Get selected plant type
        boolean carnivorous = isCarnivorous.get();  // Get carnivorous checkbox state

        // Apply filters on the plants list
        List<PlantDTO> filtered = plantRepository.getTableContent().stream()
                .filter(p -> {
                    // Apply type filter (default to "No Filter" if no type selected)
                    boolean typeMatches = "No Filter".equals(type) || p.getType().equalsIgnoreCase(type);

                    // Apply carnivorous filter
                    boolean carnivoreMatches = !carnivorous || p.getCarnivore() == 1;

                    return typeMatches && carnivoreMatches;  // Only include plants that match both conditions
                })
                .map(p -> new PlantDTO(p.getPlant_id(), p.getName(), p.getType(), p.getSpecies(), p.getCarnivore()))
                .collect(Collectors.toList());

        // Update the ObservableList with the filtered plants
        plantList.setAll(filtered);
    }

    public void exportPlantsToCSV() {
        List<Plant> plants = plantRepository.getTableContent();
        PlantExporter.exportToCSV(plants, "plants.csv");

    }

    public void exportPlantsToDOC() {
        List<Plant> plants = plantRepository.getTableContent();
        PlantExporter.exportToDOC(plants, "plants.docx");
    }

    // New search functionality
    public void searchSpecimens() {
        String query = searchQuery.get().toLowerCase();  // Get search query
        List<SpecimenDTO> filtered = specimenRepository.getTableContent().stream()
                .filter(specimen -> specimen.getLocation().toLowerCase().contains(query))  // Filter by location
                .map(specimen -> new SpecimenDTO(specimen.getSpecimen_id(), specimen.getPlant_id(), specimen.getLocation(), specimen.getImageUrl()))
                .collect(Collectors.toList());
        specimenList.setAll(filtered);  // Update specimen list with search results
    }

    // Expose commands for FXML binding
    public Command getFilterCommand() {
        return filterCommand;
    }

    public Command getExportCommand() {
        return exportCommand;
    }

    public Command getExportDocCommand() {
        return exportDocCommand;
    }

    public Command getSearchCommand() {
        return searchCommand;
    }

    public List<String> getAvailablePlantTypes() {
        return plantRepository.getTableContent().stream()
                .map(Plant::getType)
                .distinct()
                .collect(Collectors.toList());
    }
}
