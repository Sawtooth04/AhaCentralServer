package com.sawtooth.ahacentralserver.models.chunk;

import org.springframework.hateoas.RepresentationModel;

public class ChunkDownloadModel extends RepresentationModel<ChunkDownloadModel> {
    private byte[] data;

    public ChunkDownloadModel() {
        this.data = null;
    }

    public ChunkDownloadModel(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

