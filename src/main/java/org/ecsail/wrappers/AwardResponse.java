package org.ecsail.wrappers;

import org.ecsail.abstractions.ResponseWrapper;
import org.ecsail.pojo.Award;

public class AwardResponse extends ResponseWrapper<Award> {


    public AwardResponse(Award award) {
        super(award);
    }

    public AwardResponse() {
        super(null);
    }

    @Override
    protected Award createDefaultInstance() {
        return new Award();
    }

    public Award getAward() {
        return getData();
    }

    public void setAward(Award award) {
        setData(award);
    }
}

