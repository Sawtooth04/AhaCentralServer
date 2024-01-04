package com.sawtooth.ahacentralserver.models.chunk;

import org.springframework.hateoas.RepresentationModel;

public class ChunkLastModifiedResponse extends RepresentationModel<ChunkLastModifiedResponse> {
    private long lastModified;

    public ChunkLastModifiedResponse() {
        this.lastModified = 0;
    }

    public ChunkLastModifiedResponse(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setData(long lastModified) {
        this.lastModified = lastModified;
    }
}
