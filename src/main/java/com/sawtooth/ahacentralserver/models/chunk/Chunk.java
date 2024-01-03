package com.sawtooth.ahacentralserver.models.chunk;

public record Chunk(int chunkID, int fileID, String name, int size, int sequenceNumber) {
    public Chunk WithSize(int size) {
        return new Chunk(chunkID(), fileID(), name(), size, sequenceNumber());
    }
}
