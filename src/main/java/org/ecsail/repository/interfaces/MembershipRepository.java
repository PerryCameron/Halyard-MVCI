package org.ecsail.repository.interfaces;


import org.ecsail.dto.MembershipListDTO;

import java.util.List;

public interface MembershipRepository {

    List<MembershipListDTO> getActiveRoster(Integer selectedYear);
    List<MembershipListDTO> getInActiveRoster(Integer selectedYear);
    List<MembershipListDTO> getAllRoster(Integer selectedYear);
    List<MembershipListDTO> getNewMemberRoster(Integer selectedYear);
    List<MembershipListDTO> getReturnMemberRoster(Integer selectedYear);
    List<MembershipListDTO> getSlipWaitList(Integer selectedYear);
    List<MembershipListDTO> getMembershipByBoatId(Integer boatId);
}
