package org.example.model.repository;


import org.example.model.Plant;
import org.example.model.Specimen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PlantRepository extends AbstractRepository<Plant> {

    public PlantRepository(){
        super();
    }

    public List<Specimen> getSpecimensByPlantId(int plantId) {
        List<Specimen> specimenList = new ArrayList<>();
        String query = "SELECT * FROM Specimen WHERE plant_id = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, plantId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Specimen specimen = new Specimen();
                    specimen.setSpecimen_id(resultSet.getInt("specimen_id"));
                    specimen.setPlant_id(resultSet.getInt("plant_id"));
                    specimen.setLocation(resultSet.getString("location"));
                    specimen.setImageUrl(resultSet.getString("imageUrl"));
                    specimenList.add(specimen);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error fetching specimens by plant_id: " + e.getMessage());
        }

        return specimenList;
    }



}

