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
    List<BoatListDTO> getAllBoats();
    List<BoatDTO> getBoatsByMsId(int msId);
    List<BoatDTO> getOnlySailboatsByMsId(int msId);
    List<BoatOwnerDTO> getBoatOwners();
    int update(BoatDTO o);
    int updateAux(boolean aux, int boatId);
    int delete(BoatDTO o);
    int insert(BoatDTO o);
    int insertOwner(BoatOwnerDTO boatOwnerDTO);
}
