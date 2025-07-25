package org.ecsail.wrappers;

import org.ecsail.abstractions.ResponseWrapper;
import org.ecsail.fx.Picture;

public class PictureResponse extends ResponseWrapper<Picture> {

    public PictureResponse(Picture picture) {
        super(picture);
    }

    public PictureResponse() {
        super(null);
    }

    @Override
    protected Picture createDefaultInstance() {
        return new Picture();
    }

    public Picture getPicture() {
        return getData();
    }

    public void setPicture(Picture Picture) {
        setData(Picture);
    }
}

