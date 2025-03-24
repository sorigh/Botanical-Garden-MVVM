package org.example.presenter.dto;


import org.example.model.Plant;

public class Maper {

    public static PlantDTO mapToDTO(Plant plant) {
        if (plant == null) {
            return null;
        }
        return new PlantDTO(
                plant.getPlant_id(),
                plant.getName(),
                plant.getType(),
                plant.getSpecies(),
                plant.getCarnivore()
        );
    }

    public static Plant mapToModel(PlantDTO dto) {
        if (dto == null) {
            return null;
        }
        Plant plant = new Plant();
        plant.setPlant_id(dto.getPlant_id());
        plant.setName(dto.getName());
        plant.setType(dto.getType());
        plant.setSpecies(dto.getSpecies());
        plant.setCarnivore(dto.getCarnivore());

        return plant;
    }

}
