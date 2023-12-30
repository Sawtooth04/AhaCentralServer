package com.sawtooth.ahacentralserver.storage.repositories.chunk;

import com.sawtooth.ahacentralserver.models.chunk.Chunk;
import com.sawtooth.ahacentralserver.storage.repositories.IRepository;

public interface IChunkRepository extends IRepository {
    public void Put(Chunk chunk);
}
