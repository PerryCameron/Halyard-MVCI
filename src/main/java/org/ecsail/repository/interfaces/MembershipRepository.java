package org.ecsail.repository.interfaces;


import org.ecsail.dto.MembershipDTO;
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
    int update(MembershipListDTO membershipListDTO);

    int updateJoinDate(MembershipListDTO membershipListDTO);

    MembershipListDTO getMembershipByMembershipId(int membershipId);

    MembershipListDTO getMembershipByMsId(int membershipId);

    MembershipListDTO insertMembership(MembershipListDTO nm);

    MembershipListDTO getMembershipListByIdAndYear(int membershipId, int year);

    MembershipDTO getCurrentMembershipChair();

    int deleteFormMsIdHash(int ms_id);

    int deleteMembership(int msId);
}
