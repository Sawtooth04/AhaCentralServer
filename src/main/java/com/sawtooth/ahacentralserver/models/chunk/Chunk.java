package com.sawtooth.ahacentralserver.models.chunk;

public record Chunk(int chunkID, int fileID, String name, int size, int sequenceNumber) {
}
