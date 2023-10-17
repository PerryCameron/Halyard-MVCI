package org.ecsail.mvci_membership;

public enum MembershipMessage {
    UPDATE_MEMBERSHIP_LIST,
    UPDATE_AWARD,
    UPDATE_BOAT,
    UPDATE_EMAIL,
    UPDATE_MEMBERSHIP_ID,
    UPDATE_NOTE,
    UPDATE_OFFICER,
    UPDATE_PHONE,
    UPDATE_PERSON,
    INSERT_AWARD,
    INSERT_BOAT,
    INSERT_EMAIL,
    INSERT_MEMBERSHIP_ID,
    INSERT_NOTE,
    INSERT_OFFICER,
    INSERT_PHONE,
    INSERT_PERSON,
    INSERT_INVOICE,
    DELETE_AWARD,
    DELETE_BOAT,
    DELETE_EMAIL,
    DELETE_MEMBERSHIP_ID,
    DELETE_NOTE,
    DELETE_OFFICER,
    DELETE_PHONE,
    DELETE_INVOICE,
    UPLOAD_MEMBER_PHOTO,
    NONE,
    CHANGE_MEMBER_TYPE,
    DETACH_MEMBER_FROM_MEMBERSHIP,
    DETACH_PRIMARY_MEMBER_FROM_MEMBERSHIP,
    MOVE_SECONDARY_TO_PRIMARY,
    DELETE_MEMBER_FROM_DATABASE,
    DELETE_MEMBER_FROM_DATABASE_SUCCEED,
    DELETE_PRIMARY_MEMBER_FROM_DATABASE_SUCCEED,
    DATA_LOAD_SUCCEED,
    INSERT_PERSON_SUCCEED,
    MOVE_SECONDARY_TO_PRIMARY_SUCCEED,
    DELETE_MEMBER_FROM_DATABASE_FAIL,
    MOVE_MEMBER_TO_MEMBERSHIP,
    RELEASE_SUBLEASE,
    REASSIGN_SLIP,
    SUBLEASE_SLIP,
    SET_WAIT_LIST,
    EMAIL_IS_PRIMARY_USE,
    LOAD_INVOICES,
    LOAD_INVOICE,
    LOAD_IDS;
}
