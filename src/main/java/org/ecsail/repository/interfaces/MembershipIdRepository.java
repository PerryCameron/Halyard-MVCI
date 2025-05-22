package org.ecsail.repository.interfaces;

import org.ecsail.fx.MembershipIdDTOFx;

import java.util.List;

public interface MembershipIdRepository {
    List<MembershipIdDTOFx> getIds();
    List<MembershipIdDTOFx> getIds(int ms_id);
    MembershipIdDTOFx getId(int ms_id);

    MembershipIdDTOFx getCurrentId(int ms_id);

    MembershipIdDTOFx getMembershipIdFromMsid(int msid);
    MembershipIdDTOFx getMsidFromMembershipID(int membership_id);
    MembershipIdDTOFx getMembershipId(String year, int ms_id);
    MembershipIdDTOFx getMembershipIdObject(int mid);
    MembershipIdDTOFx getHighestMembershipId(String year);
    boolean isRenewedByMsidAndYear(int ms_id, String year);
    List<MembershipIdDTOFx> getAllMembershipIdsByYear(int year);

    List<MembershipIdDTOFx> getActiveMembershipIdsByYear(String year);
    int getNonRenewNumber(int year);
    int getMsidFromYearAndMembershipId(int year, String membershipId);
    int update(MembershipIdDTOFx o);

    int delete(MembershipIdDTOFx membershipIdDTO);

    int insert(MembershipIdDTOFx membershipIdDTO);

    int getMembershipIdForNewestMembership();

    int deleteMembershipId(int msId);
}
