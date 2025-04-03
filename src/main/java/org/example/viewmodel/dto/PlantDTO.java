package org.example.viewmodel.dto;

public class PlantDTO {
    private final int plant_id;
    private final String name;
    private final String type;
    private final String species;

    private final int carnivore;

    public PlantDTO(int plant_id, String name, String species, String type, Integer carnivore) {
        this.plant_id = plant_id;
        this.name = name;
        this.species = species;
        this.type = type;
        this.carnivore = carnivore;
    }

    public int getPlant_id() {
        return plant_id;
    }

    public String getName() {
        return name;
    }


    public String getType() {
        return type;
    }


    public String getSpecies() {
        return species;
    }


    public int getCarnivore() {
        return carnivore;
    }


    @Override
    public String toString() {
        return "PlantDTO{" +
                "plant_id=" + plant_id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", species='" + species + '\'' +
                ", carnivore=" + carnivore +
                '}';
    }
}
