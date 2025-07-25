package org.ecsail.repository.interfaces;


import org.ecsail.fx.*;

import java.util.List;

public interface BoatRepository {

    List<BoatListDTO> getActiveSailBoats();
    List<BoatListDTO> getActiveAuxBoats();
    List<BoatListDTO> getAllSailBoats();
    List<BoatListDTO> getAllAuxBoats();
    List<BoatListDTO> getAllBoats();
    List<BoatFx> getBoatsByMsId(int msId);
    List<BoatFx> getOnlySailboatsByMsId(int msId);
    List<BoatOwnerFx> getBoatOwnersByBoatId(int boatId);
    List<BoatPhotosDTO> getImagesByBoatId(int boat_id);
    int update(BoatFx o);
    int update(BoatListDTO boatListDTO);
    int update(BoatPhotosDTO boatPhotosDTO);
    int updateAux(boolean aux, int boatId);
    int delete(BoatFx o);
    int insert(BoatFx o);
    int insert(BoatPhotosDTO boatPhotosDTO);
    int delete(BoatPhotosDTO boatPhotosDTO);
    int insertOwner(BoatOwnerFx boatOwnerDTO);
    int deleteBoatOwner(MembershipListDTO membershipListDTO, BoatListDTO boatListDTO);


    int deleteBoatOwner(MembershipListDTO membershipListDTO, BoatFx boatDTO);

    int setAllDefaultImagesToFalse(int boatId);

    int setDefaultImageTrue(int id);

    int deleteBoatOwner(int msId);
}
