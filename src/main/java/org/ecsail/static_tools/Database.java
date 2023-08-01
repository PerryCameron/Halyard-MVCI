package org.ecsail.static_tools;

import org.ecsail.dto.SchemaDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.repository.implementations.DBRepositoryImpl;
import org.ecsail.repository.interfaces.DBRepository;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database implements ConfigFilePaths {

    public static void  BackUp(DataSource datasource) {
        DBRepository dbRepository = new DBRepositoryImpl(datasource);
        ArrayList<SchemaDTO> schemaDTOS = (ArrayList<SchemaDTO>) dbRepository.getSchema("ECSC_SQL");
        schemaDTOS.forEach(System.out::println);
        ArrayList<String> sql = (ArrayList<String>) SchemaToSql.convertToSql(schemaDTOS);
        String temp = "/Users/parrishcameron/Documents/JavaFX Projects/Halyard-MVCI/src/main/resources/database/test.sql";
//        SCRIPTS_FOLDER + "/today.txt"
        writeToFile(sql,temp);
    }

    public static void writeToFile(List<String> lines, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
