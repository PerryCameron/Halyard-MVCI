package org.ecsail.static_tools;

import org.ecsail.dto.SchemaDTO;
import org.ecsail.repository.implementations.DBRepositoryImpl;
import org.ecsail.repository.interfaces.DBRepository;

import javax.sql.DataSource;
import java.util.ArrayList;

public class Database {

    public static void  BackUp(DataSource datasource) {
        DBRepository dbRepository = new DBRepositoryImpl(datasource);
        ArrayList<String> sql = (ArrayList<String>) SchemaToSql.convertToSql(dbRepository.getSchema("ECSC_SQL"));
//        ArrayList<SchemaDTO> schemaDTOS = (ArrayList<SchemaDTO>) dbRepository.getSchema("ECSC_SQL");
        sql.forEach(System.out::println);
    }
}
