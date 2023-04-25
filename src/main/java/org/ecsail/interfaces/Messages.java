package org.ecsail.interfaces;

public interface Messages {

    enum MessageType {
        INSERT,
        DELETE,
        UPDATE,
        NONE,
        CHANGE_MEMBER_TYPE,
        REMOVE_MEMBER_FROM_MEMBERSHIP,
        DELETE_MEMBER_FROM_DATABASE,
        MOVE_MEMBER_TO_MEMBERSHIP;
    }

}
