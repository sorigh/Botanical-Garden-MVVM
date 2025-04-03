package org.example.viewmodel.dto;

import org.example.model.Plant;
import org.example.model.Specimen;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    public static PlantDTO mapToDTO(Plant plant) {
        return new PlantDTO(
                plant.getPlant_id(),
                plant.getName(),
                plant.getType(),
                plant.getSpecies(),
                plant.getCarnivore()
        );
    }

    public static List<PlantDTO> mapToDTO(List<Plant> plants) {
        return plants.stream()
                .map(Mapper::mapToDTO)
                .collect(Collectors.toList());
    }

    public static Plant mapToModel(PlantDTO plantDTO) {
        return new Plant(
                plantDTO.getPlant_id(),
                plantDTO.getName(),
                plantDTO.getType(),
                plantDTO.getSpecies(),
                plantDTO.getCarnivore()
        );
    }

    public static List<Plant> mapToModel(List<PlantDTO> plantDTOs) {
        return plantDTOs.stream()
                .map(Mapper::mapToModel)
                .collect(Collectors.toList());
    }

    public static List<SpecimenDTO> mapToDTOSpecimen(List<Specimen> specimens) {
        return specimens.stream()
                .map(s -> new SpecimenDTO(s.getSpecimen_id(), s.getPlant_id(), s.getLocation()))
                .collect(Collectors.toList());
    }
}
