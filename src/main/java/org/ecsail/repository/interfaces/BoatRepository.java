package org.ecsail.repository.interfaces;


import org.ecsail.dto.BoatDTO;
import org.ecsail.dto.BoatListDTO;
import org.ecsail.dto.BoatOwnerDTO;

import java.util.List;

public interface BoatRepository {

    List<BoatListDTO> getActiveSailBoats();
    List<BoatListDTO> getActiveAuxBoats();
    List<BoatListDTO> getAllSailBoats();
    List<BoatListDTO> getAllAuxBoats();
    List<BoatListDTO> getAllBoatLists();
    List<BoatDTO> getAllBoats();
    List<BoatDTO> getBoatsByMsId(int msId);
    List<BoatDTO> getOnlySailboatsByMsId(int msId);
    List<BoatOwnerDTO> getBoatOwners();
    int update(BoatDTO o);
}
