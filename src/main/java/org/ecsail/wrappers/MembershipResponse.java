package org.ecsail.wrappers;

import com.fasterxml.jackson.databind.JsonNode;
import org.ecsail.pojo.Membership;

public class MembershipResponse {
    private boolean success;
    private Membership membership;
    private String message;

    public MembershipResponse() {
        success = false;
        membership = null;
    }

    public MembershipResponse(Membership membership) {
        this.success = false;
        this.membership = membership;
        this.message = "";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}



