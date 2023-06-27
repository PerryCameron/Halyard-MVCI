package org.ecsail.mvci_membership;

public interface MembershipMessages {

    enum action {
        INSERT,
        DELETE,
        UPDATE,
        NONE,
        CHANGE_MEMBER_TYPE,
        REMOVE_MEMBER_FROM_MEMBERSHIP,
        DELETE_MEMBER_FROM_DATABASE,
        MOVE_MEMBER_TO_MEMBERSHIP,
        RELEASE_SUBLEASE,
        REASSIGN_SLIP,
        SUBLEASE_SLIP,
        SET_WAIT_LIST;
    }

}
