package org.ecsail.wrappers;


import org.ecsail.abstractions.ResponseWrapper;
import org.ecsail.pojo.Officer;

public class PositionResponse extends ResponseWrapper<Officer>{

    public PositionResponse(Officer position) {
        super(position);
    }

    public PositionResponse() {
        super(null);
    }

    @Override
    protected Officer createDefaultInstance() {
        return new Officer();
    }

    public Officer getPosition() {
        return getData();
    }

    public void setPosition(Officer position) {
        setData(position);
    }
}

