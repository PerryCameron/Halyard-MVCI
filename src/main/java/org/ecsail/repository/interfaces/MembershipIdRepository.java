package org.ecsail.repository.interfaces;

import org.ecsail.fx.MembershipIdFx;

import java.util.List;

public interface MembershipIdRepository {
    List<MembershipIdFx> getIds();
    List<MembershipIdFx> getIds(int ms_id);
    MembershipIdFx getId(int ms_id);

    MembershipIdFx getCurrentId(int ms_id);

    MembershipIdFx getMembershipIdFromMsid(int msid);
    MembershipIdFx getMsidFromMembershipID(int membership_id);
    MembershipIdFx getMembershipId(String year, int ms_id);
    MembershipIdFx getMembershipIdObject(int mid);
    MembershipIdFx getHighestMembershipId(String year);
    boolean isRenewedByMsidAndYear(int ms_id, String year);
    List<MembershipIdFx> getAllMembershipIdsByYear(int year);

    List<MembershipIdFx> getActiveMembershipIdsByYear(String year);
    int getNonRenewNumber(int year);
    int getMsidFromYearAndMembershipId(int year, String membershipId);
    int update(MembershipIdFx o);

    int delete(MembershipIdFx membershipIdDTO);

    int insert(MembershipIdFx membershipIdDTO);

    int getMembershipIdForNewestMembership();

    int deleteMembershipId(int msId);
}
