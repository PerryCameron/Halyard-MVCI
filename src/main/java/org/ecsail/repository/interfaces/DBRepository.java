package org.ecsail.repository.interfaces;

import com.mysql.cj.xdevapi.Schema;
import org.ecsail.dto.SchemaDTO;

import java.util.List;

public interface DBRepository {
    List<SchemaDTO> getSchema(String dbName);
}
