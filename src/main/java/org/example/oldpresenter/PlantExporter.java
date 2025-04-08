package org.example.oldpresenter;


import org.apache.poi.xwpf.usermodel.*;
import org.example.model.Plant;
import org.example.model.Specimen;
import org.example.model.repository.PlantRepository;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class PlantExporter {

    public static void exportToCSV(List<Plant> plants, String filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("ID,Name,Specimens\n");
            PlantRepository plantRepository = new PlantRepository();

            for (Plant plant : plants) {
                List<Specimen> specimenList = plantRepository.getSpecimensByPlantId(plant.getPlant_id());

                String specimenStr = specimenList.stream()
                        .map(Specimen::getLocation)
                        .collect(Collectors.joining(" | ")); // Separăm cu "|"

                writer.write(plant.getPlant_id() + "," +
                        plant.getName() + "," +
                        specimenStr + "," + "\n");
            }
            System.out.println("Plants exported successfully to CSV: " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting plants to CSV: " + e.getMessage());
        }
    }

    public static void exportToDOC(List<Plant> plants, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            XWPFDocument document = new XWPFDocument();
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Plant List - Botanical Garden");
            titleRun.setBold(true);
            titleRun.setFontSize(14);

            XWPFTable table = document.createTable();
            table.setWidth("100%");

            XWPFTableRow header = table.getRow(0);
            header.getCell(0).setText("ID");
            header.addNewTableCell().setText("Name");
            header.addNewTableCell().setText("Specimens");

            PlantRepository plantRepository = new PlantRepository();

            for (Plant plant : plants) {
                List<Specimen> specimenList = plantRepository.getSpecimensByPlantId(plant.getPlant_id());

                String specimenStr = specimenList.stream()
                        .map(Specimen::getLocation) // Sau getImageUrl()
                        .collect(Collectors.joining(" | "));

                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(String.valueOf(plant.getPlant_id()));
                row.getCell(1).setText(plant.getName());
                row.getCell(2).setText(specimenStr);
                row.addNewTableCell().setText(""); // Family este gol momentan
            }

            document.write(fos);
            document.close();
            System.out.println("Plants exported successfully to DOC: " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting plants to DOC: " + e.getMessage());
        }
    }
}
