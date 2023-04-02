package org.ecsail.repository.interfaces;

import com.ecsail.dto.MembershipListDTO;

import java.util.List;

public interface MembershipRepository {

    List<MembershipListDTO> getActiveRoster(String selectedYear);
    List<MembershipListDTO> getInActiveRoster(String selectedYear);
    List<MembershipListDTO> getAllRoster(String selectedYear);
    List<MembershipListDTO> getNewMemberRoster(String selectedYear);
    List<MembershipListDTO> getReturnMemberRoster(String selectedYear);
    List<MembershipListDTO> getSlipWaitList(String selectedYear);
}
