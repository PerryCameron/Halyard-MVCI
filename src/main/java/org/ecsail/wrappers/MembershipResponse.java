package org.ecsail.wrappers;

import org.ecsail.abstractions.ResponseWrapper;
import org.ecsail.pojo.Membership;

public class MembershipResponse extends ResponseWrapper<Membership> {

    public MembershipResponse(Membership membership) {
        super(membership);
    }

    public MembershipResponse() {
        super(null);
    }

    @Override
    protected Membership createDefaultInstance() {
        return new Membership();
    }

    public Membership getMembership() {
        return getData();
    }

    public void setMembership(Membership membership) {
        setData(membership);
    }

}



