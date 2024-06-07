package org.ecsail.repository.interfaces;


import org.ecsail.dto.*;

import java.util.List;

public interface BoatRepository {

    List<BoatListDTO> getActiveSailBoats();
    List<BoatListDTO> getActiveAuxBoats();
    List<BoatListDTO> getAllSailBoats();
    List<BoatListDTO> getAllAuxBoats();
    List<BoatListDTO> getAllBoats();
    List<BoatDTO> getBoatsByMsId(int msId);
    List<BoatDTO> getOnlySailboatsByMsId(int msId);
    List<BoatOwnerDTO> getBoatOwnersByBoatId(int boatId);
    List<BoatPhotosDTO> getImagesByBoatId(int boat_id);
    int update(BoatDTO o);
    int update(BoatListDTO boatListDTO);
    int update(BoatPhotosDTO boatPhotosDTO);
    int updateAux(boolean aux, int boatId);
    int delete(BoatDTO o);
    int insert(BoatDTO o);
    int insert(BoatPhotosDTO boatPhotosDTO);
    int delete(BoatPhotosDTO boatPhotosDTO);
    int insertOwner(BoatOwnerDTO boatOwnerDTO);
    int deleteBoatOwner(MembershipListDTO membershipListDTO, BoatListDTO boatListDTO);


    int deleteBoatOwner(MembershipListDTO membershipListDTO, BoatDTO boatDTO);

    int setAllDefaultImagesToFalse(int boatId);

    int setDefaultImageTrue(int id);

    int deleteBoatOwner(int msId);
}
