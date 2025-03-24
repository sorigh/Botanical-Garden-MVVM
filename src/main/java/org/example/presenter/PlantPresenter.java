package org.example.presenter;

import org.example.model.Plant;
import org.example.model.repository.AbstractRepository;
import org.example.model.repository.PlantRepository;
import org.example.presenter.dto.PlantDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PlantPresenter {
    private PlantView view;
    private AbstractRepository<Plant> repository;

    public PlantPresenter(PlantView view) {
        this.view = view;
        this.repository = new PlantRepository();
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

    public void loadPlant() {
        List<Plant> products = repository.getTableContent();
        List<PlantDTO> dtos = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        view.displayPlants(dtos);
    }

    public void addPlant() {
        String name = view.getPlantName();
        String species = view.getPlantSpecies();
        String type = view.getPlantType();
        String carnivore = view.getPlantCarnivore();

        int carnivore_int;
        try {
            carnivore_int = Integer.parseInt(carnivore);
        } catch (NumberFormatException e) {
            view.showError("Invalid numeric input for Price.");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            view.showError("Plant name is required.");
            return;
        }

        Plant plant = new Plant();
        plant.setName(name);
        plant.setSpecies(species);
        plant.setType(type);
        plant.setCarnivore(carnivore_int);



        int success = repository.insert(plant);
        if (success!=0) {
            view.showMessage("Product added successfully.");
            loadPlant();
        } else {
            view.showError("Error adding product.");
        }
    }

    public void updatePlant() {
        PlantDTO selected = view.getSelectedProduct();
        if (selected == null) {
            view.showError("Please select a product to update.");
            return;
        }


        String name = view.getPlantName();
        String species = view.getPlantSpecies();
        String type = view.getPlantType();
        String carnivore = view.getPlantCarnivore();


        int carnivore_int;
        try {
            carnivore_int = Integer.parseInt(carnivore);
        } catch (NumberFormatException e) {
            view.showError("Invalid numeric input for Price.");
            return;
        }

        Plant plant = new Plant();
        plant.setPlant_id(selected.getPlant_id());
        plant.setName(name);
        plant.setSpecies(species);
        plant.setType(type);
        plant.setCarnivore(carnivore_int);

        int success = repository.update(plant);
        if (success!=0) {
            view.showMessage("Product updated successfully.");
            loadPlant();
        } else {
            view.showError("Error updating product.");
        }
    }

    public void deletePlant() {
        PlantDTO selected = view.getSelectedProduct();
        if (selected == null) {
            view.showError("Please select a product to delete.");
            return;
        }

        String name = view.getPlantName();
        String species = view.getPlantSpecies();
        String type = view.getPlantType();
        String carnivore = view.getPlantCarnivore();


        int carnivore_int;
        try {
            carnivore_int = Integer.parseInt(carnivore);
        } catch (NumberFormatException e) {
            view.showError("Invalid numeric input for Price.");
            return;
        }

        Plant plant = new Plant();
        plant.setPlant_id(selected.getPlant_id());
        plant.setName(name);
        plant.setSpecies(species);
        plant.setType(type);
        plant.setCarnivore(carnivore_int);

        int success = repository.delete(plant);
        if (success!=0) {
            view.showMessage("Product deleted successfully.");
            loadPlant();
        } else {
            view.showError("Error deleting product.");
        }
    }

}
