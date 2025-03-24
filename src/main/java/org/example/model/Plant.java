package org.example.model;

public class Plant {
	private int plant_id;
	private String name;
	private String type;
	private String species;
	private Integer carnivore;


	public Plant(){}


	public Plant(String name, String type, String species, int carnivore) {
		super();
		this.name = name;
		this.type = type;
		this.species = species;
		this.carnivore = carnivore;
	}

	public int getPlant_id() {
		return plant_id;
	}

	public void setPlant_id(int plant_id) {
		this.plant_id = plant_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}


	public int getCarnivore() {
		return carnivore;
	}

	public void setCarnivore(int carnivore) {
		this.carnivore = carnivore;
	}

	@Override
	public String toString() {
		return "Plant{" +
				"plant_id=" + plant_id +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", species='" + species + '\'' +
				", carnivore=" + carnivore +
				'}';
	}
}
