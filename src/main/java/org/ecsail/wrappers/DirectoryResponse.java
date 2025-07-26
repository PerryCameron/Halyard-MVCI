package org.ecsail.wrappers;

import org.ecsail.abstractions.ResponseWrapper;

public class DirectoryResponse extends ResponseWrapper<byte[]> {

    public DirectoryResponse(byte[] bytes) {
        super(bytes);
    }

    public DirectoryResponse() {
        super(null);
    }

    @Override
    protected byte[] createDefaultInstance() {
        return new byte[0]; // Empty byte array as default
    }

    public byte[] getDirectory() {
        return getData();
    }

    public void setDirectory(byte[] bytes) {
        setData(bytes);
    }
}

